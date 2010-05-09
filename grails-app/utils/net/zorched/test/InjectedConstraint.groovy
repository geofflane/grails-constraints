package net.zorched.test

class InjectedConstraint {

    def myTestService

    def validate = { propertyValue, target ->
        return myTestService.isSomething()
    }
}
