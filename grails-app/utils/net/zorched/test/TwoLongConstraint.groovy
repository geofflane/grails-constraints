package net.zorched.test

class TwoLongConstraint {

    def validate = { propertyValue, target ->
        if (null == propertyValue) {
            println "null propertyvalue"
            return true
        }
        return propertyValue.size() == 2
    }
}
