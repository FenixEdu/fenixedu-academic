/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITestScope;
import net.sourceforge.fenixedu.domain.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestScope;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class TestScopeOJB extends PersistentObjectOJB implements IPersistentTestScope {

    public TestScopeOJB() {
    }

    public ITestScope readByDomainObject(IDomainObject object) throws ExcepcaoPersistencia {
        //		 Force object materialization to obtain correct class name for query.
        IDomainObject materializedObject = materialize(object);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", materializedObject.getClass().getName());
        criteria.addEqualTo("keyClass", object.getIdInternal());

        return (ITestScope) queryObject(TestScope.class, criteria);
    }
}