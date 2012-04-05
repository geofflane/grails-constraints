package net.zorched.grails.plugins.validation

interface ConstraintApi {
    Object getParams(Object instance)
    Object getHibernateTemplate(Object instance)
    Class getConstraintOwningClass(Object instance)
    String getConstraintPropertyName(Object instance)
}
