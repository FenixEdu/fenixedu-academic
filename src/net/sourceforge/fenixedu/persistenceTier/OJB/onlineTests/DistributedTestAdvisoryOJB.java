/*
 * Created on 19/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Susana Fernandes
 */

public class DistributedTestAdvisoryOJB extends PersistentObjectOJB implements IPersistentDistributedTestAdvisory {

    public void updateDistributedTestAdvisoryDates(IDistributedTest distributedTest, Date newExpiresDate) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List<IDistributedTestAdvisory> result = queryList(DistributedTestAdvisory.class, criteria);
        for (IDistributedTestAdvisory distributedTestAdvisory : result) {
            simpleLockWrite(distributedTestAdvisory.getAdvisory());
            distributedTestAdvisory.getAdvisory().setExpires(newExpiresDate);
        }
    }

    public void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List<IDistributedTestAdvisory> result = queryList(DistributedTestAdvisory.class, criteria);
        Calendar expiresDate = Calendar.getInstance();
        expiresDate.add(Calendar.DAY_OF_MONTH, -1);
        for (IDistributedTestAdvisory distributedTestAdvisory : result) {
            simpleLockWrite(distributedTestAdvisory.getAdvisory());
            distributedTestAdvisory.getAdvisory().setExpires(expiresDate.getTime());
            delete(distributedTestAdvisory);
        }
    }
}