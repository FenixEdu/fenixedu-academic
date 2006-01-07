/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestLog extends IPersistentObject {
    public abstract List<StudentTestLog> readByStudentAndDistributedTest(Student student, DistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(DistributedTest distributedTest) throws ExcepcaoPersistencia;
}