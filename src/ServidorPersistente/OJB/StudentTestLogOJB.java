/*
 * Created on 10/Set/2003
 *
 */

package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestLog;
import Dominio.StudentTestLog;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestLog;

/**
 * @author Susana Fernandes
 */
public class StudentTestLogOJB extends PersistentObjectOJB implements IPersistentStudentTestLog {
    public StudentTestLogOJB() {
    }

    public List readByStudentAndDistributedTest(IStudent student, IDistributedTest distributedTest)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        return queryList(StudentTestLog.class, criteria);
    }

    public void deleteByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        List studentTestLogs = queryList(StudentTestLog.class, criteria);
        Iterator it = studentTestLogs.iterator();
        while (it.hasNext()) {
            delete((IStudentTestLog) it.next());
        }
    }

    public void delete(IStudentTestLog studentTestLog) throws ExcepcaoPersistencia {
        super.delete(studentTestLog);
    }
}