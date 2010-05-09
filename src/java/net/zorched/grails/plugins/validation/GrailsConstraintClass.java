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

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;

/**
 * Represents a validation class in Grails. This is a mediator around
 * the custom Groovy classes that provides an API to access static values
 * and access to the Closures.
 *
 * @author Geoff Lane
 * @since 0.1
 */
public interface GrailsConstraintClass extends InjectableGrailsClass {

    public static final String CONSTRAINT = "Constraint";

    public static final String EXPECTS_PARAMS_PROPERTY = "expectsParams";
    public static final String NAME_PROPERTY = "name";

    /** The default error message if none is found in the message.properties */
    public static final String DEFAULT_MESSAGE_PROPERTY = "defaultMessage";
    /** The default message key to look up in messages.properties */
    public static final String DEFAULT_MESSAGE_CODE_PROPERTY = "defaultMessageCode";
    /** The default suffix to append to Class.property in the messages.properties for a custom error message*/
    public static final String FAILURE_CODE_PROPERTY = "failureCode";

    public static final String PERSISTENT_PROPERTY = "persistent";

    public static final String VALIDATE = "validate";
    public static final String SUPPORTS = "supports";


    /**
     * The validation method called
     * @return True if the constraint validates
     */
    public boolean validate(Object[] params);

    /**
     * Can the constraint be applied to a specific type?
     * @param aClass The type to check
     * @return True if this constraint can be applied to the type
     */
    public boolean supports(Class aClass);

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
