package net.zorched.test

class Compared {
    int age
    int maxAge

    static constraints = {
        age(compare: [op: '<', field:'maxAge'])
    }
}
