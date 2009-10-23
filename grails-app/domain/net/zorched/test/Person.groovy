package net.zorched.test

class Person {
    String firstName
    String lastName

    static constraints = {
        firstName(beginsWith: 'G')
        lastName(startsAndEndsWith: [start: "L", end: "e"])
    }
}