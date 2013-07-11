/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion {

    protected void run(String executionCourseId, String testQuestionId, Integer testQuestionOrder, Double testQuestionValue,
            CorrectionFormula formula) throws FenixServiceException {
        TestQuestion testQuestion = FenixFramework.getDomainObject(testQuestionId);
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

    // Service Invokers migrated from Berserk

    private static final EditTestQuestion serviceInstance = new EditTestQuestion();

    @Service
    public static void runEditTestQuestion(String executionCourseId, String testQuestionId, Integer testQuestionOrder,
            Double testQuestionValue, CorrectionFormula formula) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testQuestionId, testQuestionOrder, testQuestionValue, formula);
    }

}