package net.zorched.test

class ErrorParamsConstraint {
    def validate = { val ->
        return ["error.code", "foo", "bar"]
    }
}
