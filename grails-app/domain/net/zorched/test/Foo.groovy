package net.zorched.test

class Foo {
    String bar
    List baz

    static constraints = {
        bar(twoLong: true)
        baz(twoLong: true)
    }
}
