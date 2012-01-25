package net.zorched.test

class Subjugated {
    String foo

    static constraints = {
        foo(powerHungry: true, beginsWith: "zzz")
    }
}
