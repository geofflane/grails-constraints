package net.zorched.test

class HasExtendedValidators {
    String foo
    String bar

    static constraints = {
        foo(errorCode: true)
        bar(errorParams: true)
    }
}
