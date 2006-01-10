/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
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

    public List<StudentTestQuestion> readByStudentAndDistributedTest(Integer studentId, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("student.idInternal", studentId);
        criteria.addEqualTo("distributedTest.idInternal", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<StudentTestQuestion> readByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        queryCriteria.addOrderBy("student.number", true);
        queryCriteria.addOrderBy("testQuestionOrder", true);
        return queryList(queryCriteria);
    }

    public List<StudentTestQuestion> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", studentId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<StudentTestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<StudentTestQuestion> readByQuestionAndDistributedTest(Integer questionId, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public StudentTestQuestion readByQuestionAndStudentAndDistributedTest(Integer questionId, Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        criteria.addEqualTo("keyStudent", studentId);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return (StudentTestQuestion) queryObject(StudentTestQuestion.class, criteria);
    }

    public List<StudentTestQuestion> readByOrderAndDistributedTest(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("testQuestionOrder", order);
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        return queryList(StudentTestQuestion.class, criteria);
    }

    public List<Student> readStudentsByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        List<StudentTestQuestion> result = queryList(StudentTestQuestion.class, criteria);
        List<Student> studentList = new ArrayList<Student>();
        for (StudentTestQuestion studentTestQuestion : result) {
            if (!studentList.contains(studentTestQuestion.getStudent()))
                studentList.add(studentTestQuestion.getStudent());
        }
        return studentList;
    }

    public List<Student> readStudentsByDistributedTests(Collection distributedTestsIds) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("keyDistributedTest", distributedTestsIds);
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        queryCriteria.addGroupBy("student.number");
        List<StudentTestQuestion> result = queryList(queryCriteria);
        lockRead(result);
        List<Student> studentList = new ArrayList<Student>();
        for (StudentTestQuestion studentTestQuestion : result) {
            if (!studentList.contains(studentTestQuestion.getStudent()))
                studentList.add(studentTestQuestion.getStudent());
        }
        return studentList;
    }

    public List<StudentTestQuestion> readStudentTestQuestionsByDistributedTest(DistributedTest distributedTest) throws ExcepcaoPersistencia {
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
        DistributedTest distributedTest = (DistributedTest) queryObject(DistributedTest.class, criteria);
        double result = 0;
        List<StudentTestQuestion> studentTestQuestionList = readStudentTestQuestionsByDistributedTest(distributedTest);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList)
            result = result + studentTestQuestion.getTestQuestionValue().doubleValue();
        return new Double(result);
    }

    public Double readStudentTestFinalMark(Integer distributedTestId, Integer studentId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("keyStudent", studentId);
        List<StudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        if (studentTestQuestions == null || studentTestQuestions.size() == 0)
            return null;
        Double result = new Double(0);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            result = new Double(result.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
        }
        return result;
    }

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

    public int countByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        return count(StudentTestQuestion.class, criteria, false);
    }

    public void deleteByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        List<StudentTestQuestion> studentTestQuestions = queryList(StudentTestQuestion.class, criteria);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            delete(studentTestQuestion);
        }
    }
}