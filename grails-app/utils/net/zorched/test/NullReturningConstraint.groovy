package net.zorched.test

class NullReturningConstraint {
    def validate = { val ->
        return null
    }
}
