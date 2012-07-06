import org.codehaus.groovy.grails.compiler.injection.test.TestForTransformation

eventTestCompileStart = {
    try {
        Class mixinClass = classLoader.loadClass("net.zorched.grails.plugins.validation.ConstraintUnitTestMixin")
        TestForTransformation.artefactTypeToTestMap.put("Constraint", mixinClass)
    } catch(Throwable e) {
        e.printStackTrace()
    }
}