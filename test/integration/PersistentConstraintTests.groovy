import net.zorched.test.Category

class PersistentConstraintTests extends GroovyTestCase {
    
    void testExampleUniqueConstraintWorks() {
        def c = new Category(name: "Foo")
        assert c.save()
        
        def c2 = new Category(name: "Foo")
        assert ! c.save()
        
        c2.name = "Bar"
        assert c2.save()
    }
}