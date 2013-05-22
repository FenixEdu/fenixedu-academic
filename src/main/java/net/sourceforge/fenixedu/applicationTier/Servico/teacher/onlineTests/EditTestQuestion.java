/*
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion extends FenixService {

    protected void run(Integer executionCourseId, Integer testQuestionId, Integer testQuestionOrder, Double testQuestionValue,
            CorrectionFormula formula) throws FenixServiceException {
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

    // Service Invokers migrated from Berserk

    private static final EditTestQuestion serviceInstance = new EditTestQuestion();

    @Service
    public static void runEditTestQuestion(Integer executionCourseId, Integer testQuestionId, Integer testQuestionOrder,
            Double testQuestionValue, CorrectionFormula formula) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testQuestionId, testQuestionOrder, testQuestionValue, formula);
    }

}