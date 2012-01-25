package net.zorched.test

class ErrorCodeConstraint {
    def validate = { val ->
        return "error.code"
    }
}
