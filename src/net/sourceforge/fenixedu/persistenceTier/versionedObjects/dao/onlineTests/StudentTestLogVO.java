/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class StudentTestLogVO extends VersionedObjectsBase implements IPersistentStudentTestLog {

    public List<StudentTestLog> readByStudentAndDistributedTest(Student student, DistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<StudentTestLog> studentTestLogList = (List<StudentTestLog>) readAll(StudentTestLog.class);
        List<StudentTestLog> result = new ArrayList<StudentTestLog>();
        for (StudentTestLog studentTestLog : studentTestLogList) {
            if (studentTestLog.getKeyDistributedTest().equals(distributedTest.getIdInternal())
                    && studentTestLog.getStudent().equals(student.getIdInternal())) {
                result.add(studentTestLog);
            }
        }
        return result;
    }

    public void deleteByDistributedTest(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<StudentTestLog> studentTestLogList = (List<StudentTestLog>) readAll(StudentTestLog.class);
        for (StudentTestLog studentTestLog : studentTestLogList) {
            if (studentTestLog.getKeyDistributedTest().equals(distributedTest.getIdInternal())) {
                deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
            }
        }
    }
}