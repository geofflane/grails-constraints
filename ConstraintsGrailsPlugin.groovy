/*
* Copyright 2009 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import org.codehaus.groovy.grails.validation.ConstrainedProperty
import net.zorched.grails.plugins.validation.GrailsConstraintClass
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import net.zorched.grails.plugins.validation.ConstraintArtefactHandler
import net.zorched.constraints.UsZipConstraint
import net.zorched.constraints.UsPhoneConstraint
import net.zorched.constraints.SsnConstraint
import net.zorched.constraints.ComparisonConstraint
import net.zorched.grails.plugins.validation.CustomConstraintFactory

class ConstraintsGrailsPlugin {
    // the plugin version
    def version = "0.7.0"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0.0 > *"

    // the other plugins this plugin depends on
    def dependsOn = [:]

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/*.gsp",
            "grails-app/views/**/*.gsp",
            "grails-app/domain/**/*.groovy",
            "grails-app/services/**/*.groovy",
            "grails-app/controllers/**/*.groovy",
            "grails-app/utils/net/zorched/test/**/*.groovy"
    ]

    def license = "APACHE"
    def organization = [ name: "One-Line Fix, LLC", url: "http://www.onelinefix.com" ]
    def author = "Geoff Lane"
    def authorEmail = "geoff@zorched.net"
    def title = "Custom domain constraints plugin"
    def description = '''
    This plugin allows you to create custom domain validations that are applied the same
    way as built-in domain constraints.
    '''

    def developers = [ [ name: "Geoff Lane", email: "geoff@xorched.net" ] ]
    def issueManagement = [ system: "Github", url: "https://github.com/geofflane/grails-constraints/issues" ]
    def scm = [ url: "https://github.com/geofflane/grails-constraints" ]
    def documentation = "http://grails.org/plugin/constraints"

    // loadAfter just seemed to cause problems. It needs to load early.
    // def loadAfter = ['core', 'hibernate', 'controllers']

    // We can add provided constraints in this plugin using this
    def providedArtefacts = [
            ComparisonConstraint,
            SsnConstraint,
            UsPhoneConstraint,
            UsZipConstraint
    ]

    def artefacts = [new ConstraintArtefactHandler()]

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
        application.constraintClasses.each {constraintClass ->
            configureConstraintBeans.delegate = delegate
            configureConstraintBeans(constraintClass)
        }
    }

    def doWithDynamicMethods = { applicationContext ->
        // TODO Implement registering dynamic methods to classes (optional)
        application.constraintClasses.each {constraintClass ->
            setupConstraintProperties(constraintClass)

            registerConstraint.delegate = delegate
            registerConstraint(constraintClass, false)
        }
    }


    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.

        // XXX: Not sure if this works?
        if (application.isArtefactOfType(ConstraintArtefactHandler.TYPE, event.source)) {
            setupConstraintProperties(event.source)
            application.addArtefact(ConstraintArtefactHandler.TYPE, event.source)
        }
    }

    /**
     * Register the beans with Spring so that we can inject them later.
     * Not sure if this is really needed.
     */
    def configureConstraintBeans = {GrailsConstraintClass constraintClass ->
        // XXX: Not convinced this does anything
        def fullName = constraintClass.fullName
        "${fullName}Class"(MethodInvokingFactoryBean) {
            targetObject = ref("grailsApplication", true)
            targetMethod = "getArtefact"
            arguments = [ConstraintArtefactHandler.TYPE, constraintClass.fullName]
        }

        "${fullName}"(ref("${fullName}Class")) {bean ->
            bean.factoryMethod = "newInstance"
            bean.autowire = true
        }
    }

    /**
     * Setup properties on the custom Constraints to make extra information available to them
     */
    def setupConstraintProperties = { constraintClass ->
        Object params = null
        Object hibernateTemplate = null
        Object constraintOwningClass = null
        String constraintPropertyName = null
        constraintClass.clazz.metaClass {
            setParams = {val -> params = val}
            getParams = {-> return params}
            setHibernateTemplate = {val -> hibernateTemplate = val}
            getHibernateTemplate = {-> return hibernateTemplate}
            setConstraintOwningClass = {val -> constraintOwningClass = val}
            getConstraintOwningClass = {-> return constraintOwningClass}
            setConstraintPropertyName = {val -> constraintPropertyName = val}
            getConstraintPropertyName = {-> return constraintPropertyName}
        }
    }

    /**
     * Register the Custom constraint with ConstrainedProperty. Manages creating them with a CustomConstraintFactory
     */
    def registerConstraint = { constraintClass, usingHibernate ->
        def constraintName = constraintClass.name
        log.debug "Loading constraint: ${constraintClass.name}"

        ConstrainedProperty.registerNewConstraint(constraintName, new CustomConstraintFactory(constraintClass, applicationContext))
    }
}
