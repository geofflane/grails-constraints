package net.zorched.test

class Login {
    String pass
    String compare

    static constraints = {
        pass(compareTo: "compare")
    }
}
