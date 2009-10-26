import net.zorched.test.Person

class SameConstraintOnDifferentProperties extends GroovyTestCase {
    
    void testMultipleBeginsWithDontStepOnEachOthersToes() {
        def p = new Person(firstName: "Geoffrey", middleName: "Michael", lastName: "Lane")
        assert p.validate()

        p.firstName = "Michael"
        assert ! p.validate()
    }
}