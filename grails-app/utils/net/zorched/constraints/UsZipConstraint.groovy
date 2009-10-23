package net.zorched.constraints

class UsZipConstraint {

    static defaultMessage = "Property [{0}] of class [{1}] with value [{2}] is not a valid US Zipcode"
    
    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { val ->
        return val ==~ /^\d{5}(\-\d{4})?$/
    }
}