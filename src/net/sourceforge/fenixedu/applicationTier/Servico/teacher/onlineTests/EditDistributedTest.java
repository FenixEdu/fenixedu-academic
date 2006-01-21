/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest extends Service {

    private String contextPath = new String();

    public Integer run(Integer executionCourseId, Integer distributedTestId, String testInformation, Calendar beginDate, Calendar beginHour,
            Calendar endDate, Calendar endHour, TestType testType, CorrectionAvailability correctionAvailability, Boolean imsFeedback,
            String contextPath) throws FenixServiceException, ExcepcaoPersistencia {
        this.contextPath = contextPath.replace('\\', '/');
        ExecutionCourse executionCourse = (ExecutionCourse) persistentSupport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                executionCourseId);
        if (executionCourse == null)
            throw new InvalidArgumentsServiceException();

        IPersistentDistributedTest persistentDistributedTest = persistentSupport.getIPersistentDistributedTest();
        DistributedTest distributedTest = (DistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId);
        if (distributedTest == null)
            throw new InvalidArgumentsServiceException();

        distributedTest.setTestInformation(testInformation);
        boolean change2EvaluationType = false, change2OtherType = false;
        if (distributedTest.getTestType().equals(new TestType(TestType.EVALUATION)) && (!testType.equals(new TestType(TestType.EVALUATION))))
            change2OtherType = true;
        else if ((!distributedTest.getTestType().equals(new TestType(TestType.EVALUATION))) && testType.equals(new TestType(TestType.EVALUATION)))
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
            persistentSupport.getIPersistentDistributedTestAdvisory().updateDistributedTestAdvisoryDates(distributedTest, endDate.getTime());
            createDistributedTestAdvisory(distributedTest, advisory);
        }

        if (change2OtherType) {
            // Change evaluation test to study/inquiry test
            // delete evaluation and marks
            OnlineTest onlineTest = (OnlineTest) persistentSupport.getIPersistentOnlineTest().readByDistributedTest(distributedTestId);
            persistentSupport.getIPersistentMark().deleteByEvaluation(onlineTest);
            persistentSupport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
        } else if (change2EvaluationType) {
            // Change to evaluation test
            // Create evaluation (onlineTest) and marks
            OnlineTest onlineTest = DomainFactory.makeOnlineTest();
            onlineTest.setDistributedTest(distributedTest);
            onlineTest.addAssociatedExecutionCourses(executionCourse);
            List<Student> studentList = persistentSupport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                    distributedTest.getIdInternal());
            for (Student student : studentList) {
                List<StudentTestQuestion> studentTestQuestionList = persistentSupport.getIPersistentStudentTestQuestion()
                        .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());
                double studentMark = 0;
                for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                    studentMark += studentTestQuestion.getTestQuestionMark().doubleValue();
                }
                Attends attend = persistentSupport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student.getIdInternal(),
                        executionCourse.getIdInternal());
                if (attend != null) {
                    Mark mark = DomainFactory.makeMark();
                    mark.setAttend(attend);
                    mark.setEvaluation(onlineTest);
                    DecimalFormat df =new DecimalFormat("#0.##");
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
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");

        Advisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((ExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));        
        advisory.setSubject(MessageFormat.format(bundle.getString("message.distributedTest.subjectChangeDates"), new Object[] { distributedTest
                .getTitle() }));

        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(), "HH:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(), "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "HH:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(), "dd/MM/yyyy");

        Object[] args = { this.contextPath, distributedTest.getIdInternal().toString(), beginHour, beginDate, endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeInquiryDates"), args));
        } else {
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeTestDates"), args));
        }

        return advisory;
    }

    private DistributedTestAdvisory createDistributedTestAdvisory(DistributedTest distributedTest, Advisory advisory) {
        DistributedTestAdvisory distributedTestAdvisory = DomainFactory.makeDistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return distributedTestAdvisory;
    }
}
