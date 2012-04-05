package net.zorched.grails.plugins.validation

import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.After
import org.codehaus.groovy.grails.commons.metaclass.MetaClassEnhancer

class ConstraintUnitTestMixin extends GrailsUnitTestMixin {
    def params = [:]
    def constraintOwningClass = null
    def constraintPropertyName = null

    def <T> T testFor(Class<T> constraintClass) {
        return this.mockConstraint(constraintClass)
    }

    def <T> T mockConstraint(Class<T> constraintClass) {
        def instance = constraintClass.newInstance()
        
        MetaClassEnhancer enhancer = new MetaClassEnhancer()
        enhancer.addApi(new MockConstraintApi(this))
        enhancer.enhance(constraintClass.metaClass)

        return instance
    }

    @After
    void resetFields() {
        this.params = [:]
        this.constraintOwningClass = null
        this.constraintPropertyName = null
    }
}
