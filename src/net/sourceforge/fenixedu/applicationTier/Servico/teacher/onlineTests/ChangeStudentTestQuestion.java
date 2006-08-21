package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

public class ChangeStudentTestQuestion extends Service {

    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId,
            Integer distributedTestId, Integer oldQuestionId, Integer newMetadataId, Integer studentId,
            TestQuestionChangesType changesType, Boolean delete,
            TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        Question oldQuestion = distributedTest.findQuestionByOID(oldQuestionId); 
        
        if (oldQuestion == null)
            throw new InvalidArgumentsServiceException();

        Metadata metadata = null;

        List<Question> availableQuestions = new ArrayList<Question>();
        if (newMetadataId != null) {
            metadata = rootDomainObject.readMetadataByOID(newMetadataId);
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            availableQuestions.addAll(metadata.getVisibleQuestions());
        } else {
            availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
            availableQuestions.remove(oldQuestion);
        }

        final Set<DistributedTest> distributedTestList;
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS)
            distributedTestList = oldQuestion.findDistributedTests();
        else {
            distributedTestList = new HashSet<DistributedTest>();

            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            distributedTestList.add(distributedTest);

        }
        boolean canDelete = true;
        for (DistributedTest currentDistributedTest : distributedTestList) {
            Collection<StudentTestQuestion> studentsTestQuestionList = new ArrayList<StudentTestQuestion>();
            InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory = new InfoSiteDistributedTestAdvisory();
            infoSiteDistributedTestAdvisory.setInfoDistributedTest(InfoDistributedTest
                    .newInfoFromDomain(currentDistributedTest));
            infoSiteDistributedTestAdvisory.setInfoAdvisory(InfoAdvisory.newInfoFromDomain(getAdvisory(
                    currentDistributedTest, path.replace('\\', '/'))));

            if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
                Registration registration = rootDomainObject.readRegistrationByOID(studentId);
                if (registration == null)
                    throw new InvalidArgumentsServiceException();
            	studentsTestQuestionList.add(StudentTestQuestion.findStudentTestQuestion(oldQuestion, registration, currentDistributedTest));
            } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
                Registration registration = rootDomainObject.readRegistrationByOID(studentId);
                if (registration == null)
                    throw new InvalidArgumentsServiceException();
                Integer order = StudentTestQuestion.findStudentTestQuestion(oldQuestion, registration, currentDistributedTest).getTestQuestionOrder();
                studentsTestQuestionList = currentDistributedTest.findStudentTestQuestionsByTestQuestionOrder(order);
            } else
                studentsTestQuestionList = StudentTestQuestion.findStudentTestQuestions(oldQuestion, currentDistributedTest);

            List<InfoStudent> group = new ArrayList<InfoStudent>();

            for (StudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
                if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(),
                        studentTestQuestion.getDistributedTest().getEndHour())) {
                    canDelete = false;
                } else {
                    if (availableQuestions.size() == 0)
                        availableQuestions.addAll(getNewQuestionList(metadata, oldQuestion));

                    Question newQuestion = getNewQuestion(availableQuestions);
                    if (newMetadataId == null
                            && (newQuestion == null || newQuestion.equals(oldQuestion)))
                        return null;
                    else if (newQuestion == null)
                        throw new InvalidArgumentsServiceException();

                    studentTestQuestion.setQuestion(newQuestion);
                    studentTestQuestion.setResponse(null);
                    studentTestQuestion.setOptionShuffle(null);
                    availableQuestions.remove(newQuestion);
                    double oldMark = studentTestQuestion.getTestQuestionMark().doubleValue();
                    studentTestQuestion.setTestQuestionMark(new Double(0));
                    if (!group.contains(studentTestQuestion.getStudent().getPerson()))
                        group.add(InfoStudent.newInfoFromDomain(studentTestQuestion.getStudent()));
                    if (studentTestQuestion.getDistributedTest().getTestType().equals(
                            new TestType(TestType.EVALUATION))) {
                        OnlineTest onlineTest = studentTestQuestion.getDistributedTest().getOnlineTest();
                        Attends attend = studentTestQuestion.getStudent().readAttendByExecutionCourse(
                                ((ExecutionCourse) currentDistributedTest.getTestScope().getDomainObject()));
                        Mark mark = onlineTest.getMarkByAttend(attend);
                        if (mark != null) {
                            mark.setMark(getNewStudentMark(studentTestQuestion.getDistributedTest(),
                                    studentTestQuestion.getStudent(), oldMark));
                        }
                    }
                    StudentTestLog studentTestLog = new StudentTestLog();
                    studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
                    studentTestLog.setStudent(studentTestQuestion.getStudent());
                    studentTestLog.setDate(Calendar.getInstance().getTime());
                    ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
                    studentTestLog.setEvent(MessageFormat.format(bundle
                            .getString("message.changeStudentQuestionLogMessage"),
                            new Object[] { studentTestQuestion.getTestQuestionOrder() }));
                }

            }
            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);
        }

        if (delete.booleanValue()) {
            metadata = oldQuestion.getMetadata();
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            removeOldTestQuestion(oldQuestion);
            List metadataQuestions = metadata.getVisibleQuestions();

            if (metadataQuestions != null && metadataQuestions.size() <= 1)
                metadata.setVisibility(Boolean.FALSE);

            if (canDelete) {
                oldQuestion.delete();
                if (metadata.getQuestionsCount() <= 1) {
                    metadata.delete();
                }
            } else {
                oldQuestion.setVisibility(Boolean.FALSE);
            }

        }
        return infoSiteDistributedTestAdvisoryList;
    }

    private Question getNewQuestion(List<Question> questions) {

        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    private List<Question> getNewQuestionList(Metadata metadata, Question oldQuestion) {
        List<Question> result = new ArrayList<Question>();
        if (metadata != null) {
            result.addAll(metadata.getVisibleQuestions());
        } else {
            result.addAll(oldQuestion.getMetadata().getVisibleQuestions());
            result.remove(oldQuestion);
        }
        return result;
    }

    private boolean compareDates(Calendar date, Calendar hour) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(calendar, date) <= 0) {
            if (dateComparator.compare(calendar, date) == 0) {
                if (hourComparator.compare(calendar, hour) <= 0) {
                    return true;
                }

                return false;
            }
            return true;
        }
        return false;
    }

    private void removeOldTestQuestion(Question oldQuestion) {
        List<TestQuestion> testQuestionOldList = oldQuestion.getTestQuestions();
        List<Question> availableQuestions = new ArrayList<Question>();
        availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
        availableQuestions.remove(oldQuestion);

        Question newQuestion = getNewQuestion(availableQuestions);

        if (newQuestion == null || newQuestion.equals(oldQuestion)) {
            for (TestQuestion oldTestQuestion : testQuestionOldList) {
                Test test = oldTestQuestion.getTest();
                test.deleteTestQuestion(oldTestQuestion);
            }
        } else {
            for (TestQuestion oldTestQuestion : testQuestionOldList) {
                oldTestQuestion.setQuestion(newQuestion);
            }
        }
    }

    private String getNewStudentMark(DistributedTest dt, Registration s, double mark2Remove)
            throws ExcepcaoPersistencia {
        double totalMark = 0;
        Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(s, dt);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        DecimalFormat df = new DecimalFormat("#0.##");
        DecimalFormatSymbols decimalFormatSymbols = df.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalFormatSymbols);
        return (df.format(Math.max(0, totalMark)));
    }

    private Advisory getAdvisory(DistributedTest distributedTest, String path) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
        // Create Advisory
        Advisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"),
                new Object[] { ((ExecutionCourse) distributedTest.getTestScope().getDomainObject())
                        .getNome() }));

        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(),
                "HH:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(),
                "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "HH:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(),
                "dd/MM/yyyy");

        Object[] args = { path, distributedTest.getIdInternal().toString(), beginHour, beginDate,
                endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setSubject(MessageFormat.format(bundle
                    .getString("message.distributedTest.changeInquirySubject"),
                    new Object[] { distributedTest.getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle
                    .getString("message.distributedTest.messageChangeInquiry"), args));
        } else {
            advisory.setSubject(MessageFormat.format(bundle
                    .getString("message.distributedTest.changeTestSubject"),
                    new Object[] { distributedTest.getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle
                    .getString("message.distributedTest.messageChangeTest"), args));
        }

        // Create DistributedTestAdvisory
        DistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return advisory;
    }

}