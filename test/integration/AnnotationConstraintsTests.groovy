import net.zorched.test.AddressAnno
import org.junit.Test

public class AnnotationConstraintsTests {

    static String goodPhone = '555-555-1212'

    @Test
    void testSimpleValidationsOnAnnotatedClass() {
        def a = new AddressAnno(street: "123 Sesame St.", zip: "53212", phone: "xxx")
        assert ! a.validate()
    }

    @Test
    void testGoodValidationsOnAnnotatedClass() {
        def a = new AddressAnno(street: "123 Sesame St.", zip: "53212", phone: goodPhone)
        assert a.validate()
    }
}
