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
import org.codehaus.groovy.grails.validation.Constraint;
import org.codehaus.groovy.grails.validation.ConstraintFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Errors;


import java.lang.reflect.Method;
import java.util.ArrayList;

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

            // Setup parameters needed by constraints
            constraint.setParameter(constraintParameter);
            if (constraint.isPersistent()) {
                constraint.setHibernateTemplate(applicationContext);
                constraint.setConstraintOwningClass(constraintOwningClass);
                constraint.setConstraintPropertyName(constraintPropertyName);
            }

            // Inject dependencies
            applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(constraint.getReferenceInstance(), AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);

            boolean isValid = constraint.validate(params.toArray());

            if (! isValid) {
                Object[] args = new Object[] { constraintPropertyName, constraintOwningClass, propertyValue, constraintParameter };
                super.rejectValue(target, errors, constraint.getDefaultMessageCode(), constraint.getFailureCode(), args);
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

        public void setParameter(Object constraintParameter) {
            constraint.validateParams(constraintParameter, constraintPropertyName, constraintOwningClass);
            super.setParameter(constraintParameter);
        }
    }
}
