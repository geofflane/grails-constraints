import net.zorched.test.Login

class AttributesFromTargetClassTests extends GroovyTestCase {

    void testLoginComparisonWorks() {
        def l = new Login(pass: "secret", compare: "secret")
        assert l.validate()
    }

    void testWrongLoginComparisonWorks() {
        def l = new Login(pass: "secret", compare: "xxx")
        assert ! l.validate()
    }
}
