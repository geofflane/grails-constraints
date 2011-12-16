import net.zorched.test.Login
import org.junit.Test

class AttributesFromTargetClassTests {

    @Test
    void testLoginComparisonWorks() {
        def l = new Login(pass: "secret", compare: "secret")
        assert l.validate()
    }

    @Test
    void testWrongLoginComparisonWorks() {
        def l = new Login(pass: "secret", compare: "xxx")
        assert ! l.validate()
    }
}
