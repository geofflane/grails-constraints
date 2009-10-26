import net.zorched.test.*

public class SimpleConstraintsTests extends GroovyTestCase {

    static String goodPhone = '555-555-1212'

    void testSimpleValidations() {
        def a = new Address(street: "123 Sesame St.", zip: "53212", phone: goodPhone)
        assert a.validate()

        a.phone = "xxx"
        assert ! a.validate()

        a.phone = goodPhone
        assert a.validate()
    }
    
    void testSsnConstaintNotAppliedDoesntValidate() {
        def u = new User(ssn: "xxx")
        assert ! u.validate()
    }

    void testSsnConstaintAppliedValidates() {
        def u = new User(ssn:"123-45-5678")
        assert u.validate()
    }
    
    void testTwoLongConstaintBothWrongDoesntValidate() {
        def f = new Foo(bar: "xxx", baz: [1, 2, 3])
        assert ! f.validate()
    }

    void testTwoLongConstaintOneWrongDoesntValidate() {
        def f = new Foo(bar: "xx", baz: [1, 2, 3])
        assert ! f.validate()
    }

    void testTwoLongConstaintOtherWrongDoesntValidate() {
        def f = new Foo(bar: "xxx", baz: [1, 2, 3, 4])
        assert ! f.validate()
    }

    void testTwoLongConstaintAppliedValidates() {
        def f = new Foo(bar: "xx", baz: ['a', 'b'])
        assert f.validate()
    }
}