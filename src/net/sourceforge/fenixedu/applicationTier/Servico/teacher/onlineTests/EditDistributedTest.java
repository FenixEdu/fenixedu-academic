/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest implements IService {

    private String contextPath = new String();

    public Integer run(Integer executionCourseId, Integer distributedTestId, String testInformation, Calendar beginDate, Calendar beginHour,
            Calendar endDate, Calendar endHour, TestType testType, CorrectionAvailability correctionAvailability, Boolean imsFeedback,
            String contextPath) throws FenixServiceException {
        this.contextPath = contextPath.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IPersistentDistributedTest persistentDistributedTest = persistentSuport.getIPersistentDistributedTest();
            IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId, true);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            persistentDistributedTest.simpleLockWrite(distributedTest);
            distributedTest.setTestInformation(testInformation);
            boolean change2EvaluationType = false, change2OtherType = false;
            if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)) && (!testType.equals(new TestType(TestType.EVALUATION))))
                change2OtherType = true;
            else if ((!distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) && testType.equals(new TestType(TestType.EVALUATION)))
                change2EvaluationType = true;
            distributedTest.setTestType(testType);
            distributedTest.setCorrectionAvailability(correctionAvailability);
            distributedTest.setImsFeedback(imsFeedback);
            // create advisory for students that already have the test to
            // anounce the date changes

            CalendarDateComparator dateComparator = new CalendarDateComparator();
            CalendarHourComparator hourComparator = new CalendarHourComparator();
            IAdvisory advisory = null;
            if (dateComparator.compare(distributedTest.getBeginDate(), beginDate) != 0
                    || hourComparator.compare(distributedTest.getBeginHour(), beginHour) != 0
                    || dateComparator.compare(distributedTest.getEndDate(), endDate) != 0
                    || hourComparator.compare(distributedTest.getEndHour(), endHour) != 0) {

                distributedTest.setBeginDate(beginDate);
                distributedTest.setBeginHour(beginHour);
                distributedTest.setEndDate(endDate);
                distributedTest.setEndHour(endHour);
                advisory = createTestAdvisory(distributedTest);
                persistentSuport.getIPersistentAdvisory().simpleLockWrite(advisory);
                persistentSuport.getIPersistentDistributedTestAdvisory().updateDistributedTestAdvisoryDates(distributedTest, endDate.getTime());
                persistentSuport.getIPersistentDistributedTestAdvisory().simpleLockWrite(createDistributedTestAdvisory(distributedTest, advisory));
            }

            if (change2OtherType) {
                // Change evaluation test to study/inquiry test
                // delete evaluation and marks
                IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTest);
                persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
                persistentSuport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
            } else if (change2EvaluationType) {
                // Change to evaluation test
                // Create evaluation (onlineTest) and marks
                IOnlineTest onlineTest = new OnlineTest();
                persistentSuport.getIPersistentEvaluation().simpleLockWrite(onlineTest);
                onlineTest.setDistributedTest(distributedTest);
                onlineTest.addAssociatedExecutionCourses(executionCourse);
                List<IStudent> studentList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                        distributedTest.getIdInternal());
                for (IStudent student : studentList) {
                    List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                            .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());
                    double studentMark = 0;
                    for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                        studentMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                    }
                    IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);
                    if (attend != null) {
                        IMark mark = new Mark();
                        persistentSuport.getIPersistentMark().simpleLockWrite(mark);
                        mark.setAttend(attend);
                        mark.setEvaluation(onlineTest);
                        mark.setMark(new java.text.DecimalFormat("#0.##").format(studentMark));
                    }
                }
            }
            if (advisory == null)
                return null;
            return advisory.getIdInternal();
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
    }

    private String getDateFormatted(Calendar date) {
        String result = new String();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    private String getHourFormatted(Calendar hour) {
        String result = new String();
        result += hour.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (hour.get(Calendar.MINUTE) < 10)
            result += "0";
        result += hour.get(Calendar.MINUTE);
        return result;
    }

    private IAdvisory createTestAdvisory(IDistributedTest distributedTest) {
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");

        IAdvisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((IExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));
        advisory.setOnlyShowOnce(new Boolean(false));
        advisory.setSubject(MessageFormat.format(bundle.getString("message.distributedTest.subjectChangeDates"), new Object[] { distributedTest
                .getTitle() }));

        Object[] args = { this.contextPath, distributedTest.getIdInternal(), getHourFormatted(distributedTest.getBeginHour()),
                getDateFormatted(distributedTest.getBeginDate()), getHourFormatted(distributedTest.getEndHour()),
                getDateFormatted(distributedTest.getEndDate()) };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeInquiryDates"), args));
        } else {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeTestDates"), args));
        }

        return advisory;
    }

    private IDistributedTestAdvisory createDistributedTestAdvisory(IDistributedTest distributedTest, IAdvisory advisory) {
        IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return distributedTestAdvisory;
    }
}
