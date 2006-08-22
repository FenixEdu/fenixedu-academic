package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

public class EditDistributedTest extends Service {

    private String contextPath = new String();

    public Integer run(Integer executionCourseId, final Integer distributedTestId,
            String testInformation, Calendar beginDate, Calendar beginHour, Calendar endDate,
            Calendar endHour, TestType testType, CorrectionAvailability correctionAvailability,
            Boolean imsFeedback, String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
        this.contextPath = contextPath.replace('\\', '/');

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        distributedTest.setTestInformation(testInformation);
        boolean change2EvaluationType = false, change2OtherType = false;
        if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))
                && (!testType.equals(new TestType(TestType.EVALUATION))))
            change2OtherType = true;
        else if ((!distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)))
                && testType.equals(new TestType(TestType.EVALUATION)))
            change2EvaluationType = true;
        distributedTest.setTestType(testType);
        distributedTest.setCorrectionAvailability(correctionAvailability);
        distributedTest.setImsFeedback(imsFeedback);
        // create advisory for students that already have the test to anounce
        // the date changes

        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        Advisory advisory = null;
        if (dateComparator.compare(distributedTest.getBeginDate(), beginDate) != 0
                || hourComparator.compare(distributedTest.getBeginHour(), beginHour) != 0
                || dateComparator.compare(distributedTest.getEndDate(), endDate) != 0
                || hourComparator.compare(distributedTest.getEndHour(), endHour) != 0) {

            distributedTest.setBeginDate(beginDate);
            distributedTest.setBeginHour(beginHour);
            distributedTest.setEndDate(endDate);
            distributedTest.setEndHour(endHour);
            advisory = createTestAdvisory(distributedTest);
            distributedTest.updateDistributedTestAdvisoryDates(endDate.getTime());
            createDistributedTestAdvisory(distributedTest, advisory);
        }

        if (change2OtherType) {
            // Change evaluation test to study/inquiry test
            // delete evaluation and marks
            OnlineTest onlineTest = distributedTest.getOnlineTest();
            onlineTest.delete();
            /*
             * persistentSupport.getIPersistentMark().deleteByEvaluation(onlineTest);
             * persistentObject.deleteByOID(OnlineTest.class,
             * onlineTest.getIdInternal());
             */
        } else if (change2EvaluationType) {
            // Change to evaluation test
            // Create evaluation (onlineTest) and marks
            OnlineTest onlineTest = new OnlineTest();
            onlineTest.setDistributedTest(distributedTest);
            onlineTest.addAssociatedExecutionCourses(executionCourse);
            final Set<Registration> registrations = distributedTest.findStudents();
            for (Registration registration : registrations) {
            	Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
                double studentMark = 0;
                for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                    studentMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                }
                Attends attend = registration.readAttendByExecutionCourse(executionCourse);
                if (attend != null) {
                    Mark mark = new Mark();
                    mark.setAttend(attend);
                    mark.setEvaluation(onlineTest);
                    DecimalFormat df = new DecimalFormat("#0.##");
                    df.getDecimalFormatSymbols().setDecimalSeparator('.');
                    mark.setMark(df.format(Math.max(0, studentMark)));
                }
            }
        }
        if (advisory == null)
            return null;
        return advisory.getIdInternal();
    }

    private Advisory createTestAdvisory(DistributedTest distributedTest) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");

        Advisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"),
                new Object[] { ((ExecutionCourse) distributedTest.getTestScope().getDomainObject())
                        .getNome() }));
        advisory.setSubject(MessageFormat.format(bundle
                .getString("message.distributedTest.subjectChangeDates"), new Object[] { distributedTest
                .getTitle() }));

        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(),
                "HH:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(),
                "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "HH:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(),
                "dd/MM/yyyy");

        Object[] args = { this.contextPath, distributedTest.getIdInternal().toString(), beginHour,
                beginDate, endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setMessage(MessageFormat.format(bundle
                    .getString("message.distributedTest.messageChangeInquiryDates"), args));
        } else {
            advisory.setMessage(MessageFormat.format(bundle
                    .getString("message.distributedTest.messageChangeTestDates"), args));
        }

        return advisory;
    }

    private DistributedTestAdvisory createDistributedTestAdvisory(DistributedTest distributedTest,
            Advisory advisory) {
        DistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return distributedTestAdvisory;
    }

}