package net.zorched.test

class InjectedDom {
    String foo

    static constraints = {
        foo(injected:true)
    }
}
