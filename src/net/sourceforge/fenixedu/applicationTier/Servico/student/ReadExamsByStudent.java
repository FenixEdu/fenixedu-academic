package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.utils.ExamsNotEnrolledPredicate;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentSiteExams;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.WrittenEvaluationEnrolment;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Jo�o Mota
 * 
 */

public class ReadExamsByStudent extends Service {

    public Object run(String username) throws ExcepcaoPersistencia {

        List examsToEnroll = new ArrayList();

        List infoExamsToEnroll = new ArrayList();
        List infoWrittenEvaluationEnrolmentList = new ArrayList();

        Registration registration = Registration.readByUsername(username);

        if (registration != null) {
            List examsStudentRooms = registration.getWrittenEvaluationEnrolments();
            Iterator iter = examsStudentRooms.iterator();
            List<Exam> examsEnrolled = new ArrayList<Exam>();

            while (iter.hasNext()) {
                WrittenEvaluationEnrolment writtenEvaluationEnrolment = (WrittenEvaluationEnrolment) iter
                        .next();

                InfoWrittenEvaluationEnrolment infoWrittenEvaluationEnrolment = new InfoWrittenEvaluationEnrolment();
                infoWrittenEvaluationEnrolment.setIdInternal(writtenEvaluationEnrolment.getIdInternal());
                InfoExam infoExam = InfoExam.newInfoFromDomain((Exam) writtenEvaluationEnrolment
                        .getWrittenEvaluation());
                infoWrittenEvaluationEnrolment.setInfoExam(infoExam);
                infoWrittenEvaluationEnrolment.getInfoExam().setInfoExecutionCourses(
                        (List) CollectionUtils.collect(writtenEvaluationEnrolment.getWrittenEvaluation()
                                .getAssociatedExecutionCourses(), new Transformer() {

                            public Object transform(Object arg0) {
                                return InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) arg0);
                            }
                        }));
                InfoStudent infoStudent = InfoStudent.newInfoFromDomain(writtenEvaluationEnrolment
                        .getStudent());
                infoWrittenEvaluationEnrolment.setInfoStudent(infoStudent);
                if (writtenEvaluationEnrolment.getRoom() != null) {
                    InfoRoom infoRoom = InfoRoom.newInfoFromDomain(writtenEvaluationEnrolment.getRoom());
                    infoWrittenEvaluationEnrolment.setInfoRoom(infoRoom);
                }
                infoWrittenEvaluationEnrolmentList.add(infoWrittenEvaluationEnrolment);
                examsEnrolled.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
            }

            List attends = registration.getAssociatedAttends();

            Iterator examsToEnrollIterator = attends.iterator();
            while (examsToEnrollIterator.hasNext()) {
                examsToEnroll.addAll(((Attends) examsToEnrollIterator.next()).getExecutionCourse()
                        .getAssociatedEvaluations());
            }

            CollectionUtils.filter(examsToEnroll, new ExamsNotEnrolledPredicate(examsEnrolled));

            Iterator iter3 = examsToEnroll.iterator();
            while (iter3.hasNext()) {

                Evaluation evaluation = (Evaluation) iter3.next();
                if (evaluation instanceof Exam) {
                    Exam exam = (Exam) evaluation;

                    if (isInDate(exam)) {
                        InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
                        infoExam.setInfoExecutionCourses((List) CollectionUtils.collect(exam
                                .getAssociatedExecutionCourses(), new Transformer() {

                            public Object transform(Object arg0) {
                                return InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) arg0);
                            }
                        }));

                        infoExamsToEnroll.add(infoExam);
                    }
                }
            }

        }

        InfoStudentSiteExams component = new InfoStudentSiteExams(infoExamsToEnroll,
                infoWrittenEvaluationEnrolmentList);
        SiteView siteView = new SiteView(component);
        return siteView;

    }

    private boolean isInDate(Exam exam) {
        if (exam.getEnrollmentBeginDay() == null || exam.getEnrollmentEndDay() == null
                || exam.getEnrollmentBeginTime() == null || exam.getEnrollmentEndTime() == null) {
            return false;
        }
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, exam.getEnrollmentBeginDay().get(Calendar.YEAR));
        begin.set(Calendar.MONTH, exam.getEnrollmentBeginDay().get(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, exam.getEnrollmentBeginDay().get(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, exam.getEnrollmentBeginTime().get(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, exam.getEnrollmentBeginTime().get(Calendar.MINUTE));

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, exam.getEnrollmentEndDay().get(Calendar.YEAR));
        end.set(Calendar.MONTH, exam.getEnrollmentEndDay().get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, exam.getEnrollmentEndDay().get(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, exam.getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, exam.getEnrollmentEndTime().get(Calendar.MINUTE));
        return (Calendar.getInstance().getTimeInMillis() < end.getTimeInMillis() && Calendar
                .getInstance().getTimeInMillis() > begin.getTimeInMillis());

    }

}