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
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author Susana Fernandes
 */
public class StudentTestQuestionVO extends VersionedObjectsBase implements IPersistentStudentTestQuestion {

    public List<StudentTestQuestion> readByStudentAndDistributedTest(Integer studentId, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId) && studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<StudentTestQuestion> readByDistributedTest(Integer distributedTestId) {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                result.add(studentTestQuestion);
            }
        }
        Collections.sort(result, new BeanComparator("student.number"));
        Collections.sort(result, new BeanComparator("testQuestionOrder"));
        return result;
    }

    public List<StudentTestQuestion> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<StudentTestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<StudentTestQuestion> readByQuestionAndDistributedTest(Integer questionId, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public StudentTestQuestion readByQuestionAndStudentAndDistributedTest(Integer questionId, Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyStudent().equals(studentId)
                    && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                return studentTestQuestion;
            }
        }
        return null;
    }

    public List<StudentTestQuestion> readByOrderAndDistributedTest(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<StudentTestQuestion> result = new ArrayList<StudentTestQuestion>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getTestQuestionOrder().equals(order)) {
                result.add(studentTestQuestion);
            }
        }
        return result;
    }

    public List<Student> readStudentsByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<Student> result = new ArrayList<Student>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getTestQuestionOrder().equals(1)) {
                result.add(studentTestQuestion.getStudent());
            }
        }
        return result;
    }

    public List<Student> readStudentsByDistributedTests(Collection distributedTestsIds) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<Student> result = new ArrayList<Student>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getTestQuestionOrder().equals(1) && distributedTestsIds.contains(studentTestQuestion.getKeyDistributedTest())
                    && !result.contains(studentTestQuestion.getStudent())) {
                result.add(studentTestQuestion.getStudent());
            }

        }
        Collections.sort(result, new BeanComparator("student.number"));
        return result;
    }

    public List<StudentTestQuestion> readStudentTestQuestionsByDistributedTest(DistributedTest distributedTest) {
        final List<StudentTestQuestion> studentTestQuestionList = readByDistributedTest(distributedTest.getIdInternal());
        return studentTestQuestionList.subList(0, distributedTest.getNumberOfQuestions());
    }

    public Double getMaximumDistributedTestMark(Integer distributedTestId) throws ExcepcaoPersistencia {
        final DistributedTest distributedTest = (DistributedTest) readByOID(DistributedTest.class, distributedTestId);
        double result = 0;
        List<StudentTestQuestion> studentTestQuestionList = readStudentTestQuestionsByDistributedTest(distributedTest);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList)
            result = result + studentTestQuestion.getTestQuestionValue().doubleValue();
        return new Double(result);
    }

    public Double readStudentTestFinalMark(Integer distributedTestId, Integer studentId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = readByStudentAndDistributedTest(studentId, distributedTestId);
        if (studentTestQuestionList == null || studentTestQuestionList.size() == 0) {
            return null;
        }
        Double result = new Double(0);

        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            result = new Double(result.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
        }
        return result;
    }

    public List<DistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = readByQuestion(questionId);
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            DistributedTest distributedTest = studentTestQuestion.getDistributedTest();
            if (!result.contains(distributedTest))
                result.add(distributedTest);
        }
        return result;
    }

    public int countResponses(Integer order, String response, Integer distributedTestId) throws ExcepcaoPersistencia {
        int result = 0;
        List<StudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null && studentTestQuestion.getResponse().equals(response)) {
                result++;
            }
        }
        return result;
    }

    public List<String> getResponses(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<String> result = new ArrayList<String>();
        List<StudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                result.add(studentTestQuestion.getResponse());
            }
        }
        return result;
    }

    public int countByResponse(String response, Integer order, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int result = 0;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null && studentTestQuestion.getResponse().equals(response)) {
                result++;
            }
        }
        return result;
    }

    public int countResponsedOrNotResponsed(Integer order, boolean responsed, Integer distributedTestId) throws ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = null;
        int responsedNumber = 0;
        int notResponsedNumber = 0;
        if (order != null) {
            studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        } else {
            studentTestQuestionList = readByDistributedTest(distributedTestId);
        }
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
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
        List<StudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int correctNumber = 0;
        int incorrectNumber = 0;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
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
        List<StudentTestQuestion> studentTestQuestionList = readByOrderAndDistributedTest(order, distributedTestId);
        int partiallyCorrect = 0;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getResponse() != null) {
                if (studentTestQuestion.getTestQuestionMark().compareTo(mark) < 0 && studentTestQuestion.getTestQuestionMark() > 0) {
                    partiallyCorrect++;
                }
            }
        }

        return partiallyCorrect;
    }

    public int countNumberOfStudents(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        return readByOrderAndDistributedTest(1, distributedTest.getIdInternal()).size();
    }

    public int countStudentTestByStudentAndExecutionCourse(Integer executionCourseId, Integer studentId) throws ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = readByStudent(studentId);
        int result = 0;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
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
        List<StudentTestQuestion> studentTestQuestionList = readByDistributedTest(distributedTestId);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            deleteByOID(StudentTestQuestion.class, studentTestQuestion.getIdInternal());
        }
    }
}