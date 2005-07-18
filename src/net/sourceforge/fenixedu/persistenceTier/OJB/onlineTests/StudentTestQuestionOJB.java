/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
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

    public List<IStudentTestQuestion> readByStudentAndDistributedTest(Integer studentId, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", studentId);
        criteria.addEqualTo("distributedTest.idInternal", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<IStudentTestQuestion> readByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        queryCriteria.addOrderBy("student.number", true);
        queryCriteria.addOrderBy("testQuestionOrder", true);
        return queryList(queryCriteria);
    }

    public List<IStudentTestQuestion> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<IStudentTestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<IStudentTestQuestion> readByQuestionAndDistributedTest(Integer questionId, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(Integer questionId, Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return (IStudentTestQuestion) queryObject(StudentTestQuestion.class, criteria);
    }

    public List<IStudentTestQuestion> readByOrderAndDistributedTest(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<IStudent> readStudentsByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        List<IStudentTestQuestion> result = queryList(StudentTestQuestion.class, criteria);
        List<IStudent> studentList = new ArrayList<IStudent>();
        for (IStudentTestQuestion studentTestQuestion : result) {
            if (!studentList.contains(studentTestQuestion.getStudent()))
                studentList.add(studentTestQuestion.getStudent());
        }
        return studentList;
    }

    public List<IStudent> readStudentsByDistributedTests(Collection distributedTestsIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("keyDistributedTest", distributedTestsIds);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        queryCriteria.addGroupBy("student.number");
        List<IStudentTestQuestion> result = queryList(queryCriteria);
        lockRead(result);
        List<IStudent> studentList = new ArrayList<IStudent>();
        for (IStudentTestQuestion studentTestQuestion : result) {
            if (!studentList.contains(studentTestQuestion.getStudent()))
                studentList.add(studentTestQuestion.getStudent());
        }
        return studentList;
    }

    public List<IStudentTestQuestion> readStudentTestQuestionsByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        queryCriteria.addOrderBy("keyStudent", true);
        queryCriteria.addOrderBy("testQuestionOrder", true);
        queryCriteria.setEndAtIndex(distributedTest.getNumberOfQuestions().intValue());

        return queryList(queryCriteria);
    }

    public Double getMaximumDistributedTestMark(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("idInternal", distributedTestId);
        IDistributedTest distributedTest = (IDistributedTest) queryObject(DistributedTest.class, criteria);
        double result = 0;
        List<IStudentTestQuestion> studentTestQuestionList = readStudentTestQuestionsByDistributedTest(distributedTest);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList)
            result = result + studentTestQuestion.getTestQuestionValue().doubleValue();
        return new Double(result);
    }

    public Double readStudentTestFinalMark(Integer distributedTestId, Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("keyStudent", studentId);
        List<IStudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        if (studentTestQuestions == null || studentTestQuestions.size() == 0)
            return null;
        Double result = new Double(0);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestions) {
            result = new Double(result.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
        }
        return result;
    }

    public List<IDistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        List<IStudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        List<IDistributedTest> result = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestions) {
            IDistributedTest distributedTest = studentTestQuestion.getDistributedTest();
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
        List<IStudentTestQuestion> result = queryList(queryCriteria);

        List<String> resultList = new ArrayList<String>();
        Iterator iterator = result.iterator();
        for (IStudentTestQuestion studentTestQuestion : result) {
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

    public int countNumberOfStudents(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
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

    public int countByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public void deleteByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        List<IStudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestions) {
            delete(studentTestQuestion);
        }
    }
}