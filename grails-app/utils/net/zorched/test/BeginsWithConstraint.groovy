package net.zorched.test

class BeginsWithConstraint {

    static expectsParams = true

    def validate = { propertyValue, target ->
        return propertyValue[0] == params
    }
}
