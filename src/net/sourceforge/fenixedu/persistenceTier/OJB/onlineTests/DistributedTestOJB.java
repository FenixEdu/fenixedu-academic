/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Susana Fernandes
 */
public class DistributedTestOJB extends PersistentObjectOJB implements IPersistentDistributedTest {

    public List<IDistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", className);
        criteria.addEqualTo("keyClass", idInternal);
        ITestScope scope = (ITestScope) queryObject(TestScope.class, criteria);
        if (scope == null)
            return new ArrayList<IDistributedTest>();
        criteria = new Criteria();
        criteria.addEqualTo("keyTestScope", scope.getIdInternal());
        return queryList(DistributedTest.class, criteria);
    }

    public List<IDistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List<IStudentTestQuestion> result = queryList(queryCriteria);
        List<IDistributedTest> distributedTestList = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : result) {
            distributedTestList.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTestList;
    }

    public List<IDistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("distributedTest.testScope.className", ExecutionCourse.class.getName());
        criteria.addEqualTo("distributedTest.testScope.keyClass", executionCourseId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List<IStudentTestQuestion> result = queryList(queryCriteria);
        List<IDistributedTest> distributedTestList = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : result) {
            distributedTestList.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTestList;
    }

    public List<IDistributedTest> readAll() throws ExcepcaoPersistencia {
        return queryList(DistributedTest.class, null);
    }
}