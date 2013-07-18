package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InsertTestAsNewTest {

    protected Integer run(Integer executionCourseId, Integer oldTestId) throws FenixServiceException {
        Test oldTest = RootDomainObject.getInstance().readTestByOID(oldTestId);
        if (oldTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
        String title = MessageFormat.format(bundle.getString("label.testTitle.duplicated"), new Object[] { oldTest.getTitle() });
        Test test = new Test(title, oldTest.getInformation(), oldTest.getTestScope());

        List<TestQuestion> testQuestionList = oldTest.getTestQuestions();

        for (TestQuestion testQuestion : testQuestionList) {
            TestQuestion newTestQuestion = new TestQuestion();
            newTestQuestion.setQuestion(testQuestion.getQuestion());
            newTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
            newTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
            newTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
            newTestQuestion.setTest(test);
        }
        return test.getIdInternal();
    }

    // Service Invokers migrated from Berserk

    private static final InsertTestAsNewTest serviceInstance = new InsertTestAsNewTest();

    @Service
    public static Integer runInsertTestAsNewTest(Integer executionCourseId, Integer oldTestId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, oldTestId);
    }

}