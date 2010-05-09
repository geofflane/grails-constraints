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

import grails.util.GrailsNameUtils;
import groovy.lang.Closure;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;
import java.util.Map;

/**
 * Grails artefact class which represents a custom Constraint job.
 *
 * @author Geoff Lane
 * @since 0.1
 */
public class DefaultGrailsConstraintClass extends AbstractInjectableGrailsClass implements GrailsConstraintClass {

    public DefaultGrailsConstraintClass(Class clazz) {
        super(clazz, CONSTRAINT);
    }

    public boolean supports(Class aClass) {
        // FIXME: Check if supports exists
        if (null != getMetaClass().respondsTo(getReferenceInstance(), SUPPORTS)) {
            return (Boolean) getMetaClass().invokeMethod(getReferenceInstance(), SUPPORTS, new Object[] { aClass });
        }

        return true;
    }

    public boolean validate(Object[] params) {
        return (Boolean) getMetaClass().invokeMethod(getReferenceInstance(), VALIDATE, params);
    }

    public int getValidationPropertyCount() {
        Closure c = (Closure) getPropertyOrStaticPropertyOrFieldValue(VALIDATE, Closure.class);
        return c.getMaximumNumberOfParameters();
    }

    public void setParameter(Object constraintParameter) {
        getMetaClass().invokeMethod(getReferenceInstance(), "setParams", constraintParameter);
    }

    public void setHibernateTemplate(ApplicationContext applicationContext) {
        if (applicationContext.containsBean("sessionFactory")) {
            HibernateTemplate template = new HibernateTemplate((SessionFactory) applicationContext.getBean("sessionFactory"),true);
            getMetaClass().invokeMethod(getReferenceInstance(), "setHibernateTemplate", template);
        }
    }

    public void setConstraintOwningClass(Object owningClass) {
        getMetaClass().invokeMethod(getReferenceInstance(), "setConstraintOwningClass", owningClass);
    }

    public void setConstraintPropertyName(String constrainedPropertyName) {
        getMetaClass().invokeMethod(getReferenceInstance(), "setConstraintPropertyName", constrainedPropertyName);
    }

    public String getName() {
        return getConstraintName();
	}

	public String getDefaultMessageCode() {
		String obj = (String) getPropertyValue(DEFAULT_MESSAGE_CODE_PROPERTY);
		if (obj == null) return "default.invalid." + getConstraintName() + ".message";
		return obj;
	}

    public String getDefaultMessage() {
		String obj = (String) getPropertyValue(DEFAULT_MESSAGE_PROPERTY);
		if (obj == null) return "Property [{0}] of class [{1}] with value [{2}] is not valid";
		return obj;
	}

    public String getFailureCode() {
		String obj = (String) getPropertyValue(FAILURE_CODE_PROPERTY);
		if (obj == null) return getConstraintName() + ".invalid";
		return obj;
	}

    protected String getConstraintName() {
        String obj = (String) getPropertyValue(NAME_PROPERTY);
		if (obj == null) {
            return GrailsNameUtils.getPropertyName(GrailsNameUtils.getLogicalName(getClazz(), CONSTRAINT));
        }
		return obj;
    }

    public boolean isPersistent() {
        Boolean obj = (Boolean) getPropertyValue(PERSISTENT_PROPERTY);
		if (obj != null) {
            return obj;
        }
        return false;
    }

    public void validateParams(Object constraintParameter, String constraintPropertyName, Class constraintPropertyOwner) {
        Object obj = getPropertyValue(EXPECTS_PARAMS_PROPERTY);
        if (null == obj) {
            return;
        }

        if (obj instanceof Closure) {
            // Call a closure and let the instance validate it
            ((Closure) obj).call(constraintParameter);
        } else if (obj instanceof Boolean) {

            // If it's true, we just need to confirm a single param
            if (null == constraintParameter)
                throw new IllegalArgumentException("Parameter for constraint ["
                    + getConstraintName() + "] of property ["
                    + constraintPropertyName + "] of class ["
                    + constraintPropertyOwner.getName() + "] is required to have a value");
        } else if (obj instanceof List) {

            // FIXME: What if it's not a map?
            Map paramsAsMap = (Map) constraintParameter;
            for (Object item : ((List) obj)) {
                if (! paramsAsMap.containsKey(item)) {
                    throw new IllegalArgumentException("Constraint ["
                        + getConstraintName() + "] of property ["
                        + constraintPropertyName + "] of class ["
                        + constraintPropertyOwner.getName() + "] must contain a parameter called ["
                        + item + "]");
                }
            }
        } else {
            throw new IllegalArgumentException("Not a supported type");
        }
    }
}
