package net.zorched.grails.plugins.validation

/**
 * The unit test implementation of ConstraintApi. Which just uses instance variables to implement the Api.
 * TODO: is this safe? How many people out there are using multithreaded test runners?
 */
class MockConstraintApi implements ConstraintApi {
    def owner

    MockConstraintApi(owner) {
        this.owner = owner
    }

    @Override
    Object getParams(Object instance) {
        return this.owner.params
    }

    @Override
    Object getHibernateTemplate(Object instance) {
        return null
    }

    @Override
    Class getConstraintOwningClass(Object instance) {
        return this.owner.constraintOwningClass ?: null
    }

    @Override
    String getConstraintPropertyName(Object instance) {
        return this.owner.constraintPropertyName ?: null
    }
}
