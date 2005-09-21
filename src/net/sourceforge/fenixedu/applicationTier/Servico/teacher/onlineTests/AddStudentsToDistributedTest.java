/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class AddStudentsToDistributedTest implements IService {
    private String contextPath = new String();

    public Integer run(Integer executionCourseId, Integer distributedTestId, List infoStudentList, String contextPath) throws ExcepcaoPersistencia,
            InvalidArgumentsServiceException {
        if (infoStudentList == null || infoStudentList.size() == 0)
            return null;
        this.contextPath = contextPath.replace('\\', '/');
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDistributedTest persistentDistributedTest = persistentSuport.getIPersistentDistributedTest();
        IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId, true);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                .readStudentTestQuestionsByDistributedTest(distributedTest);
        for (IStudentTestQuestion studentTestQuestionExample : studentTestQuestionList) {

            List<IQuestion> questionList = new ArrayList<IQuestion>();
            questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

            for (int j = 0; j < infoStudentList.size(); j++) {
                IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class,
                        ((InfoStudent) infoStudentList.get(j)).getIdInternal());
                if (persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(student.getIdInternal(), distributedTestId)
                        .isEmpty()) {
                    IStudentTestQuestion studentTestQuestion = DomainFactory.makeStudentTestQuestion();
                    studentTestQuestion.setStudent(student);
                    studentTestQuestion.setDistributedTest(distributedTest);
                    studentTestQuestion.setTestQuestionOrder(studentTestQuestionExample.getTestQuestionOrder());
                    studentTestQuestion.setTestQuestionValue(studentTestQuestionExample.getTestQuestionValue());
                    studentTestQuestion.setCorrectionFormula(studentTestQuestionExample.getCorrectionFormula());
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    studentTestQuestion.setResponse(null);

                    if (questionList.size() == 0)
                        questionList.addAll(studentTestQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
                    IQuestion question = getStudentQuestion(questionList);
                    if (question == null) {
                        throw new InvalidArgumentsServiceException();
                    }
                    studentTestQuestion.setQuestion(question);
                    questionList.remove(question);

                }
            }
        }
        // create advisory for new students
        IAdvisory advisory = createTestAdvisory(distributedTest);

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

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest) {
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        IAdvisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((IExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));
        String msgBeginning;
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