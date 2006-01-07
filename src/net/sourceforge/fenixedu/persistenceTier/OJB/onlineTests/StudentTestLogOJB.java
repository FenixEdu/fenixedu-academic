/*
 * Created on 10/Set/2003
 *
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestLog;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Susana Fernandes
 */
public class StudentTestLogOJB extends PersistentObjectOJB implements IPersistentStudentTestLog {

    public List<StudentTestLog> readByStudentAndDistributedTest(Student student, DistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        return queryList(StudentTestLog.class, criteria);
    }

    public void deleteByDistributedTest(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List<StudentTestLog> studentTestLogs = queryList(StudentTestLog.class, criteria);
        for (StudentTestLog studentTestLog : studentTestLogs) {
            delete(studentTestLog);
        }
    }
}