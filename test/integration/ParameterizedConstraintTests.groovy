import net.zorched.test.Person
import org.junit.Test

class ParameterizedConstraintTests {

    @Test
    void testWithMultipleConstraintsPassIfValid() {
        def p = new Person(firstName: "Geoff", middleName: "Michael", lastName: "Lane")
        assert p.validate()
    }

    @Test
    void testWithMultipleConstraintsDontPassIfNotValid() {
        def p = new Person(firstName: "Mike", middleName: "Michael", lastName: "Lane")
        assert ! p.validate()

        p.firstName = "Geoff"
        p.lastName = "foo"
        assert ! p.validate()
    }
}
