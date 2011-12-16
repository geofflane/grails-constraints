package net.zorched.test

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

class UniqueEgConstraint {
    
    static persistent = true
    
    def dbCall = { propertyValue, Session session -> 
        session.setFlushMode(FlushMode.MANUAL);

        try {
            boolean shouldValidate = true;
            if(propertyValue != null && DomainClassArtefactHandler.isDomainClass(propertyValue.getClass())) {
                shouldValidate = session.contains(propertyValue)
            }
            if(shouldValidate) {
                assert null != constraintOwningClass
                assert null != constraintPropertyName

                Criteria criteria = session.createCriteria(constraintOwningClass)
                        .add(Restrictions.eq(constraintPropertyName, propertyValue))
                return criteria.list()
            } else {
                return null
            }
        } finally {
            session.setFlushMode(FlushMode.AUTO)
        }
    }
    
    def validate = { propertyValue -> 
        dbCall.delegate = delegate                                  // To make things like constraintOwningClass available above
        def _v = dbCall.curry(propertyValue) as HibernateCallback   // curry the passed value and then coerce to a HibernateCallback
        def result = hibernateTemplate.executeFind(_v)
        
        return result ? false : true    // If we find a result, then non-unique
    }
}
