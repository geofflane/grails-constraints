import org.junit.Test
import net.zorched.test.Compared

class CompareConstraintTest {

    @Test
    void compare_constraint_has_constraintPropertyName_set() {
        def a = new Compared(age: 35, maxAge: 100)
        assert a.validate()
        
        a.maxAge = 18
        assert ! a.validate()
        assert "age".equals(a.errors.allErrors[0].arguments[0])
    }
}
