package net.zorched.test

import org.codehaus.groovy.grails.validation.Validateable

/**
 * net.zorched.test
 * User: geoff
 * Date: Jan 7, 2010
 * Time: 8:28:25 PM
 */
@Validateable
public class AddressAnno {
    String street
    String zip
    String phone

    static constraints = {
        street(blank: false)
        zip(usZip: true)
        phone(usPhone: true)
    }
}
