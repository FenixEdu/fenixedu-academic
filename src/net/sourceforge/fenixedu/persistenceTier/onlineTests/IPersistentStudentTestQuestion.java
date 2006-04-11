/*
 * Created on 27/Ago/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Susana Fernandes
 */
public interface IPersistentStudentTestQuestion extends IPersistentObject {

    public abstract List<DistributedTest> readDistributedTestsByTestQuestion(Integer questionId) throws ExcepcaoPersistencia;

    public abstract int countResponses(Integer order, String response, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract List<String> getResponses(Integer order, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countByResponse(String response, Integer order, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countResponsedOrNotResponsed(Integer order, boolean responsed, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countCorrectOrIncorrectAnswers(Integer order, Double mark, boolean correct, Integer distributedTestId)
            throws ExcepcaoPersistencia;

    public abstract int countPartiallyCorrectAnswers(Integer order, Double mark, Integer distributedTestId) throws ExcepcaoPersistencia;

    public abstract int countNumberOfStudents(DistributedTest distributedTest) throws ExcepcaoPersistencia;

    public abstract int countStudentTestByStudentAndExecutionCourse(Integer executionCourseId, Integer studentId) throws ExcepcaoPersistencia;

}