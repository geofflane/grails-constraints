import net.zorched.test.Person

class ParameterizedConstraintTests extends GroovyTestCase {

    void testWithMultipleConstraintsPassIfValid() {
        def p = new Person(firstName: "Geoff", lastName: "Lane")
        assert p.validate()
    }

    void testWithMultipleConstraintsDontPassIfNotValid() {
        def p = new Person(firstName: "Mike", lastName: "Lane")
        assert ! p.validate()

        p.firstName = "Geoff"
        p.lastName = "foo"
        assert ! p.validate()
    }
}
