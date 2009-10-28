package net.zorched.test

class BeginsWithConstraint {

    static expectsParams = true

    def validate = { propertyValue, target ->
        log.debug "Called beginWith with value $propertyValue"
        return propertyValue[0] == params
    }
}
