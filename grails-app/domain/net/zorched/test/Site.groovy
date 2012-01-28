package net.zorched.test

/**
 * 
 * @author geoff
 * @since 1/28/2012
 */
class Site {
    String address
    static constraints = {
        address(validURL: true)
    }
}
