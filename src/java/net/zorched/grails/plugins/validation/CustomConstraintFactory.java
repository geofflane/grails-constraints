/* Copyright 2006-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.zorched.grails.plugins.validation;

import org.codehaus.groovy.grails.validation.AbstractConstraint;
import org.codehaus.groovy.grails.validation.ConstrainedProperty;
import org.codehaus.groovy.grails.validation.Constraint;
import org.codehaus.groovy.grails.validation.ConstraintFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Constraint factory which instantiates new Constraints. This provides an adapter between the DefaultGrailsConstraintClass
 * and the Constraint API.
 *
 * @author Geoff Lane
 * @since 0.1
 */
public class CustomConstraintFactory implements ConstraintFactory {

    private DefaultGrailsConstraintClass constraint;
    private ApplicationContext applicationContext;

    public CustomConstraintFactory(DefaultGrailsConstraintClass constraint, ApplicationContext applicationContext) {
        this.constraint = constraint;
        this.applicationContext = applicationContext;
    }

    /**
     * Create a new Constraint instance
     * @return A CustomConstraintClass instance
     */
    public Constraint newInstance() {
        return new CustomConstraintClass(constraint);
    }

    /**
     * Constraint implementation that invokes validate() on the DefaultGrailsConstraintClass instance.
     */
    public class CustomConstraintClass extends AbstractConstraint {

        private DefaultGrailsConstraintClass constraint;

        public CustomConstraintClass(DefaultGrailsConstraintClass constraint) {
            this.constraint = constraint;
        }

        @Override
        protected void processValidate(Object target, Object propertyValue, Errors errors) {
            int validationParamCount = constraint.getValidationPropertyCount();
            ArrayList<Object> params = new ArrayList<Object>();
            if (validationParamCount > 0)
                params.add(propertyValue);
            if (validationParamCount > 1)
                params.add(target);
            if (validationParamCount > 2)
                params.add(errors);

            RequestContextHolder.currentRequestAttributes().setAttribute(RequestConstraintApi.CONSTRAINT_REQUEST_ATTRIBUTE, this, 0);

            // Inject dependencies
            applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(constraint.getReferenceInstance(), AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);

            Object result = constraint.validate(params.toArray());
            boolean isValid = false;
            String errmsg = constraint.getFailureCode();
            Object[] args = null;

            if (result == null) {
                isValid = true;
            } else if (result instanceof Boolean) {
                isValid = (Boolean) result;
            } else if (result instanceof CharSequence) {
                isValid = false;
                errmsg = result.toString();
            } else if ((result instanceof Collection<?>) || result.getClass().isArray()) {
                isValid = false;
                Object[] values = (result instanceof Collection<?>) ? ((Collection<?>)result).toArray() : (Object[])result;
                if (!(values[0] instanceof String)) {
                    throw new IllegalArgumentException("Return value from validation closure [" +
                            ConstrainedProperty.VALIDATOR_CONSTRAINT+"] of property ["+constraintPropertyName+"] of class [" +
                            constraintOwningClass+"] is returning a list but the first element must be a string " +
                            "containing the error message code");
                }
                errmsg = (String)values[0];
                args = new Object[values.length - 1 + 3];
                int i = 0;
                args[i++] = constraintPropertyName;
                args[i++] = constraintOwningClass;
                args[i++] = propertyValue;
                System.arraycopy(values, 1, args, i, values.length - 1);
            } else {
                throw new IllegalArgumentException("Return value from validation closure [" +
                        ConstrainedProperty.VALIDATOR_CONSTRAINT+"] of property [" + constraintPropertyName +
                        "] of class [" + constraintOwningClass +
                        "] must be a boolean, a string, an array or a collection");
            }

            if (! isValid) {
                if (args == null) {
                    args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue };
                }

                super.rejectValue(target, errors, constraint.getDefaultMessageCode(), errmsg, args);
            }
        }

        public boolean supports(Class aClass) {
            Method method = ReflectionUtils.findMethod(constraint.getClass(), GrailsConstraintClass.SUPPORTS, new Class[]{Object.class});
            if (method != null) {
                return (Boolean) ReflectionUtils.invokeMethod(method, constraint, aClass);
            }
            return true;
        }

        public String getName() {
            return constraint.getName();
        }

        protected boolean skipBlankValues() {
            return constraint.skipBlankValues();
        }

        protected boolean skipNullValues() {
            return constraint.skipNullValues();
        }
        
        public Class getConstraintOwningClass() {
            return this.constraintOwningClass;
        }

        public void setParameter(Object constraintParameter) {
            constraint.validateParams(constraintParameter, constraintPropertyName, constraintOwningClass);
            super.setParameter(constraintParameter);
        }
    }
}
