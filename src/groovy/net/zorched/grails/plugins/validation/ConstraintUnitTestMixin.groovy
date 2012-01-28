package net.zorched.grails.plugins.validation

import grails.test.mixin.support.GrailsUnitTestMixin
import org.junit.After

class ConstraintUnitTestMixin extends GrailsUnitTestMixin {
    def params = [:]
    def veto = null

    def <T> T testFor(Class<T> constraintClass) {
        return this.mockConstraint(constraintClass)
    }

    def <T> T mockConstraint(Class<T> constraintClass) {
        def instance = constraintClass.newInstance()

        instance.metaClass.getParams = {-> params }
        instance.metaClass.setParams = {newParams -> params = newParams }
        instance.metaClass.getVeto = {-> veto }
        instance.metaClass.setVeto = {newVeto -> veto = newVeto }

        return instance
    }

    @After
    void resetFields() {
        params = [:]
        veto = null
    }
}
