/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestLog;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestLog extends IPersistentObject {
    public abstract List readByStudentAndDistributedTest(IStudent student,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public abstract void delete(IStudentTestLog studentTestLog) throws ExcepcaoPersistencia;
}