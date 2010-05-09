import net.zorched.test.Address
import net.zorched.test.InjectedDom

public class InjectedConstraintsTests extends GroovyTestCase {

    void testInjectedValidations() {
        def a = new InjectedDom(foo:"xxx")
        assert a.validate()
    }
}
