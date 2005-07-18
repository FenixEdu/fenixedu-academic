/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestQuestion extends IPersistentObject {

    public abstract List<IStudentTestQuestion> readByStudentAndDistributedTest(Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readByStudent(Integer studentId) throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readByQuestionAndDistributedTest(Integer questionId, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(Integer questionId, Integer studentId, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readByOrderAndDistributedTest(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract List<IStudent> readStudentsByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract List<IStudent> readStudentsByDistributedTests(Collection distributedTestsIds) throws ExcepcaoPersistencia;

    public abstract List<IStudentTestQuestion> readStudentTestQuestionsByDistributedTest(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public abstract Double getMaximumDistributedTestMark(Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract Double readStudentTestFinalMark(Integer distributedTestId, Integer studentId) throws ExcepcaoPersistencia;

    public abstract List<IDistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia;

    public abstract int countResponses(Integer order, String response, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract List<String> getResponses(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countByResponse(String response, Integer order, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countResponsedOrNotResponsed(Integer order, boolean responsed, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countCorrectOrIncorrectAnswers(Integer order, Double mark, boolean correct, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract int countPartiallyCorrectAnswers(Integer order, Double mark, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countNumberOfStudents(IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract int countStudentTestByStudentAndExecutionCourse(Integer executionCourseId, Integer studentId) throws ExcepcaoPersistencia;

    public abstract int countByQuestion(Integer questionId) throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(Integer distributedTestId) throws ExcepcaoPersistencia;
}