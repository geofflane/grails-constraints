# Custom Constraints #
This Grails plugin allows you to create custom domain Constraints for validating Domain objects.

Without this plugin, if you have a custom validation that you want to perform on
a Domain object, you have to use a generic *validator* constraint and define it inline.
With this plugin, you can create reusable, shareable constraints that you can use on
multiple Domain objects. You can then package Constraints in plugins of their own and reuse them across
projects as well.

1. Create a groovy file in */grails-app/utils/* called *Constraint.groovy
2. Implement a validate closure
3. Add appropriate messages to */grails-app/i18n/messages.properties*
4. Apply the validation to a Domain class

## Create a Constraint with a validate closure ##
Under */grails-app/utils/*:

	class UsPhoneConstraint {
		def validate = { val -> 
			return val ==~ /^[01]?[- .]?(\([2-9]\d{2}\)|[2-9]\d{2})[- .]?\d{3}[- .]?\d{4}$/
		}
	}

## Add messages to messages.properties ##
Unless you set the *defaultMessage* static property, then it is a good idea to add an entry to messages.properties
with a default format string to show the user if validation fails.

The default format is: default.invalid.<constraint name>.message

## Apply the Constraint to a domain class ##
	class Person {
	    String phone

		static constraints = {
		    phone(usPhone: true)
		}
	}

## Details

### Constraint parameters ###
Any parameters passed to the constraint will be available in your Constraint object via the params
property.

### validate closure (required) ###
The validate closure takes up to 3 parameters

1. The value to be validated
2. The target object being validated
3. The validation errors collection

This is the main validation routine to run to validate the property.

### supports closure (optional) ###
Your Constraint can optionally implement a *supports* closure that will allow you to restrict the types
of the properties that the Constraint can be applied to. This closure will be passed a single argument, a Class that
represents the type of the property that the constraint was applied to.

e.g.:

    class Foo {
        Integer bar
        static constraints = {
            bar(custom: true)
        }
    }

The CustomConstraint will get an Integer class passed to its *supports* closure to check.


### name property (optional) ###
The default name of the Constraint to use in your Domain object is the name of the class, camelCased, 
without the tailing Constraint.

e.g.:

1. MyConstraint -> my
2. UsPhoneConstraint -> usPhone

You can override this by providing a static name variable in your constraint definition:
   static name = "customName"

### defaultMessageCode property (optional) ###
The defaultMessageCode property defines the default key that will be used to look up the error message
in the *grails-app/i18n/messages.properties* files.

### failureCode property (optional) ###
The failureCode property defines a key that can be used to lookup error messages in the *grails-app/i18n/messages.properties* files.
The value of this property is appended to the end of the Class.property name that the Constraint is applied to.

e.g.
With a CustomConstraint defined the default entry in messages.properties will be something like:
Person.firstName.custom.invalid

### defaultMessage property (optional) ###
If no value is found in *messages.properties* for the defaultMessageCode or the failureCode then this message will be
used if it is supplied.

### expectsParams property (optional) ###
The expectsParams static property allows you to define the required parameters for the Constraint.
The expectsParams can be one of:

1. Boolean true, saying a parameter is expected
2. A List of the named parameters that are expected in a map
3. A Closure allowing you to validate the parameters yourself

e.g.:

	static expectsParams = ['start', 'end']
	static expectsParams = true
	static expectsParams = { parameters -> // ... do something }

## Example ##
	class SsnConstraint {

	    static name = "social"
	    static defaultMessageCode = "default.not.social.message"

	    def supports = { type ->
	        return type!= null && String.class.isAssignableFrom(type);
	    }

	    def validate = { propertyValue ->
	        return propertyValue ==~ /\d{3}(-)?\d{2}(-)?\d{4}/
	    }
	}
	
	class Person {
		String ssn
		static constraints = {
			ssn(social: true)
		}
	}
	
	
## Example With Params ##
	class StartsAndEndsWithConstraint {
	   	static expectsParams = ['start', 'end']

		def validate = { propertyValue, target ->
		   return propertyValue[0] == params.start && propertyValue[-1] == params.end
		}
	}
	
	class MyDomain {
		String foo
		static constraints = {
			foo(startsAndEndsWith: [start: 'G', end: 'f'])
		}
	}
	