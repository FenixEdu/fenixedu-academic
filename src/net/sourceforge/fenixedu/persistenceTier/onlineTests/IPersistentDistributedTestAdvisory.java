/*
 * Created on 24/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.Date;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentDistributedTestAdvisory extends IPersistentObject {

    public void updateDistributedTestAdvisoryDates(IDistributedTest distributedTest, Date newExpiresDate) throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia;
}