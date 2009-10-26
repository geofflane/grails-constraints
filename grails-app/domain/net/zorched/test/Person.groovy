package net.zorched.test

class Person {
    String firstName
    String middleName
    String lastName

    static constraints = {
        middleName(beginsWith: 'M')
        firstName(beginsWith: 'G')
        lastName(startsAndEndsWith: [start: "L", end: "e"])
    }
}