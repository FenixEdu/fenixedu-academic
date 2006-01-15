/*
 * Created on 19/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTestAdvisory;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */

public class DistributedTestAdvisoryVO extends VersionedObjectsBase implements IPersistentDistributedTestAdvisory {

    public void updateDistributedTestAdvisoryDates(DistributedTest distributedTest, Date newExpiresDate) throws ExcepcaoPersistencia {
        final List<DistributedTestAdvisory> distributedTestAdvisoryList = (List<DistributedTestAdvisory>) readAll(DistributedTestAdvisory.class);
        for (DistributedTestAdvisory distributedTestAdvisory : distributedTestAdvisoryList) {
            if (distributedTestAdvisory.getKeyDistributedTest().equals(distributedTest)) {
                distributedTestAdvisory.getAdvisory().setExpires(newExpiresDate);
            }
        }
    }

    public void deleteByDistributedTest(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<DistributedTestAdvisory> distributedTestAdvisoryList = (List<DistributedTestAdvisory>) readAll(DistributedTestAdvisory.class);
        Calendar expiresDate = Calendar.getInstance();
        expiresDate.add(Calendar.DAY_OF_MONTH, -1);
        for (DistributedTestAdvisory distributedTestAdvisory : distributedTestAdvisoryList) {
            distributedTestAdvisory.getAdvisory().setExpires(expiresDate.getTime());
            deleteByOID(DistributedTestAdvisory.class, distributedTestAdvisory.getIdInternal());
        }
    }
}