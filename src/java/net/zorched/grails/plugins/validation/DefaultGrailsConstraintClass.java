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
import groovy.lang.MetaMethod;
import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import org.codehaus.groovy.runtime.MethodClosure;

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
    private Closure validateMethod;
    private Closure supportsMethod;

    private static final String EXPECTS_PARAMS_PROPERTY = "expectsParams";
    private static final String NAME_PROPERTY = "name";
    private static final String DEFAULT_MESSAGE_CODE_PROPERTY = "defaultMessageCode";
    
    private static final String VALIDATE_CLOSURE = "validate";
    private static final String SUPPORTS_CLOSURE = "supports";

    public DefaultGrailsConstraintClass(Class clazz) {
		super(clazz, CONSTRAINT);
        validateMethod = getMethodOrClosureMethod(VALIDATE_CLOSURE);
        supportsMethod = getMethodOrClosureMethod(SUPPORTS_CLOSURE);
    }

    private Closure getMethodOrClosureMethod(String methodName) {
        Closure closure = (Closure) getPropertyOrStaticPropertyOrFieldValue(methodName, Closure.class);
        if(closure == null) {
            MetaMethod method = getMetaClass().getMetaMethod(methodName, new Object[]{Object.class});
            if(method!=null) {
            	if(method.isStatic()) {
            		closure = new MethodClosure(getClazz(), methodName);
            	} else {
            		closure = new MethodClosure(getReference(), methodName);
            	}
            }
        }
        return closure;
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
		if (obj == null) return "default.not." + getConstraintName() + ".message";
		return obj;
	}

    protected String getConstraintName() {
        String obj = (String) getPropertyValue(NAME_PROPERTY);
		if (obj == null) {
            return GrailsNameUtils.getPropertyName(GrailsNameUtils.getLogicalName(getClazz(), CONSTRAINT));
        }
		return obj;
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
