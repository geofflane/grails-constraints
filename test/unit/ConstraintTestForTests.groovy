import grails.test.mixin.TestFor
import org.junit.Test
import net.zorched.constraints.UsPhoneConstraint
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

/**
 *
 * @author geoff
 * @since 2/10/2012
 */
@TestFor(UsPhoneConstraint)
class ConstraintTestForTests {

    @Test
    void testThatConstraintIsMixedIn() {
        // Constraint setup automatically by TestFor

        // Params are automatically mixed in to the test class and exposed
        // to the constraint with the call above.
        params = true

        assert null != constraint
        assertTrue constraint.validate("5135551212")
        assertFalse constraint.validate("bad")
    }
}
