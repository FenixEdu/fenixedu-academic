package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.utils.ExamsNotEnrolledPredicate;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamStudentRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentSiteExams;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExamStudentRoom;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamStudentRoom;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 *  
 */

public class ReadExamsByStudent implements IService {

    public Object run(String username) {

        List examsToEnroll = new ArrayList();

        List infoExamsToEnroll = new ArrayList();
        List infoExamStudentRoomList = new ArrayList();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExamStudentRoom examStudentRoomDAO = sp.getIPersistentExamStudentRoom();
            IStudent student = sp.getIPersistentStudent().readByUsername(username);

            if (student != null) {
                List examsStudentRooms = examStudentRoomDAO.readBy(student);
                Iterator iter = examsStudentRooms.iterator();
                List examsEnrolled = new ArrayList();

                while (iter.hasNext()) {
                    IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();

                    InfoExamStudentRoom infoExamStudentRoom = new InfoExamStudentRoom();
                    infoExamStudentRoom.setIdInternal(examStudentRoom.getIdInternal());
                    InfoExam infoExam = InfoExam.newInfoFromDomain(examStudentRoom.getExam());
                    infoExamStudentRoom.setInfoExam(infoExam);
                    infoExamStudentRoom.getInfoExam().setInfoExecutionCourses(
                            (List) CollectionUtils.collect(examStudentRoom.getExam()
                                    .getAssociatedExecutionCourses(), new Transformer() {

                                public Object transform(Object arg0) {
                                    return InfoExecutionCourse.newInfoFromDomain((IExecutionCourse) arg0);
                                }
                            }));
                    InfoStudent infoStudent = InfoStudent.newInfoFromDomain(examStudentRoom.getStudent());
                    infoExamStudentRoom.setInfoStudent(infoStudent);
                    if (examStudentRoom.getRoom() != null) {
                        InfoRoom infoRoom = InfoRoom.newInfoFromDomain(examStudentRoom.getRoom());
                        infoExamStudentRoom.setInfoRoom(infoRoom);
                    }
                    infoExamStudentRoomList.add(infoExamStudentRoom);
                    examsEnrolled.add(examStudentRoom.getExam());
                }

                List attends = sp.getIFrequentaPersistente().readByStudentNumber(student.getNumber(), student.getDegreeType());

                Iterator examsToEnrollIterator = attends.iterator();
                while (examsToEnrollIterator.hasNext()) {
                    examsToEnroll.addAll(((IAttends) examsToEnrollIterator.next())
                            .getDisciplinaExecucao().getAssociatedExams());
                }

                CollectionUtils.filter(examsToEnroll, new ExamsNotEnrolledPredicate(examsEnrolled));

                Iterator iter3 = examsToEnroll.iterator();
                while (iter3.hasNext()) {

                    IEvaluation evaluation = (IEvaluation) iter3.next();
                    if (evaluation instanceof Exam) {
                        IExam exam = (IExam) evaluation;

                        if (isInDate(exam)) {
                            InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
                            infoExam.setInfoExecutionCourses((List) CollectionUtils.collect(exam
                                    .getAssociatedExecutionCourses(), new Transformer() {

                                public Object transform(Object arg0) {
                                    return InfoExecutionCourse.newInfoFromDomain((IExecutionCourse) arg0);
                                }
                            }));

                            infoExamsToEnroll.add(infoExam);
                        }
                    }
                }

            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }

        InfoStudentSiteExams component = new InfoStudentSiteExams(infoExamsToEnroll,
                infoExamStudentRoomList);
        SiteView siteView = new SiteView(component);
        return siteView;

    }

    /**
     * @param examEnrollment
     * @return
     */
    private boolean isInDate(IExam exam) {
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