package net.zorched.test

class Category {
    String name
    
    static constraints = {
        name(uniqueEg: true)
    }
}