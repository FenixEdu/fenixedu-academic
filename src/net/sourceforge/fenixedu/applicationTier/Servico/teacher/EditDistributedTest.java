/*
 * Created on 1/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IOnlineTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class EditDistributedTest implements IService {

    private String contextPath = new String();

    public EditDistributedTest() {
    }

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
            //create advisory for students that already have the test to
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
                //Change evaluation test to study/inquiry test
                //delete evaluation and marks
                IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTest);
                persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
                persistentSuport.getIPersistentOnlineTest().delete(onlineTest);
            } else if (change2EvaluationType) {
                //Change to evaluation test
                //Create evaluation (onlineTest) and marks
                IOnlineTest onlineTest = new OnlineTest();
                persistentSuport.getIPersistentEvaluation().simpleLockWrite(onlineTest);
                onlineTest.setDistributedTest(distributedTest);
                List executionCourseList = new ArrayList();
                executionCourseList.add(executionCourse);
                onlineTest.setAssociatedExecutionCourses(executionCourseList);
                List studentList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTest);
                Iterator studentIt = studentList.iterator();
                while (studentIt.hasNext()) {
                    IStudent student = (IStudent) studentIt.next();
                    List studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(student,
                            distributedTest);
                    Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
                    double studentMark = 0;
                    while (studentTestQuestionIt.hasNext()) {
                        IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) studentTestQuestionIt.next();
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
        IAdvisory advisory = new Advisory();
        advisory.setCreated(Calendar.getInstance().getTime());
        advisory.setExpires(distributedTest.getEndDate().getTime());
        advisory.setSender("Docente da disciplina " + ((IExecutionCourse) distributedTest.getTestScope().getDomainObject()).getNome());
        String msgBeginning;

        advisory.setSubject(distributedTest.getTitle() + ": Alteração de datas");
        if (distributedTest.getTestType().equals(new TestType(TestType.INQUIRY)))
            msgBeginning = new String("As datas para responder ao <a href='" + this.contextPath
                    + "/student/studentTests.do?method=prepareToDoTest&testCode=" + distributedTest.getIdInternal()
                    + "'>questionário</a> foram alteradas. Deverá responder ao questionário entre ");
        else
            msgBeginning = new String("As datas de realização da <a href='" + this.contextPath
                    + "/student/studentTests.do?method=prepareToDoTest&testCode=" + distributedTest.getIdInternal()
                    + "'>Ficha de Trabalho</a> foram alteradas. Deverá realizar a ficha entre ");
        advisory.setMessage(msgBeginning + " as " + getHourFormatted(distributedTest.getBeginHour()) + " de "
                + getDateFormatted(distributedTest.getBeginDate()) + " e as " + getHourFormatted(distributedTest.getEndHour()) + " de "
                + getDateFormatted(distributedTest.getEndDate()));
        advisory.setOnlyShowOnce(new Boolean(false));
        return advisory;
    }

    private IDistributedTestAdvisory createDistributedTestAdvisory(IDistributedTest distributedTest, IAdvisory advisory) {
        IDistributedTestAdvisory distributedTestAdvisory = new DistributedTestAdvisory();
        distributedTestAdvisory.setAdvisory(advisory);
        distributedTestAdvisory.setDistributedTest(distributedTest);
        return distributedTestAdvisory;
    }
}