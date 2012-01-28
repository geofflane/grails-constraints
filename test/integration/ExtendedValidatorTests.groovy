import net.zorched.test.HasExtendedValidators
import org.junit.Test

class ExtendedValidatorTests {
    @Test
    void test_code_returned_from_validator() {
        def a = new HasExtendedValidators(foo:"bar")
        a.validate()

        assert a.errors.hasFieldErrors("foo")
        assert a.errors.getFieldError("foo").code == "error.code"
    }

    @Test
    void test_code_and_arguments_from_validator() {
        def a = new HasExtendedValidators(bar:"foo")
        a.validate()

        assert a.errors.hasFieldErrors("bar")

        def error = a.errors.getFieldError("bar")
        assert error.code == "error.code"
        assert error.arguments[0] == "bar"  // property name
        assert error.arguments[1] == a.class     // constraint owning class
        assert error.arguments[2] == "foo"  // property value
        assert error.arguments[3] == "foo"  // custom param #1
        assert error.arguments[4] == "bar"  // custom param #2
    }

    @Test
    void test_null_returned_from_validator() {
        def a = new HasExtendedValidators(baz:"foo")
        assert a.validate()     // Null returned from validator means valid
    }
}
