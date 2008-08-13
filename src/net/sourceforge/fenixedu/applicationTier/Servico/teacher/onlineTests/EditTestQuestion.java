/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion extends Service {

    public void run(Integer executionCourseId, Integer testQuestionId, Integer testQuestionOrder, Double testQuestionValue, CorrectionFormula formula)
            throws FenixServiceException {
        TestQuestion testQuestion = rootDomainObject.readTestQuestionByOID(testQuestionId);
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (testQuestionOrder == -1) {
            testQuestionOrder = testQuestion.getTest().getTestQuestions().size();
        } else if (testQuestionOrder == -2) {
            testQuestionOrder = testQuestion.getTestQuestionOrder();
        }
        if (testQuestionOrder.compareTo(testQuestion.getTestQuestionOrder()) < 0) {
            testQuestionOrder++;
        }

        testQuestion.editTestQuestion(testQuestionOrder, testQuestionValue, formula);
    }
}