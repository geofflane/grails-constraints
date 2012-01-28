import org.junit.Test
import net.zorched.test.Site

class ValidURLConstraintTest {

    @Test
    void valid_url_constraint_called() {
        def a = new Site(address: "http://zorched.net")
        assert a.validate()
    }
}
