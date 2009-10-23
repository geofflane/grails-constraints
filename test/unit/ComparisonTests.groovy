import net.zorched.constraints.ComparisonConstraint

class ComparisonTests extends GroovyTestCase {

    void testComparisonOfMatchingWorks() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }

        assert v.validate("xxx", new Test(other:"xxx"))
    }

    void testComparisonOfNotMatchingWorks() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }

        assert ! v.validate("yyy", new Test(other:"xxx"))
    }
}

class Test {
    String other
}
