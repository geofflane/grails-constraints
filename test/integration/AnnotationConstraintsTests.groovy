import net.zorched.test.AddressAnno

public class AnnotationConstraintsTests extends GroovyTestCase {

    static String goodPhone = '555-555-1212'

    void testSimpleValidationsOnAnnotatedClass() {
        def a = new AddressAnno(street: "123 Sesame St.", zip: "53212", phone: "xxx")
        assert ! a.validate()
    }

    void testGoodValidationsOnAnnotatedClass() {
        def a = new AddressAnno(street: "123 Sesame St.", zip: "53212", phone: goodPhone)
        assert a.validate()
    }
}
