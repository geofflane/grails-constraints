package net.zorched.test

class HasExtendedValidators {
    String foo
    String bar
    String baz

    static constraints = {
        foo(nullable: true, errorCode: true)
        bar(nullable: true, errorParams: true)
        baz(nullable: true, nullReturning: true)
    }
}
