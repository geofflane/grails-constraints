package net.zorched.test

class Foo {
    String bar
    List baz

    static constraints = {
        bar(twoLong: true, nullable:true)
        baz(twoLong: true)
    }
}
