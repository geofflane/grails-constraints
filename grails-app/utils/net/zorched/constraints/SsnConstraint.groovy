package net.zorched.constraints

class SsnConstraint {

    static defaultMessage = "Property [{0}] of class [{1}] with value [{2}] is not a valid SSN"

    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { propertyValue ->
        return propertyValue ==~ /\d{3}(-)?\d{2}(-)?\d{4}/
    }
}
