/*
 * Created on 19/Mar/2004
 *
 */
package ServidorAplicacao.Servico.sop.exams;

/**
 * @author Ana e Ricardo
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoViewExam;
import DataBeans.InfoViewExamByDayAndShift;
import DataBeans.util.Cloner;
import Dominio.ICurricularCourseScope;
import Dominio.IDegree;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IRoom;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoSala;

public class ReadExamsByDate implements IServico {

    private static ReadExamsByDate _servico = new ReadExamsByDate();

    /**
     * The singleton access method of this class.
     */
    public static ReadExamsByDate getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadExamsByDate() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadExamsByDate";
    }

    public InfoViewExam run(Calendar day, Calendar beginning, Calendar end) {
        InfoViewExam infoViewExam = new InfoViewExam();
        List infoViewExams = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

            // Read exams on requested day, start and end time
            List exams = sp.getIPersistentExam().readBy(day, beginning, end);

            IExam tempExam = null;
            InfoExam tempInfoExam = null;
            //List tempAssociatedCurricularCourses = null;
            IDegree tempDegree = null;
            List tempInfoExecutionCourses = null;
            List tempInfoDegrees = null;
            //            Integer numberStudentesAttendingCourse = null;
            int totalNumberStudents = 0;

            // For every exam return:
            //    - exam info
            //    - degrees associated with exam
            //    - number of students attending course (potentially attending
            // exam)
            for (int i = 0; i < exams.size(); i++) {
                // prepare exam info
                tempExam = (IExam) exams.get(i);
                tempInfoExam = Cloner.copyIExam2InfoExam(tempExam);
                tempInfoDegrees = new ArrayList();
                tempInfoExecutionCourses = new ArrayList();
                // int totalNumberStudentsForExam = 0;
                IExecutionPeriod executionPeriod = ((IExecutionCourse) tempExam
                        .getAssociatedExecutionCourses().get(0)).getExecutionPeriod();

                if (tempExam.getAssociatedExecutionCourses() != null) {
                    for (int k = 0; k < tempExam.getAssociatedExecutionCourses().size(); k++) {
                        IExecutionCourse executionCourse = (IExecutionCourse) tempExam
                                .getAssociatedExecutionCourses().get(k);
                        tempInfoExecutionCourses.add(Cloner.get(executionCourse));

                        // prepare degrees associated with exam
                        //                        tempAssociatedCurricularCourses =
                        //                            executionCourse.getAssociatedCurricularCourses();
                        //                        for (int j = 0; j <
                        // tempAssociatedCurricularCourses.size(); j++)
                        //                        {
                        //                            tempDegree =
                        //                                ((ICurricularCourse)
                        // tempAssociatedCurricularCourses.get(j))
                        //                                    .getDegreeCurricularPlan()
                        //                                    .getDegree();
                        //                            tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
                        //                        }

                        // determine number of students attending course and
                        // exam
                        //                        numberStudentesAttendingCourse =
                        //                            sp.getIFrequentaPersistente().countStudentsAttendingExecutionCourse(
                        //                                executionCourse);
                        //                        totalNumberStudents +=
                        // numberStudentesAttendingCourse.intValue();
                        //                        totalNumberStudentsForExam +=
                        // numberStudentesAttendingCourse.intValue();
                    }
                }
                int numberOfStudentsForExam = 0;
                List curricularCourseIDs = new ArrayList();
                for (int j = 0; j < tempExam.getAssociatedCurricularCourseScope().size(); j++) {
                    ICurricularCourseScope scope = (ICurricularCourseScope) tempExam
                            .getAssociatedCurricularCourseScope().get(j);
                    if (!curricularCourseIDs.contains(scope.getCurricularCourse().getIdInternal())) {
                        curricularCourseIDs.add(scope.getCurricularCourse().getIdInternal());
                        List enroledStudents = persistentEnrolment
                                .readByCurricularCourseAndExecutionPeriod(scope.getCurricularCourse(),
                                        executionPeriod);
                        numberOfStudentsForExam += enroledStudents.size();

                        tempDegree = scope.getCurricularCourse().getDegreeCurricularPlan().getDegree();
                        tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));

                    }
                }
                totalNumberStudents += numberOfStudentsForExam;

                // add exam and degree info to result list
                infoViewExams.add(new InfoViewExamByDayAndShift(tempInfoExam, tempInfoExecutionCourses,
                        tempInfoDegrees, new Integer(numberOfStudentsForExam)));

            }

            infoViewExam.setInfoViewExamsByDayAndShift(infoViewExams);

            // Read all rooms to determine total exam capacity
            // TODO : This can be done more efficiently.
            List rooms = sp.getISalaPersistente().readAll();
            int totalExamCapacity = 0;
            for (int i = 0; i < rooms.size(); i++) {
                IRoom room = (IRoom) rooms.get(i);
                if (!room.getTipo().equals(new TipoSala(TipoSala.LABORATORIO))) {
                    totalExamCapacity += room.getCapacidadeExame().intValue();
                }
            }

            infoViewExam
                    .setAvailableRoomOccupation(new Integer(totalExamCapacity - totalNumberStudents));

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }

        return infoViewExam;
    }

}