class AddressCommand {
    String street
    String zip
    String phone

    static constraints = {
        zip(usZip: true)
        phone(usPhone: true)
    }
}
