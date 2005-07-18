/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class StudentTestLogVO extends VersionedObjectsBase implements IPersistentStudentTestLog {

    public List<IStudentTestLog> readByStudentAndDistributedTest(IStudent student, IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<IStudentTestLog> studentTestLogList = (List<IStudentTestLog>) readAll(StudentTestLog.class);
        List<IStudentTestLog> result = new ArrayList<IStudentTestLog>();
        for (IStudentTestLog studentTestLog : studentTestLogList) {
            if (studentTestLog.getKeyDistributedTest().equals(distributedTest.getIdInternal())
                    && studentTestLog.getStudent().equals(student.getIdInternal())) {
                result.add(studentTestLog);
            }
        }
        return result;
    }

    public void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<IStudentTestLog> studentTestLogList = (List<IStudentTestLog>) readAll(StudentTestLog.class);
        for (IStudentTestLog studentTestLog : studentTestLogList) {
            if (studentTestLog.getKeyDistributedTest().equals(distributedTest.getIdInternal())) {
                deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
            }
        }
    }
}