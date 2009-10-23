package net.zorched.test

class Address {
    String street
    String zip
    String phone

    static constraints = {
        zip(usZip: true)
        phone(usPhone: true)
    }
}
