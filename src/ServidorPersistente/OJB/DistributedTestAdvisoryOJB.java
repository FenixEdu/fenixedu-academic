/*
 * Created on 19/Ago/2003
 *  
 */

package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DistributedTestAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTestAdvisory;

/**
 * @author Susana Fernandes
 */

public class DistributedTestAdvisoryOJB extends PersistentObjectOJB implements
        IPersistentDistributedTestAdvisory {

    public DistributedTestAdvisoryOJB() {
    }

    public void updateDistributedTestAdvisoryDates(IDistributedTest distributedTest, Date newExpiresDate)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List result = queryList(DistributedTestAdvisory.class, criteria);
        Iterator it = result.iterator();
        while (it.hasNext()) {
            IDistributedTestAdvisory distributedTestAdvisory = (IDistributedTestAdvisory) it.next();
            simpleLockWrite(distributedTestAdvisory.getAdvisory());
            distributedTestAdvisory.getAdvisory().setExpires(newExpiresDate);
        }
    }

    public void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List result = queryList(DistributedTestAdvisory.class, criteria);
        Iterator it = result.iterator();
        Calendar expiresDate = Calendar.getInstance();
        expiresDate.add(Calendar.DAY_OF_MONTH, -1);
        while (it.hasNext()) {
            IDistributedTestAdvisory distributedTestAdvisory = (IDistributedTestAdvisory) it.next();
            simpleLockWrite(distributedTestAdvisory.getAdvisory());
            distributedTestAdvisory.getAdvisory().setExpires(expiresDate.getTime());
            delete(distributedTestAdvisory);
        }
    }

    public void delete(IDistributedTestAdvisory distributedTestAdvisory) throws ExcepcaoPersistencia {
        super.delete(distributedTestAdvisory);
    }
}