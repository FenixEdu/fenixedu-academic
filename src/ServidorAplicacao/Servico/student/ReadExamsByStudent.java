package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoExam;
import DataBeans.InfoExamStudentRoom;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoStudentSiteExams;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.utils.ExamsNotEnrolledPredicate;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */

public class ReadExamsByStudent implements IServico
{

    private static ReadExamsByStudent _servico = new ReadExamsByStudent();
    /**
     * The singleton access method of this class.
     **/
    public static ReadExamsByStudent getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private ReadExamsByStudent()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "ReadExamsByStudent";
    }

    //TODO: filtrar os exames por período de inscrição

    public Object run(String username)
    {

        List examsToEnroll = new ArrayList();

        List infoExamsToEnroll = new ArrayList();
        List infoExamStudentRoomList = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExamStudentRoom examStudentRoomDAO = sp.getIPersistentExamStudentRoom();
            IStudent student = sp.getIPersistentStudent().readByUsername(username);

            if (student != null)
            {
                List examsStudentRooms = examStudentRoomDAO.readBy(student);
                Iterator iter = examsStudentRooms.iterator();
                List examsEnrolled = new ArrayList();

                while (iter.hasNext())
                {
                    IExamStudentRoom examStudentRoom = (IExamStudentRoom) iter.next();

                    InfoExamStudentRoom infoExamStudentRoom = new InfoExamStudentRoom();
                    infoExamStudentRoom.setIdInternal(examStudentRoom.getIdInternal());
                    infoExamStudentRoom.setInfoExam(
                        Cloner.copyIExam2InfoExam(examStudentRoom.getExam()));
                    infoExamStudentRoom.getInfoExam().setInfoExecutionCourse(
                        (InfoExecutionCourse) Cloner.get(
                            (IExecutionCourse) examStudentRoom
                                .getExam()
                                .getAssociatedExecutionCourses()
                                .get(
                                0)));
                    infoExamStudentRoom.setInfoStudent(
                        Cloner.copyIStudent2InfoStudent(examStudentRoom.getStudent()));
                    if (examStudentRoom.getRoom() != null)
                    {
                        infoExamStudentRoom.setInfoRoom(
                            Cloner.copyRoom2InfoRoom(examStudentRoom.getRoom()));
                    }
                    infoExamStudentRoomList.add(infoExamStudentRoom);
                    examsEnrolled.add(examStudentRoom.getExam());
                }

                //
                //				Iterator examsEnrolledIterator = examsEnrolled.iterator();
                //				while (examsEnrolledIterator.hasNext()) {
                //					IExam exam = (IExam) examsEnrolledIterator.next();
                //					InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
                //					infoExam.setInfoExecutionCourse(
                //						Cloner.copyIExecutionCourse2InfoExecutionCourse(
                //							(IDisciplinaExecucao) exam
                //								.getAssociatedExecutionCourses()
                //								.get(
                //								0)));
                //
                //					infoExamsEnrolled.add(infoExam);
                //
                //					if (!isInDate(exam)) {
                //						//closedEnrollmentExams.add(infoExam);
                //						IExamStudentRoom examStudentRoom = examStudentRoomDAO.readBy(exam, student);
                //						InfoExamStudentRoom infoExamStudentRoom = null;
                //						if (examStudentRoom != null) {
                //							infoExamStudentRoom = Cloner.copyIExamStudentRoom2InfoExamStudentRoom(examStudentRoom);							
                //						} else {
                //							infoExamStudentRoom = new InfoExamStudentRoom();
                //							infoExamStudentRoom.setInfoRoom(null);
                //							infoExamStudentRoom.setInfoStudent(null);
                //							infoExamStudentRoom.setInfoExam(infoExam);
                //						}
                //						studentExamDistribution.add(infoExamStudentRoom);
                //					}
                //
                //				}

                List attends = sp.getIFrequentaPersistente().readByStudentNumber(student.getNumber());

                Iterator examsToEnrollIterator = attends.iterator();
                while (examsToEnrollIterator.hasNext())
                {
                    examsToEnroll.addAll(
                        ((IFrequenta) examsToEnrollIterator.next())
                            .getDisciplinaExecucao()
                            .getAssociatedExams());
                }

                CollectionUtils.filter(examsToEnroll, new ExamsNotEnrolledPredicate(examsEnrolled));

                Iterator iter3 = examsToEnroll.iterator();
                while (iter3.hasNext())
                {
                    IExam exam = (IExam) iter3.next();

                    if (isInDate(exam))
                    {
                        InfoExam infoExam = Cloner.copyIExam2InfoExam(exam);
                        infoExam.setInfoExecutionCourse(
                            (InfoExecutionCourse) Cloner.get(
                                (IExecutionCourse) exam.getAssociatedExecutionCourses().get(0)));
                        infoExamsToEnroll.add(infoExam);
                    }
                }

            }

        } catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
        }

        InfoStudentSiteExams component =
            new InfoStudentSiteExams(infoExamsToEnroll, infoExamStudentRoomList);
        SiteView siteView = new SiteView(component);
        return siteView;

    }

    /**
     * @param examEnrollment
     * @return
     */
    private boolean isInDate(IExam exam)
    {
        if (exam.getEnrollmentBeginDay() == null
            || exam.getEnrollmentEndDay() == null
            || exam.getEnrollmentBeginTime() == null
            || exam.getEnrollmentEndTime() == null)
        {
            return false;
        } else
        {
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
            return (
                Calendar.getInstance().getTimeInMillis() < end.getTimeInMillis()
                    && Calendar.getInstance().getTimeInMillis() > begin.getTimeInMillis());
        }
    }

}
