import net.zorched.test.Person
import org.junit.Test

class SameConstraintOnDifferentProperties {

    @Test
    void testMultipleBeginsWithDontStepOnEachOthersToes() {
        def p = new Person(firstName: "Geoffrey", middleName: "Michael", lastName: "Lane")
        assert p.validate()

        p.firstName = "Michael"
        assert ! p.validate()
    }
}