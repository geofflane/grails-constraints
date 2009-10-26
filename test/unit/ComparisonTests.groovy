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
    
    void testComparisonOfNumber() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }

        assert v.validate(1, new Test(other: 1))
        assert ! v.validate(1, new Test(other: 2))
    }
    
    void testComparisonOfNullValues() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }

        assert ! v.validate(null, new Test(other:"xxx"))
        assert ! v.validate(null, new Test(other:null))
        assert ! v.validate("xxx", new Test(other:null))
    }
}

class Test {
    def other
}
