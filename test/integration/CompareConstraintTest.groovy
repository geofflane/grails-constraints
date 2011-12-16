import org.junit.Test
import net.zorched.test.Compared

class CompareConstraintTest {

    @Test
    void testCompareConstraint() {
        def a = new Compared(age: 35, maxAge: 100)
        assert a.validate()
        
        a.maxAge = 18
        assert ! a.validate()
    }
}
