import net.zorched.grails.plugins.validation.ConstraintUnitTestMixin
import grails.test.mixin.TestMixin
import org.junit.Test
import net.zorched.constraints.UsPhoneConstraint
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse

/**
 *
 * @author geoff
 * @since 2/10/2012
 */
@TestMixin(ConstraintUnitTestMixin)
class ConstraintUnitTestMixinTest {

    @Test
    void testThatConstraintIsMixedIn() {
        def constraint = testFor(UsPhoneConstraint)

        // Params are automatically mixed in to the test class and exposed
        // to the constraint with the call above.
        params = true

        assertTrue constraint.validate("5135551212")
        assertFalse constraint.validate("bad")
    }
}
