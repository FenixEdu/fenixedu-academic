/*
 * Created on 27/Ago/2003
 *  
 */

package ServidorPersistente;

import java.util.Collection;
import java.util.List;

import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestQuestion extends IPersistentObject {

    public abstract List readByStudentAndDistributedTest(IStudent student,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract List readByDistributedTest(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public abstract List readByStudent(IStudent student)
            throws ExcepcaoPersistencia;

    public abstract List readByQuestion(IQuestion question)
            throws ExcepcaoPersistencia;

    public abstract List readByQuestionAndDistributedTest(IQuestion question,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract IStudentTestQuestion readByQuestionAndStudentAndDistributedTest(
            IQuestion question, IStudent student,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract List readStudentsByDistributedTest(
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public List readStudentsByDistributedTests(Collection distributedTestsIds)
            throws ExcepcaoPersistencia;

    public abstract List readStudentTestQuestionsByDistributedTest(
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public Double getMaximumDistributedTestMark(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public Double getMaximumDistributedTestMark(Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public Double readStudentTestFinalMark(Integer distributedTestId,
            Integer studentId) throws ExcepcaoPersistencia;

    public List readDistributedTestsByTestQuestion(IQuestion question)
            throws ExcepcaoPersistencia;

    public abstract int countResponses(Integer order, String response,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract List getResponses(Integer order,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract int countByResponse(String response, Integer order,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public int countResponsedOrNotResponsed(Integer order, boolean responsed,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public int countCorrectOrIncorrectAnswers(Integer order, Double mark,
            boolean equal, IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public int countPartiallyCorrectAnswers(Integer order, Double mark,
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public int countNumberOfStudents(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia;

    public int countStudentTestByStudentAndExecutionCourse(
            IExecutionCourse executionCourse, IStudent student)
            throws ExcepcaoPersistencia;

    public abstract int countByQuestion(IQuestion question)
            throws ExcepcaoPersistencia;

    public abstract void deleteByDistributedTest(
            IDistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract void delete(IStudentTestQuestion studentTestQuestion)
            throws ExcepcaoPersistencia;
}