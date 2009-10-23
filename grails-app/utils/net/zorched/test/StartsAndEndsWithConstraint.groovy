package net.zorched.test

class StartsAndEndsWithConstraint {

    static expectsParams = ['start', 'end']

    def validate = { propertyValue, target ->
        return propertyValue[0] == params.start && propertyValue[-1] == params.end
    }
}
