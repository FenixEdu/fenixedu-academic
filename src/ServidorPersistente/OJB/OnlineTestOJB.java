/*
 * Created on 2/Fev/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDistributedTest;
import Dominio.IOnlineTest;
import Dominio.OnlineTest;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentOnlineTest;

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