package net.zorched.test

class PowerHungryConstraint {
    static vetoer = true

    def validate = { val ->
        veto = true
        return true
    }
}
