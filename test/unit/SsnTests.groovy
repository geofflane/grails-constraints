import net.zorched.constraints.SsnConstraint
import org.apache.commons.logging.LogFactory

class SsnTests extends GroovyTestCase {
    
    void testValidationNotCalledIfFalsePassedAsParam() {
        def v = getConstraint(false)
        
        assert v.validate("555-44-1212")
        assert v.validate("")
        assert v.validate(null)
    }

    void testValidatesSsn() {
        def v = getConstraint(true)
        
        assert v.validate("123-45-6789")
        assert v.validate("000-00-0000")
    }

    void testDoesntValidateInvalidSsns() {
        def v = getConstraint(true)
        
        assert ! v.validate("1")
        assert ! v.validate("12-12-1234")
        assert ! v.validate("123-1-1234")
        assert ! v.validate("53215-45678")
        assert ! v.validate("056789")
        assert ! v.validate("")
        assert ! v.validate("       ")
        assert ! v.validate(null)
    }

    def log = LogFactory.getLog("xxx");
    private def getConstraint(boolean param) {
        def v = new SsnConstraint()
        v.metaClass.getParams = {-> param }
        v.metaClass.getLog = {-> return log }
        return v
    }
}
