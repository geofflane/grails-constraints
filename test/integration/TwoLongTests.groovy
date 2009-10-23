import net.zorched.test.Foo

class TwoLongTests extends GroovyTestCase {

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