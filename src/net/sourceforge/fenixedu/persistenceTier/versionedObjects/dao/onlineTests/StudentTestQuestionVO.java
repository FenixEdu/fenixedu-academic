/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionVO extends VersionedObjectsBase implements IPersistentStudentTestQuestion {

    public List<IStudentTestQuestion> readByStudentAndDistributedTest(Integer studentId, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId) && studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<IStudentTestQuestion> readByDistributedTest(Integer distributedTestId) {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                result.add(studentTestQuestion);
            }
        }
        Collections.sort(result, new BeanComparator("student.number"));
        Collections.sort(result, new BeanComparator("testQuestionOrder"));
        return result;
    }

    public List<IStudentTestQuestion> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<IStudentTestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<IStudentTestQuestion> readByQuestionAndDistributedTest(Integer questionId, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(Integer questionId, Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyStudent().equals(studentId)
                    && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                return studentTestQuestion;
            }
        }
        return null;
    }

    public List<IStudentTestQuestion> readByOrderAndDistributedTest(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudentTestQuestion> result = new ArrayList<IStudentTestQuestion>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getTestQuestionOrder().equals(order)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<IStudent> readStudentsByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudent> result = new ArrayList<IStudent>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getTestQuestionOrder().equals(1)) {
                result.add(studentTestQuestion.getStudent());
            }
        }
        return result;
    }

    public List<IStudent> readStudentsByDistributedTests(Collection distributedTestsIds) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IStudent> result = new ArrayList<IStudent>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getTestQuestionOrder().equals(1) && distributedTestsIds.contains(studentTestQuestion.getKeyDistributedTest())
                    && !result.contains(studentTestQuestion.getStudent())) {
                result.add(studentTestQuestion.getStudent());
            }

        }
        Collections.sort(result, new BeanComparator("student.number"));
        return result;
    }

    public List<IStudentTestQuestion> readStudentTestQuestionsByDistributedTest(IDistributedTest distributedTest) {
        final List<IStudentTestQuestion> studentTestQuestionList = readByDistributedTest(distributedTest.getIdInternal());
        return studentTestQuestionList.subList(0, distributedTest.getNumberOfQuestions());
    }

    public Double getMaximumDistributedTestMark(Integer distributedTestId) throws ExcepcaoPersistencia {
        final IDistributedTest distributedTest = (IDistributedTest) readByOID(DistributedTest.class, distributedTestId);
        double result = 0;
        List<IStudentTestQuestion> studentTestQuestionList = readStudentTestQuestionsByDistributedTest(distributedTest);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList)
            result = result + studentTestQuestion.getTestQuestionValue().doubleValue();
        return new Double(result);
    }

    public Double readStudentTestFinalMark(Integer distributedTestId, Integer studentId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = readByStudentAndDistributedTest(studentId, distributedTestId);
        if (studentTestQuestionList == null || studentTestQuestionList.size() == 0) {
            return null;
        }
        Double result = new Double(0);

        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            result = new Double(result.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
        }
        return result;
    }

    public List<IDistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = readByQuestion(questionId);
        List<IDistributedTest> result = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            IDistributedTest distributedTest = studentTestQuestion.getDistributedTest();
            if (!result.contains(distributedTest))
                result.add(distributedTest);
        }
        return result;
    }

    public int countResponses(Integer order, String response, Integer distributedTestId) throws ExcepcaoPersistencia {
        int result = 0;
        List<IStudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null && studentTestQuestion.getResponse().equals(response)) {
                result++;
            }
        }
        return result;
    }

    public List<String> getResponses(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<String> result = new ArrayList<String>();
        List<IStudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                result.add(studentTestQuestion.getResponse());
            }
        }
        return result;
    }

    public int countByResponse(String response, Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int result = 0;
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null && studentTestQuestion.getResponse().equals(response)) {
                result++;
            }
        }
        return result;
    }

    public int countResponsedOrNotResponsed(Integer order, boolean responsed, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = null;
        int responsedNumber = 0;
        int notResponsedNumber = 0;
        if (order != null) {
            studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        } else {
            studentTestQuestionList = readByDistributedTest(distributedTestId);
        }
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                responsedNumber++;
            } else {
                notResponsedNumber++;
            }
        }
        if (responsed) {
            return responsedNumber;
        }
        return notResponsedNumber;
    }

    public int countCorrectOrIncorrectAnswers(Integer order, Double mark, boolean correct, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int correctNumber = 0;
        int incorrectNumber = 0;
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                if (studentTestQuestion.getTestQuestionMark().compareTo(mark) >= 0) {
                    correctNumber++;
                } else {
                    incorrectNumber++;
                }
            }
        }
        if (correct) {
            return correctNumber;
        }
        return incorrectNumber;
    }

    public int countPartiallyCorrectAnswers(Integer order, Double mark, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int partiallyCorrect = 0;
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                if (studentTestQuestion.getTestQuestionMark().compareTo(mark) < 0 && studentTestQuestion.getTestQuestionMark() > 0) {
                    partiallyCorrect++;
                }
            }
        }

        return partiallyCorrect;
    }

    public int countNumberOfStudents(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        return readByOrderAndDistributedTest(1, distributedTest.getIdInternal()).size();
    }

    public int countStudentTestByStudentAndExecutionCourse(Integer executionCourseId, Integer studentId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = readByStudent(studentId);
        int result = 0;
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getTestQuestionOrder().equals(1)
                    && studentTestQuestion.getDistributedTest().getTestScope().getClassName().equals(ExecutionCourse.class.getName())
                    && studentTestQuestion.getDistributedTest().getTestScope().getKeyClass().equals(executionCourseId)) {
                result++;
            }
        }
        return result;
    }

    public int countByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        return readByQuestion(questionId).size();
    }

    public void deleteByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = readByDistributedTest(distributedTestId);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            deleteByOID(StudentTestQuestion.class, studentTestQuestion.getIdInternal());
        }
    }
}