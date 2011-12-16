import net.zorched.test.InjectedDom
import org.junit.Test

public class InjectedConstraintsTests {

    @Test
    void testInjectedValidations() {
        def a = new InjectedDom(foo:"xxx")
        assert a.validate()
    }
}
