/* Copyright 2009 the original author or authors.
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

import java.util.List;
import java.util.Map;

/**
 * Grails artefact class which represents a constraint.
 *
 * @author Geoff Lane
 * 
 * @since 0.1
 */
public class DefaultGrailsConstraintClass extends AbstractInjectableGrailsClass implements GrailsConstraintClass {

	public static final String CONSTRAINT = "Constraint";

    private static final String EXPECTS_PARAMS_PROPERTY = "expectsParams";
    private static final String NAME_PROPERTY = "name";

    /** The default error message if none is found in the message.properties */
    private static final String DEFAULT_MESSAGE_PROPERTY = "defaultMessage";
    /** The default message key to look up in messages.properties */
    private static final String DEFAULT_MESSAGE_CODE_PROPERTY = "defaultMessageCode";
    /** The default suffix to append to Class.property in the messages.properties for a custom error message*/
    private static final String FAILURE_CODE_PROPERTY = "failureCode";

    private static final String PERSISTENT_PROPERTY = "persistent";
    
    private static final String VALIDATE_CLOSURE = "validate";
    private static final String SUPPORTS_CLOSURE = "supports";
    
    private Closure validateMethod;
    private Closure supportsMethod;

    public DefaultGrailsConstraintClass(Class clazz) {
		super(clazz, CONSTRAINT);
        validateMethod = (Closure) getPropertyValue(VALIDATE_CLOSURE, Closure.class);
        supportsMethod = (Closure) getPropertyValue(SUPPORTS_CLOSURE, Closure.class);
    }

    public Closure getValidationMethod() {
        return validateMethod;
    }

    public Closure getSupportsMethod() {
        return supportsMethod;
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
            return obj.booleanValue();
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
