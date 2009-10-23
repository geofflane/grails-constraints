import net.zorched.constraints.UsPhoneConstraint

class UsPhoneTests extends GroovyTestCase {

    void testValidUsPhones() {
        def v = new UsPhoneConstraint()

        assert v.validate("555-555-1212")
        assert v.validate("1-800-555-1212")
    }

    void testInvalidUsPhones() {
        def v = new UsPhoneConstraint()

        assert ! v.validate("555-555-12123")
        assert ! v.validate("1-800-5553-1212")
        assert ! v.validate("1-800-5553-xxxx")
        assert ! v.validate("         ")
        assert ! v.validate("")
        assert ! v.validate(null)
    }
}
