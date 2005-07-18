/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestLog extends IPersistentObject {
    public abstract List<IStudentTestLog> readByStudentAndDistributedTest(IStudent student, IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia;
}