/*
 * Created on 1/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import Dominio.Advisory;
import Dominio.DistributedTest;
import Dominio.DistributedTestAdvisory;
import Dominio.ExecutionCourse;
import Dominio.IAdvisory;
import Dominio.IDistributedTest;
import Dominio.IDistributedTestAdvisory;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IOnlineTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Mark;
import Dominio.OnlineTest;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.tests.CorrectionAvailability;
import Util.tests.TestType;

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
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
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

                List students = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTest);
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
                    IFrequenta attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(student, executionCourse);
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