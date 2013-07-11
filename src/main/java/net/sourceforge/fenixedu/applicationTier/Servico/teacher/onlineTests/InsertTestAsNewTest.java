package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InsertTestAsNewTest {

    protected String run(String executionCourseId, String oldTestId) throws FenixServiceException {
        Test oldTest = FenixFramework.getDomainObject(oldTestId);
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
        return test.getExternalId();
    }

    // Service Invokers migrated from Berserk

    private static final InsertTestAsNewTest serviceInstance = new InsertTestAsNewTest();

    @Atomic
    public static String runInsertTestAsNewTest(String executionCourseId, String oldTestId) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, oldTestId);
    }

}