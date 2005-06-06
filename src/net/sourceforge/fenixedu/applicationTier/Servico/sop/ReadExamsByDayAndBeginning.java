/*
 * ReadExamsByDayAndBeginning.java
 * 
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewExamByDayAndShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoSala;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExamsByDayAndBeginning implements IService {

    private static ReadExamsByDayAndBeginning _servico = new ReadExamsByDayAndBeginning();

    public InfoViewExam run(Calendar day, Calendar beginning) throws ExcepcaoPersistencia {
        InfoViewExam infoViewExam = new InfoViewExam();
        List infoViewExams = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read exams on requested day and start-time
        List exams = sp.getIPersistentExam().readBy(day, beginning);

        IExam tempExam = null;
        InfoExam tempInfoExam = null;
        List tempAssociatedCurricularCourses = null;
        IDegree tempDegree = null;
        List tempInfoExecutionCourses = null;
        List tempInfoDegrees = null;
        Integer numberStudentesAttendingCourse = null;
        int totalNumberStudents = 0;

        // For every exam return:
        // - exam info
        // - degrees associated with exam
        // - number of students attending course (potentially attending
        // exam)
        for (int i = 0; i < exams.size(); i++) {
            // prepare exam info
            tempExam = (IExam) exams.get(i);
            tempInfoExam = Cloner.copyIExam2InfoExam(tempExam);
            tempInfoDegrees = new ArrayList();
            tempInfoExecutionCourses = new ArrayList();
            int totalNumberStudentsForExam = 0;

            if (tempExam.getAssociatedExecutionCourses() != null) {
                for (int k = 0; k < tempExam.getAssociatedExecutionCourses().size(); k++) {
                    IExecutionCourse executionCourse = (IExecutionCourse) tempExam
                            .getAssociatedExecutionCourses().get(k);
                    tempInfoExecutionCourses.add(Cloner.get(executionCourse));

                    // prepare degrees associated with exam
                    tempAssociatedCurricularCourses = executionCourse.getAssociatedCurricularCourses();
                    for (int j = 0; j < tempAssociatedCurricularCourses.size(); j++) {
                        tempDegree = ((ICurricularCourse) tempAssociatedCurricularCourses.get(j))
                                .getDegreeCurricularPlan().getDegree();
                        tempInfoDegrees.add(Cloner.copyIDegree2InfoDegree(tempDegree));
                    }

                    // determine number of students attending course and
                    // exam
                    numberStudentesAttendingCourse = sp.getIFrequentaPersistente()
                            .countStudentsAttendingExecutionCourse(executionCourse);
                    totalNumberStudents += numberStudentesAttendingCourse.intValue();
                    totalNumberStudentsForExam += numberStudentesAttendingCourse.intValue();
                }
            }

            // add exam and degree info to result list
            infoViewExams.add(new InfoViewExamByDayAndShift(tempInfoExam, tempInfoExecutionCourses,
                    tempInfoDegrees, new Integer(totalNumberStudentsForExam)));
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

        infoViewExam.setAvailableRoomOccupation(new Integer(totalExamCapacity - totalNumberStudents));
        return infoViewExam;
    }

}