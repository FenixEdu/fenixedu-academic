/*
 * Created on Oct 20, 2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentStudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ChangeStudentTestQuestionMark implements IService {
    public List<LabelValueBean> run(Integer executionCourseId, Integer distributedTestId, Double newMark, Integer questionId, Integer studentId,
            TestQuestionStudentsChangesType studentsType) throws FenixServiceException, ExcepcaoPersistencia {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentTestQuestion persistentStudentTestQuestion = persistentSuport.getIPersistentStudentTestQuestion();
        List<IStudentTestQuestion> studentsTestQuestionList = new ArrayList<IStudentTestQuestion>();
        if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.THIS_STUDENT) {
            studentsTestQuestionList.add(persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId, studentId,
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST_VARIATION) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestionAndDistributedTest(questionId, distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.STUDENTS_FROM_TEST) {
            IStudentTestQuestion studentTestQuestion = persistentStudentTestQuestion.readByQuestionAndStudentAndDistributedTest(questionId,
                    studentId, distributedTestId);
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByOrderAndDistributedTest(studentTestQuestion.getTestQuestionOrder(),
                    distributedTestId));
        } else if (studentsType.getType().intValue() == TestQuestionStudentsChangesType.ALL_STUDENTS) {
            studentsTestQuestionList.addAll(persistentStudentTestQuestion.readByQuestion(questionId));
        }
        for (IStudentTestQuestion studentTestQuestion : studentsTestQuestionList) {
            if (!compareDates(studentTestQuestion.getDistributedTest().getEndDate(), studentTestQuestion.getDistributedTest().getEndHour())) {
                result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle().concat(" (Ficha Fechada)"), studentTestQuestion
                        .getStudent().getNumber().toString()));
            } else {
                studentTestQuestion.setTestQuestionMark(newMark);
                if (studentTestQuestion.getResponse() != null
                        && studentTestQuestion.getDistributedTest().getTestType().equals(new TestType(TestType.EVALUATION))) {
                    IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(
                            studentTestQuestion.getDistributedTest().getIdInternal());
                    IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseId);
                    IAttends attend = persistentSuport.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(studentTestQuestion.getStudent(),
                            executionCourse);
                    IMark mark = persistentSuport.getIPersistentMark().readBy(onlineTest, attend);
                    if (mark != null) {
                        mark.setMark(getNewStudentMark(persistentSuport, studentTestQuestion.getDistributedTest(), studentTestQuestion.getStudent()));
                    }
                    result.add(new LabelValueBean(studentTestQuestion.getDistributedTest().getTitle(), studentTestQuestion.getStudent().getNumber()
                            .toString()));
                }
            }
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

    private String getNewStudentMark(ISuportePersistente sp, IDistributedTest dt, IStudent s) throws ExcepcaoPersistencia {
        double totalMark = 0;
        List<IStudentTestQuestion> studentTestQuestionList = sp.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                s.getIdInternal(), dt.getIdInternal());
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            totalMark += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        return (new java.text.DecimalFormat("#0.##").format(totalMark));
    }
}