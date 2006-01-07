/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestScope;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScopeOJB extends PersistentObjectOJB implements IPersistentTestScope {

    public TestScope readByDomainObject(String className, Integer idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", className);
        criteria.addEqualTo("keyClass", idInternal);
        return (TestScope) queryObject(TestScope.class, criteria);
    }
}