/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentOnlineTest;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class OnlineTestOJB extends PersistentObjectOJB implements IPersistentOnlineTest {

    public Object readByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        return queryObject(OnlineTest.class, criteria);
    }
}