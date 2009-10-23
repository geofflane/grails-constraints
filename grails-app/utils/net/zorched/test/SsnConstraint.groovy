package net.zorched.test

class SsnConstraint {

    static name = "social"
    static defaultMessageCode = "default.not.ssn.message"
    static failureCode = "not.ssn"

    def supports = { type ->
        return type!= null && String.class.isAssignableFrom(type);
    }

    def validate = { propertyValue ->
        return propertyValue ==~ /\d{3}(-)?\d{2}(-)?\d{4}/
    }
}
