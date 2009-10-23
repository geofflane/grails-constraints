import net.zorched.test.User

class SsnApplicationTests extends GroovyTestCase {

    void testSsnConstaintNotAppliedDoesntValidate() {
        def u = new User(ssn: "xxx")
        assert ! u.validate()
    }

    void testSsnConstaintAppliedValidates() {
        def u = new User(ssn:"123-45-5678")
        assert u.validate()
    }
}
