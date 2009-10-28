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

import groovy.lang.Closure;
import org.codehaus.groovy.grails.commons.InjectableGrailsClass;

import java.util.Map;

/**
 * Represents a validation class in Grails. This is a mediator around
 * the custom Groovy classes that provides an API to access static values
 * and access to the Closures.
 * 
 * @author Geoff Lane
 * 
 * @since 0.1
 */
public interface GrailsConstraintClass extends InjectableGrailsClass {
	
	/**
	 * Get the method which is executed by the validator.
	 */
	Closure getValidationMethod();

    /**
     * Get the method which is executed to determine if the constrained property
     * is supported by this Constraint.
     */
    Closure getSupportsMethod();

    /**
     * The name of the constraint that will be used to apply it to a property
     * in a domain class.
     *
     * Defaults to the camelCase of the constraint without the Constraint suffix.
     */
	public String getName();

    /**
     * The default error message that will be shown to the user if the defaultMessageCode
     * or the failureCode is not found in messages.properties
     * 
     * static defaultMessage = "xxx"
     */
    public String getDefaultMessage();

    /**
     * The default error message that will be shown to the user if the
     * failureCode is not found in messages.properties
     * 
     * Defaults to "default.invalid.${name}.message"
     * static defaultMessageCode = "xxx"
     */
    public String getDefaultMessageCode();

    /**
     * The key (appended to the Class.propertyName) that will be looked for first
     * in the messages.properties for the error message to show to the user.
     *
     * Defaults to "${name}.invalid"
     * static failureCode = "xxx"
     */
    public String getFailureCode();
    
    /**
     * Does this constraint need access to the database to perform validation.
     * 
     * Defaults to false
     * static persistent = false
     */
    public boolean isPersistent();
}
