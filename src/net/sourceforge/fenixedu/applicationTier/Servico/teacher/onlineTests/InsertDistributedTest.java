/*
 * Created on 19/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class InsertDistributedTest implements IService {

    private String contextPath = new String();

    public Integer run(Integer executionCourseId, Integer testId, String testInformation, Calendar beginDate, Calendar beginHour, Calendar endDate,
            Calendar endHour, TestType testType, CorrectionAvailability correctionAvaiability, Boolean imsFeedback,
            List<InfoStudent> infoStudentList, String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
        this.contextPath = contextPath.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseId);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        IDistributedTest distributedTest = DomainFactory.makeDistributedTest();

        ITest test = (ITest) persistentSuport.getIPersistentTest().readByOID(Test.class, testId);
        if (test == null)
            throw new InvalidArgumentsServiceException();

        distributedTest.setTitle(test.getTitle());
        distributedTest.setTestInformation(testInformation);
        distributedTest.setBeginDate(beginDate);
        distributedTest.setBeginHour(beginHour);
        distributedTest.setEndDate(endDate);
        distributedTest.setEndHour(endHour);
        distributedTest.setTestType(testType);
        distributedTest.setCorrectionAvailability(correctionAvaiability);
        distributedTest.setImsFeedback(imsFeedback);
        distributedTest.setNumberOfQuestions(test.getTestQuestionsCount());

        ITestScope testScope = persistentSuport.getIPersistentTestScope().readByDomainObject(ExecutionCourse.class.getName(), executionCourseId);

        if (testScope == null) {
            testScope = DomainFactory.makeTestScope(persistentExecutionCourse.materialize(executionCourse));
        }
        distributedTest.setTestScope(testScope);

        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();

        List<ITestQuestion> testQuestionList = persistentTestQuestion.readByTest(testId);

        for (ITestQuestion testQuestion : testQuestionList) {
            List<IQuestion> questionList = new ArrayList<IQuestion>();
            questionList.addAll(testQuestion.getQuestion().getMetadata().getVisibleQuestions());

            for (InfoStudent infoStudent : infoStudentList) {
                IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, infoStudent.getIdInternal());
                IStudentTestQuestion studentTestQuestion = DomainFactory.makeStudentTestQuestion();
                studentTestQuestion.setStudent(student);
                studentTestQuestion.setDistributedTest(distributedTest);
                studentTestQuestion.setTestQuestionOrder(testQuestion.getTestQuestionOrder());
                studentTestQuestion.setTestQuestionValue(testQuestion.getTestQuestionValue());
                studentTestQuestion.setCorrectionFormula(testQuestion.getCorrectionFormula());
                studentTestQuestion.setTestQuestionMark(new Double(0));
                studentTestQuestion.setResponse(null);

                if (questionList.size() == 0)
                    questionList.addAll(testQuestion.getQuestion().getMetadata().getVisibleQuestions());
                IQuestion question = getStudentQuestion(questionList);
                if (question == null) {
                    throw new InvalidArgumentsServiceException();
                }
                studentTestQuestion.setQuestion(question);
                questionList.remove(question);
            }
        }
        // Create Evaluation - OnlineTest and Marks
        if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) {
            IOnlineTest onlineTest = DomainFactory.makeOnlineTest();
            onlineTest.addAssociatedExecutionCourses(executionCourse);
            onlineTest.setDistributedTest(distributedTest);
        }

        // Create Advisory
        IAdvisory advisory = getAdvisory(distributedTest, executionCourse.getNome());

        // Create DistributedTestAdvisory
        IDistributedTestAdvisory distributedTestAdvisory = DomainFactory.makeDistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);

        return advisory.getIdInternal();

    }

    private IQuestion getStudentQuestion(List<IQuestion> questions) {
        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    private IAdvisory getAdvisory(IDistributedTest distributedTest, String sender) {
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        IAdvisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { sender }));
        advisory.setSubject(distributedTest.getTitle());
        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(), "HH:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(), "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "HH:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(), "dd/MM/yyyy");
        Object[] args = { this.contextPath, distributedTest.getIdInternal().toString(), beginHour, beginDate, endHour, endDate };

        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.distributeInquiryMessage"), args));
        } else {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.distributeTestMessage"), args));
        }
        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }
}
