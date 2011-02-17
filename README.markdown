# Custom Constraints #
This Grails plugin allows you to create custom domain Constraints for validating Domain objects.

Without this plugin, if you have a custom validation that you want to perform on
a Domain object, you have to use a generic *validator* constraint and define it inline.
With this plugin, you can create reusable, shareable constraints that you can use on
multiple Domain objects. You can then package Constraints in plugins of their own and reuse them across
projects as well.

### Please Note: ###
Plugins are not loaded during Unit Tests, so you cannot test constraints in your unit tests. They should work during
integration tests though, so you can test them there.

## Get Started ##

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

The default format is: default.invalid.constraintName.message

## Apply the Constraint to a domain class ##
	class Person {
	    String phone

		static constraints = {
		    phone(usPhone: true)
		}
	}

## Details

### Constraint parameters ###
Any parameters passed to the constraint will be available in your Constraint object via the *params*
property.

e.g.
	class FooDomain {
		String prop
		static constraints = {
			prop(someConstraint: ['a':1, 'b':2])
		}
	}

	def validate = { val ->
		def a = params.a
		def b = params.b
		return val == a + b
    }

### validate closure (required) ###
The validate closure is the main part of the algorithm where validation is performed. It should return true if the
validation was successful and false if the validation did not succeed.

The validate closure takes up to 3 parameters:

1. The value to be validated
2. The target object being validated
3. The validation errors collection

e.g.
	def validate = { thePropertyValue, theTargetObject, errorsListYouProbablyWontEverNeed ->
		return null != thePropertyValue && theTargetObject.rocks()
    }


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

The default value is *default.$name.invalid.message*
You can override this by providing a static variable:

	static defaultMessageCode = "default.something.unset.message"

### failureCode property (optional) ###
The failureCode property defines a key that can be used to lookup error messages in the *grails-app/i18n/messages.properties* files.
The value of this property is appended to the end of the Class.property name that the Constraint is applied to.

The default value is *invalid.$name*
e.g.:
With a CustomConstraint defined the default entry in messages.properties will be something like:
Person.firstName.custom.invalid

You can override this by providing a static variable:

	static failureCode = "unset.constraint"

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

### persistent property (optional) ###
If you need access to the database to perform your validation, you can make your Constraint a persistent constraint by
setting the static property *persist = true* in your Constraint class.

This will make a *hibernateTemplate* property available to your Constraint that you can use to access the database.
Generally these will be more complicated to write because they require knowledge of the details of the Domain

Set this property in your Constraint class with:

	static persistent = true

> ### Note ###
> Persistent constraints are only supported when using the Hibernate plugin.

## Simple Example ##

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
	
## Example Persistent Constraint ##

	import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
	import org.hibernate.Criteria;
	import org.hibernate.FlushMode;
	import org.hibernate.HibernateException;
	import org.hibernate.Session;
	import org.hibernate.criterion.Restrictions;
	import org.springframework.orm.hibernate3.HibernateCallback;

	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Iterator;
	import java.util.List;

	class UniqueEgConstraint {
    
	    static persistent = true
    
	    def dbCall = { propertyValue, Session session -> 
	        session.setFlushMode(FlushMode.MANUAL);

	        try {
	            boolean shouldValidate = true;
	            if(propertyValue != null && DomainClassArtefactHandler.isDomainClass(propertyValue.getClass())) {
	                shouldValidate = session.contains(propertyValue)
	            }
	            if(shouldValidate) {
	                Criteria criteria = session.createCriteria( constraintOwningClass )
	                        .add(Restrictions.eq( constraintPropertyName, propertyValue ))
	                return criteria.list()
	            } else {
	                return null
	            }
	        } finally {
	            session.setFlushMode(FlushMode.AUTO)
	        }
	    }
    
	    def validate = { propertyValue -> 
	        dbCall.delegate = delegate
	        def _v = dbCall.curry(propertyValue) as HibernateCallback
	        def result = hibernateTemplate.executeFind(_v)
        
	        return result ? false : true    // If we find a result, then non-unique
	    }
	}

	
## Notes ###

### Dependency Injection ###
Constraints are standard Grails Artefacts which means that standard things like dependency injection are supported.
You can inject a service or other Spring managed beans into your Constraint class if you need to use it.

e.g.

    class MyCustomConstraint {
		def someService
		
		def validate = { val -> 
			return someService.someMethod(val)
		}
	}

### Logging ###
Like dependency injection, your constraints classes will have access to the *log* property if you want to do logging
in them.

e.g.

    class MyCustomConstraint {
		def validate = { val -> 
			log.debug "Calling MyCustomConstraint with value [${val}]"
			// ...
		}
	}

