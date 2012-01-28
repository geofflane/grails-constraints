package net.zorched.test

/**
 *
 * @author geoff
 * @since 1/28/2012
 */
class ValidURLConstraint {
    static name = "validURL"
    static defaultMessageCode = "default.not.validURL.message"
    def params
    def supports = { type ->
        return type != null && String.class.isAssignableFrom(type);
    }
    def validate = { value ->
        log.debug "Calling ValidURLConstraint with value [${val}]"
        return true
    }
}
