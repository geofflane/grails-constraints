import net.zorched.test.*
import org.junit.Test


public class SimpleConstraintsTests {

    static String goodPhone = '555-555-1212'

    @Test
    void testSimpleValidations() {
        def a = new Address(street: "123 Sesame St.", zip: "53212", phone: goodPhone)
        assert a.validate()

        a.phone = "xxx"
        assert ! a.validate()

        a.phone = goodPhone
        assert a.validate()
    }

    @Test
    void testSsnConstaintNotAppliedDoesntValidate() {
        def u = new User(ssn: "xxx")
        assert ! u.validate()
    }

    @Test
    void testSsnConstaintAppliedValidates() {
        def u = new User(ssn:"123-45-5678")
        assert u.validate()
    }

    @Test
    void testTwoLongConstaintBothWrongDoesntValidate() {
        def f = new Foo(bar: "xxx", baz: [1, 2, 3])
        assert ! f.validate()
    }

    @Test
    void testTwoLongConstaintOneWrongDoesntValidate() {
        def f = new Foo(bar: "xx", baz: [1, 2, 3])
        assert ! f.validate()
    }

    @Test
    void testTwoLongConstaintOtherWrongDoesntValidate() {
        def f = new Foo(bar: "xxx", baz: [1, 2, 3, 4])
        assert ! f.validate()
    }

    @Test
    void testTwoLongConstaintAppliedValidates() {
        def f = new Foo(bar: "xx", baz: ['a', 'b'])
        assert f.validate()
    }

    @Test
    void testTwoLongConstaintAppliedToNullValidates() {
        def f = new Foo(bar: null, baz: ['a', 'b'])
        assert f.validate()
    }
}