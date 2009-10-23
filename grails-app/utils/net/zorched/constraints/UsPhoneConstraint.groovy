package net.zorched.constraints

class UsPhoneConstraint {

    static defaultMessage = "Property [{0}] of class [{1}] with value [{2}] is not a valid US Phone number"

    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { val ->
        if (! params)
            return true
        return val ==~ /^[01]?[- .]?(\([2-9]\d{2}\)|[2-9]\d{2})[- .]?\d{3}[- .]?\d{4}$/
    }
}
