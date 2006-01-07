/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
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

    public List<DistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", className);
        criteria.addEqualTo("keyClass", idInternal);
        TestScope scope = (TestScope) queryObject(TestScope.class, criteria);
        if (scope == null)
            return new ArrayList<DistributedTest>();
        criteria = new Criteria();
        criteria.addEqualTo("keyTestScope", scope.getIdInternal());
        return queryList(DistributedTest.class, criteria);
    }

    public List<DistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List<StudentTestQuestion> result = queryList(queryCriteria);
        List<DistributedTest> distributedTestList = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : result) {
            distributedTestList.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTestList;
    }

    public List<DistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("distributedTest.testScope.className", ExecutionCourse.class.getName());
        criteria.addEqualTo("distributedTest.testScope.keyClass", executionCourseId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List<StudentTestQuestion> result = queryList(queryCriteria);
        List<DistributedTest> distributedTestList = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : result) {
            distributedTestList.add(studentTestQuestion.getDistributedTest());
        }
        return distributedTestList;
    }

    public List<DistributedTest> readAll() throws ExcepcaoPersistencia {
        return queryList(DistributedTest.class, null);
    }
}