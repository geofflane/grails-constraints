import net.zorched.constraints.ComparisonConstraint
import org.apache.commons.logging.LogFactory

class ComparisonTests extends GroovyTestCase {

    void testComparisonOfMatchingWorks() {
        def v = getConstraint()

        assert v.validate("xxx", new Test(other:"xxx"))
    }

    void testComparisonOfNotMatchingWorks() {
        def v = getConstraint()

        assert ! v.validate("yyy", new Test(other:"xxx"))
    }
    
    void testComparisonOfNumber() {
        def v = getConstraint()

        assert v.validate(1, new Test(other: 1))
        assert ! v.validate(1, new Test(other: 2))
    }
    
    void testComparisonOfNullValues() {
        def v = getConstraint()

        assert ! v.validate(null, new Test(other:"xxx"))
        assert ! v.validate(null, new Test(other:null))
        assert ! v.validate("xxx", new Test(other:null))
    }

    def log = LogFactory.getLog("xxx")
    private def getConstraint() {
        def v = new ComparisonConstraint()
        v.metaClass.getParams = {-> "other" }
        v.metaClass.getLog = {-> return log }

        return v
    }
}

class Test {
    def other
}
