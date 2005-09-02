/*
 * Created on Oct 20, 2003
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
import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.lang.time.DateFormatUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestion implements IService {

    public List<InfoSiteDistributedTestAdvisory> run(Integer executionCourseId, Integer distributedTestId, Integer oldQuestionId,
            Integer newMetadataId, Integer studentId, TestQuestionChangesType changesType, Boolean delete,
            TestQuestionStudentsChangesType studentsType, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IQuestion oldQuestion = (IQuestion) persistentSuport.getIPersistentQuestion().readByOID(Question.class, oldQuestionId);
        if (oldQuestion == null)
            throw new InvalidArgumentsServiceException();

        IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
        IMetadata metadata = null;

        List<IQuestion> availableQuestions = new ArrayList<IQuestion>();
        if (newMetadataId != null) {
            metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, newMetadataId);
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            availableQuestions.addAll(metadata.getVisibleQuestions());
        } else {
            availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
            availableQuestions.remove(oldQuestion);
        }

        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();

        List<IDistributedTest> distributedTestList = new ArrayList<IDistributedTest>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS)
            distributedTestList = persistentStudentTestQuestion.readDistributedTestsByTestQuestion(oldQuestion.getIdInternal());
        else {
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();
            distributedTestList.add(distributedTest);

        }
        boolean canDelete = true;
        for (IDistributedTest distributedTest : distributedTestList) {
            List<IStudentTestQuestion> studentsTestQuestionList = new ArrayList<IStudentTestQuestion>();
            InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory = new InfoSiteDistributedTestAdvisory();
            infoSiteDistributedTestAdvisory.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(distributedTest));
            infoSiteDistributedTestAdvisory.setInfoAdvisory(InfoAdvisory.newInfoFromDomain(getAdvisory(distributedTest, path.replace('\\', '/'))));

            if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
                IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
                if (student == null)
                    throw new InvalidArgumentsServiceException();
                studentsTestQuestionList.add(persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(oldQuestion.getIdInternal(),
                        student.getIdInternal(), distributedTest.getIdInternal()));
            } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
                IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
                if (student == null)
                    throw new InvalidArgumentsServiceException();
                Integer order = persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(oldQuestion.getIdInternal(),
                        student.getIdInternal(), distributedTest.getIdInternal()).getTestQuestionOrder();
                studentsTestQuestionList = persistentStudentTestQuestion.readByOrderAndDistributedTest(order, distributedTest.getIdInternal());
            } else
                studentsTestQuestionList = persistentStudentTestQuestion.readByQuestionAndDistributedTest(oldQuestion.getIdInternal(),
                        distributedTest.getIdInternal());

            List<InfoStudent> group = new ArrayList<InfoStudent>();

            for (IStudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
                if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(), studentTestQuestion.getDistributedTest().getEndHour())) {
                    canDelete = false;
                } else {
                    if (availableQuestions.size() == 0)
                        availableQuestions.addAll(getNewQuestionList(metadata, oldQuestion));

                    IQuestion newQuestion = getNewQuestion(availableQuestions);
                    if (newMetadataId == null && (newQuestion == null || newQuestion.equals(oldQuestion)))
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
                    if (studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                        IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(
                                studentTestQuestion.getDistributedTest().getIdInternal());
                        IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                                studentTestQuestion.getStudent(), (IExecutionCourse) distributedTest.getTestScope().getDomainObject());
                        IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
                        if (mark != null) {
                            mark.setMark(getNewStudentMark(persistentSuport, studentTestQuestion.getDistributedTest(), studentTestQuestion
                                    .getStudent(), oldMark));
                        }
                    }
                    IStudentTestLog studentTestLog = DomainFactory.makeStudentTestLog();
                    studentTestLog.setDistributedTest(studentTestQuestion.getDistributedTest());
                    studentTestLog.setStudent(studentTestQuestion.getStudent());
                    studentTestLog.setDate(Calendar.getInstance().getTime());
                    ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
                    studentTestLog.setEvent(MessageFormat.format(bundle.getString("message.changeStudentQuestionLogMessage"), new Object[] { studentTestQuestion.getTestQuestionOrder() }));
                }

            }
            infoSiteDistributedTestAdvisory.setInfoStudentList(group);
            infoSiteDistributedTestAdvisoryList.add(infoSiteDistributedTestAdvisory);
        }

        if (delete.booleanValue()) {
            metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, oldQuestion.getKeyMetadata());
            if (metadata == null)
                throw new InvalidArgumentsServiceException();
            removeOldTestQuestion(persistentSuport, oldQuestion);
            List metadataQuestions = metadata.getVisibleQuestions();

            if (metadataQuestions != null && metadataQuestions.size() <= 1)
                metadata.setVisibility(new Boolean(false));

            if (canDelete) {
                int metadataNumberOfQuestions = persistentMetadata.getNumberOfQuestions(metadata);
                oldQuestion.delete();
                if (metadataNumberOfQuestions <= 1) {
                    metadata.removeExecutionCourse();
                    persistentMetadata.deleteByOID(Metadata.class, metadata.getIdInternal());
                }
            } else {
                oldQuestion.setVisibility(new Boolean(false));
            }

        }
        return infoSiteDistributedTestAdvisoryList;
    }

    private IQuestion getNewQuestion(List<IQuestion> questions) {

        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    private List<IQuestion> getNewQuestionList(IMetadata metadata, IQuestion oldQuestion) {
        List<IQuestion> result = new ArrayList<IQuestion>();
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

    private void removeOldTestQuestion(ISuportePersistente persistentSuport, IQuestion oldQuestion) throws ExcepcaoPersistencia {

        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();
        List<ITestQuestion> testQuestionOldList = (List<ITestQuestion>) persistentTestQuestion.readByQuestion(oldQuestion.getIdInternal());
        List<IQuestion> availableQuestions = new ArrayList<IQuestion>();
        availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
        availableQuestions.remove(oldQuestion);

        IQuestion newQuestion = getNewQuestion(availableQuestions);

        if (newQuestion == null || newQuestion.equals(oldQuestion)) {
            for (ITestQuestion oldTestQuestion : testQuestionOldList) {
                ITest test = oldTestQuestion.getTest();
                test.deleteTestQuestion(oldTestQuestion);
            }
        } else {
            for (ITestQuestion oldTestQuestion : testQuestionOldList) {
                oldTestQuestion.setQuestion(newQuestion);
            }
        }
    }

    private String getNewStudentMark(ISuportePersistente sp, IDistributedTest dt, IStudent s, double mark2Remove) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List<IStudentTestQuestion> studentTestQuestionList = sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                s.getIdInternal(), dt.getIdInternal());
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        return (new java.text.DecimalFormat("#0.##").format(totalMark));
    }

    private IAdvisory getAdvisory(IDistributedTest distributedTest, String path) {
        ResourceBundle bundle = ResourceBundle.getBundle("ServidorApresentacao.ApplicationResources");
        // Create Advisory
        IAdvisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender(MessageFormat.format(bundle.getString("message.distributedTest.from"), new Object[] { ((IExecutionCourse) distributedTest
                .getTestScope().getDomainObject()).getNome() }));

        final String beginHour = DateFormatUtils.format(distributedTest.getBeginHour().getTime(), "hh:mm");
        final String beginDate = DateFormatUtils.format(distributedTest.getBeginDate().getTime(), "dd/MM/yyyy");
        final String endHour = DateFormatUtils.format(distributedTest.getEndHour().getTime(), "hh:mm");
        final String endDate = DateFormatUtils.format(distributedTest.getEndDate().getTime(), "dd/MM/yyyy");

        Object[] args = { path, distributedTest.getIdInternal().toString(), beginHour, beginDate, endHour, endDate };
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY))) {
            advisory.setSubject(MessageFormat.format(bundle.getString("message.distributedTest.changeInquirySubject"), new Object[] { distributedTest
                    .getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeInquiry"), args));
        } else {
            advisory.setSubject(MessageFormat.format(bundle.getString("message.distributedTest.changeTestSubject"), new Object[] { distributedTest
                    .getTitle() }));
            advisory.setMessage(MessageFormat.format(bundle.getString("message.distributedTest.messageChangeTest"), args));
        }

        advisory.setOnlyShowOnce(new Boolean(false));

        // Create DistributedTestAdvisory
        IDistributedTestAdvisory distributedTestAdvisory = DomainFactory.makeDistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return advisory;
    }

}