/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IOnlineTest;
import net.sourceforge.fenixedu.domain.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentOnlineTest;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Susana Fernandes
 *  
 */
public class OnlineTestOJB extends PersistentObjectOJB implements IPersistentOnlineTest {

    public OnlineTestOJB() {
    }

    public Object readByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        return queryObject(OnlineTest.class, criteria);
    }

    public void delete(IOnlineTest onlineTest) throws ExcepcaoPersistencia {
        super.delete(onlineTest);
    }
}