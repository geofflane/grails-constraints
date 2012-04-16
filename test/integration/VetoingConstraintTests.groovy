import org.junit.Test
import net.zorched.test.Subjugated

class VetoingConstraintTests {
    @Test
    void testVetoingConstraint() {
        def a = new Subjugated(foo: "bar")

        // This should pass because the vetoing constraint will not cause a validation error, and should cease all
        // further validation.
        assert a.validate()
    }
}
