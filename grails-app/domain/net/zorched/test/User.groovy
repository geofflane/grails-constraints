package net.zorched.test

class User {
    String ssn

    static constraints = {
        ssn(ssn: true)
    }
}
