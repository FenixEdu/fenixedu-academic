/*
 * Created on Oct 20, 2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IMetadata;
import Dominio.IOnlineTest;
import Dominio.IQuestion;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Question;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.TestQuestionChangesType;
import Util.tests.TestQuestionStudentsChangesType;
import Util.tests.TestType;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestion implements IService {

    public ChangeStudentTestQuestion() {
    }

    public List run(Integer executionCourseId, Integer distributedTestId, Integer oldQuestionId,
            Integer newMetadataId, Integer studentId, TestQuestionChangesType changesType,
            Boolean delete, TestQuestionStudentsChangesType studentsType) throws FenixServiceException {
        List result = new ArrayList();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new InvalidArgumentsServiceException();

            IPersistentQuestion persistentQuestion = persistentSuport.getIPersistentQuestion();
            IQuestion oldQuestion = (IQuestion) persistentQuestion.readByOID(Question.class,
                    oldQuestionId, true);
            if (oldQuestion == null)
                throw new InvalidArgumentsServiceException();

            IPersistentMetadata persistentMetadata = persistentSuport.getIPersistentMetadata();
            IMetadata metadata = null;

            List availableQuestions = new ArrayList();
            if (newMetadataId != null) {
                metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, newMetadataId);
                if (metadata == null)
                    throw new InvalidArgumentsServiceException();
                availableQuestions.addAll(metadata.getVisibleQuestions());
            } else {
                availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
                availableQuestions.remove(oldQuestion);
            }

            IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport
                    .getIPersistentStudentTestQuestion();

            List distributedTestList = new ArrayList();
            if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS)
                distributedTestList = persistentStudentTestQuestion
                        .readDistributedTestsByTestQuestion(oldQuestion);
            else {
                IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                        .getIPersistentDistributedTest().readByOID(DistributedTest.class,
                                distributedTestId);
                if (distributedTest == null)
                    throw new InvalidArgumentsServiceException();
                distributedTestList.add(distributedTest);

            }
            boolean canDelete = true;
            for (int i = 0; i < distributedTestList.size(); i++) {
                List studentsTestQuestionList = new ArrayList();
                IDistributedTest distributedTest = (IDistributedTest) distributedTestList.get(i);

                if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
                    IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                            Student.class, studentId);
                    if (student == null)
                        throw new InvalidArgumentsServiceException();
                    studentsTestQuestionList.add(persistentStudentTestQuestion
                            .readByQuestionAndStudentAndDistributedTest(oldQuestion, student,
                                    distributedTest));
                } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
                    IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(
                            Student.class, studentId);
                    if (student == null)
                        throw new InvalidArgumentsServiceException();
                    Integer order = ((IStudentTestQuestion) persistentStudentTestQuestion
                            .readByQuestionAndStudentAndDistributedTest(oldQuestion, student,
                                    distributedTest)).getTestQuestionOrder();
                    studentsTestQuestionList = persistentStudentTestQuestion
                            .readByOrderAndDistributedTest(order, distributedTest);
                } else
                    studentsTestQuestionList = persistentStudentTestQuestion
                            .readByQuestionAndDistributedTest(oldQuestion, distributedTest);

                Iterator studentsTestQuestionIt = studentsTestQuestionList.iterator();

                List group = new ArrayList();

                while (studentsTestQuestionIt.hasNext()) {
                    IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentsTestQuestionIt
                            .next();
                    persistentStudentTestQuestion.simpleLockWrite(studentTestQuestion);
                    if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(),
                            studentTestQuestion.getDistributedTest().getEndHour())) {

                        result.add(new LabelValueBean(studentTestQuestion.getDistributedTest()
                                .getTitle().concat(" (Ficha Fechada)"), studentTestQuestion.getStudent()
                                .getNumber().toString()));
                        canDelete = false;
                    } else {

                        result.add(new LabelValueBean(studentTestQuestion.getDistributedTest()
                                .getTitle(), studentTestQuestion.getStudent().getNumber().toString()));
                        IQuestion newQuestion = new Question();

                        if (availableQuestions.size() == 0)
                            availableQuestions.addAll(getNewQuestionList(metadata, oldQuestion));

                        newQuestion = getNewQuestion(availableQuestions);
                        if (newMetadataId == null
                                && (newQuestion == null || newQuestion.equals(oldQuestion)))
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
                        if (studentTestQuestion.getDistributedTest().getTestType().equals(
                                new TestType(TestType.EVALUATION))) {
                            IOnlineTest onlineTest = (IOnlineTest) persistentSuport
                                    .getIPersistentOnlineTest().readByDistributedTest(
                                            studentTestQuestion.getDistributedTest());
                            IFrequenta attend = persistentSuport.getIFrequentaPersistente()
                                    .readByAlunoAndDisciplinaExecucao(studentTestQuestion.getStudent(),
                                            executionCourse);
                            IMark mark = persistentSuport.getIPersistentMark()
                                    .readBy(onlineTest, attend);
                            if (mark != null) {
                                persistentSuport.getIPersistentMark().simpleLockWrite(mark);
                                mark
                                        .setMark(getNewStudentMark(persistentSuport, studentTestQuestion
                                                .getDistributedTest(), studentTestQuestion.getStudent(),
                                                oldMark));
                            }
                        }
                    }

                }

            }

            if (delete.booleanValue()) {
                metadata = (IMetadata) persistentMetadata.readByOID(Metadata.class, oldQuestion
                        .getKeyMetadata(), true);
                if (metadata == null)
                    throw new InvalidArgumentsServiceException();
                metadata.setNumberOfMembers(new Integer(metadata.getNumberOfMembers().intValue() - 1));
                removeOldTestQuestion(persistentSuport, oldQuestion);
                List metadataQuestions = metadata.getVisibleQuestions();

                if (metadataQuestions != null && metadataQuestions.size() <= 1)
                    metadata.setVisibility(new Boolean(false));

                if (canDelete) {
                    int metadataNumberOfQuestions = persistentMetadata.getNumberOfQuestions(metadata);
                    persistentQuestion.delete(oldQuestion);
                    if (metadataNumberOfQuestions <= 1)
                        persistentMetadata.delete(metadata);
                } else {
                    oldQuestion.setVisibility(new Boolean(false));
                }

            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return result;
    }

    private IQuestion getNewQuestion(List questions) throws ExcepcaoPersistencia {

        IQuestion question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = (IQuestion) questions.get(questionIndex);
        }
        return question;
    }

    private List getNewQuestionList(IMetadata metadata, IQuestion oldQuestion)
            throws ExcepcaoPersistencia {
        List result = new ArrayList();
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

    private void removeOldTestQuestion(ISuportePersistente persistentSuport, IQuestion oldQuestion)
            throws ExcepcaoPersistencia {

        IPersistentTestQuestion persistentTestQuestion = persistentSuport.getIPersistentTestQuestion();
        Iterator it = persistentTestQuestion.readByQuestion(oldQuestion).iterator();
        List availableQuestions = new ArrayList();
        availableQuestions.addAll(oldQuestion.getMetadata().getVisibleQuestions());
        availableQuestions.remove(oldQuestion);

        IQuestion newQuestion = getNewQuestion(availableQuestions);

        if (newQuestion == null || newQuestion.equals(oldQuestion)) {
            while (it.hasNext()) {
                ITestQuestion oldTestQuestion = (ITestQuestion) it.next();
                persistentTestQuestion.simpleLockWrite(oldTestQuestion);
                ITest test = oldTestQuestion.getTest();

                List testQuestionList = persistentTestQuestion.readByTest(test);
                Iterator testQuestionListIt = testQuestionList.iterator();
                while (testQuestionListIt.hasNext()) {
                    ITestQuestion testQuestion = (ITestQuestion) testQuestionListIt.next();
                    if (testQuestion.getTestQuestionOrder().compareTo(
                            oldTestQuestion.getTestQuestionOrder()) > 0) {
                        persistentTestQuestion.simpleLockWrite(testQuestion);
                        testQuestion.setTestQuestionOrder(new Integer(testQuestion
                                .getTestQuestionOrder().intValue() - 1));
                    }
                }
                persistentSuport.getIPersistentTest().simpleLockWrite(test);
                test.setNumberOfQuestions(new Integer(test.getNumberOfQuestions().intValue() - 1));
                persistentTestQuestion.delete(oldTestQuestion);
            }
        } else {
            while (it.hasNext()) {
                ITestQuestion oldTestQuestion = (ITestQuestion) it.next();
                persistentTestQuestion.simpleLockWrite(oldTestQuestion);

                oldTestQuestion.setKeyQuestion(newQuestion.getIdInternal());
                oldTestQuestion.setQuestion(newQuestion);
            }
        }
    }

    private String getNewStudentMark(ISuportePersistente sp, IDistributedTest dt, IStudent s,
            double mark2Remove) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List studentTestQuestionList = sp.getIPersistentStudentTestQuestion()
                .readByStudentAndDistributedTest(s, dt);

        Iterator it = studentTestQuestionList.iterator();
        while (it.hasNext()) {
            IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        return (new java.text.DecimalFormat("#0.##").format(totalMark));
    }

}