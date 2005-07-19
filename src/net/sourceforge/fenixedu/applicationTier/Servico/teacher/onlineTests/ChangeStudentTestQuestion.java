/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestion implements IService {

    public List<LabelValueBean> run(Integer executionCourseId, Integer distributedTestId, Integer oldQuestionId, Integer newMetadataId,
            Integer studentId, TestQuestionChangesType changesType, Boolean delete, TestQuestionStudentsChangesType studentsType)
            throws FenixServiceException {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion oldQuestion = (IQuestion) persistentQuestion.readByOID(Question.class, oldQuestionId, true);
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
                IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(
                        DistributedTest.class, distributedTestId);
                if (distributedTest == null)
                    throw new InvalidArgumentsServiceException();
                distributedTestList.add(distributedTest);

            }
            boolean canDelete = true;
            for (int i = 0; i < distributedTestList.size(); i++) {
                List<IStudentTestQuestion> studentsTestQuestionList = new ArrayList<IStudentTestQuestion>();
                IDistributedTest distributedTest = distributedTestList.get(i);

                if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
                    IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
                    if (student == null)
                        throw new InvalidArgumentsServiceException();
                    studentsTestQuestionList.add(persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(
                            oldQuestion.getIdInternal(), student.getIdInternal(), distributedTest.getIdInternal()));
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

                List<IPerson> group = new ArrayList<IPerson>();

                for (IStudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
                    persistentStudentTestQuestion.simpleLockWrite(studentTestQuestion);
                    if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(), studentTestQuestion.getDistributedTest().getEndHour())) {

                        result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle().concat(" (Ficha Fechada)"),
                                studentTestQuestion.getStudent().getNumber().toString()));
                        canDelete = false;
                    } else {

                        result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle(), studentTestQuestion.getStudent()
                                .getNumber().toString()));
                        IQuestion newQuestion = new Question();

                        if (availableQuestions.size() == 0)
                            availableQuestions.addAll(getNewQuestionList(metadata, oldQuestion));

                        newQuestion = getNewQuestion(availableQuestions);
                        if (newMetadataId == null && (newQuestion == null || newQuestion.equals(oldQuestion)))
                            return null;
                        else if (newQuestion == null)
                            throw new InvalidArgumentsServiceException();

                        studentTestQuestion.setQuestion(newQuestion);
                        studentTestQuestion.setOldResponse(new Integer(0));
                        studentTestQuestion.setResponse(null);
                        studentTestQuestion.setOptionShuffle(null);
                        availableQuestions.remove(newQuestion);
                        double oldMark = studentTestQuestion.getTestQuestionMark().doubleValue();
                        studentTestQuestion.setTestQuestionMark(new Double(0));
                        if (!group.contains(studentTestQuestion.getStudent().getPerson()))
                            group.add(studentTestQuestion.getStudent().getPerson());
                        if (studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                            IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(
                                    studentTestQuestion.getDistributedTest());
                            IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                                    studentTestQuestion.getStudent(), (IExecutionCourse) distributedTest.getTestScope().getDomainObject());
                            IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
                            if (mark != null) {
                                persistentSuport.getIPersistentMark().simpleLockWrite(mark);
                                mark.setMark(getNewStudentMark(persistentSuport, studentTestQuestion.getDistributedTest(), studentTestQuestion
                                        .getStudent(), oldMark));
                            }
                        }
                    }

                }

            }

            if (delete.booleanValue()) {
                metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, oldQuestion.getKeyMetadata(), true);
                if (metadata == null)
                    throw new InvalidArgumentsServiceException();
                metadata.setNumberOfMembers(new Integer(metadata.getNumberOfMembers().intValue() - 1));
                removeOldTestQuestion(persistentSuport, oldQuestion);
                List metadataQuestions = metadata.getVisibleQuestions();

                if (metadataQuestions != null && metadataQuestions.size() <= 1)
                    metadata.setVisibility(new Boolean(false));

                if (canDelete) {
                    int metadataNumberOfQuestions = persistentMetadata.getNumberOfQuestions(metadata);
                    persistentQuestion.deleteByOID(Question.class, oldQuestion.getIdInternal());
                    if (metadataNumberOfQuestions <= 1)
                        persistentMetadata.deleteByOID(Metadata.class, metadata.getIdInternal());
                } else {
                    oldQuestion.setVisibility(new Boolean(false));
                }

            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return result;
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
        List<ITestQuestion> testQuestionOldList = (List<ITestQuestion>) persistentTestQuestion.readByQuestion(oldQuestion.getIdInternal()).iterator();
        List<IQuestion> availableQuestions = new ArrayList<IQuestion>();
        availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
        availableQuestions.remove(oldQuestion);

        IQuestion newQuestion = getNewQuestion(availableQuestions);

        if (newQuestion == null || newQuestion.equals(oldQuestion)) {
            for (ITestQuestion oldTestQuestion : testQuestionOldList) {
                persistentTestQuestion.simpleLockWrite(oldTestQuestion);
                ITest test = oldTestQuestion.getTest();
                List<ITestQuestion> testQuestionList = persistentTestQuestion.readByTest(test.getIdInternal());
                Iterator testQuestionListIt = testQuestionList.iterator();
                for (ITestQuestion testQuestion : testQuestionList) {
                    if (testQuestion.getTestQuestionOrder().compareTo(oldTestQuestion.getTestQuestionOrder()) > 0) {
                        persistentTestQuestion.simpleLockWrite(testQuestion);
                        testQuestion.setTestQuestionOrder(new Integer(testQuestion.getTestQuestionOrder().intValue() - 1));
                    }
                }
                persistentSuport.getIPersistentTest().simpleLockWrite(test);
                test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
                persistentTestQuestion.deleteByOID(Question.class, oldTestQuestion.getIdInternal());
            }
        } else {
            for (ITestQuestion oldTestQuestion : testQuestionOldList) {
                persistentTestQuestion.simpleLockWrite(oldTestQuestion);
                oldTestQuestion.setKeyQuestion(newQuestion.getIdInternal());
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

}