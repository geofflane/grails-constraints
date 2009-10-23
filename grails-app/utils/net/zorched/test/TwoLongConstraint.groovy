package net.zorched.test

class TwoLongConstraint {

    def validate = { propertyValue, target ->
        return propertyValue.size() == 2
    }
}
