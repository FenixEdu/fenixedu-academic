/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionOJB extends PersistentObjectOJB implements IPersistentStudentTestQuestion {

    public List<DistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        List<StudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            DistributedTest distributedTest = studentTestQuestion.getDistributedTest();
            if (!result.contains(distributedTest))
                result.add(distributedTest);
        }
        return result;
    }

    public int countResponses(Integer order, String response, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addLike("response", response);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public List<String> getResponses(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        criteria.addNotNull("response");
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, true);
        queryCriteria.addGroupBy("response");
        List<StudentTestQuestion> result = queryList(queryCriteria);

        List<String> resultList = new ArrayList<String>();
        for (StudentTestQuestion studentTestQuestion : result) {
            resultList.add(studentTestQuestion.getResponse());
        }
        return resultList;
    }

    public int countByResponse(String response, Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addEqualTo("response", response);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return count(StudentTestQuestion.class, criteria, false);

    }

    public int countResponsedOrNotResponsed(Integer order, boolean responsed, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        if (order != null)
            criteria.addEqualTo("testQuestionOrder", order);
        if (responsed)
            criteria.addNotNull("response");
        else
            criteria.addIsNull("response");
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public int countCorrectOrIncorrectAnswers(Integer order, Double mark, boolean correct, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addNotNull("response");
        if (correct)
            criteria.addGreaterOrEqualThan("testQuestionMark", mark);
        else
            criteria.addLessOrEqualThan("testQuestionMark", new Double(0));

        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public int countPartiallyCorrectAnswers(Integer order, Double mark, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addNotNull("response");
        criteria.addLessThan("testQuestionMark", mark);
        criteria.addGreaterThan("testQuestionMark", new Integer(0));
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public int countNumberOfStudents(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        return count(StudentTestQuestion.class, criteria, false) / distributedTest.getNumberOfQuestions().intValue();
    }

    public int countStudentTestByStudentAndExecutionCourse(Integer executionCourseId, Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("distributedTest.testScope.className", ExecutionCourse.class.getName());
        criteria.addEqualTo("distributedTest.testScope.keyClass", executionCourseId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        return count(StudentTestQuestion.class, criteria, false);
    }

}