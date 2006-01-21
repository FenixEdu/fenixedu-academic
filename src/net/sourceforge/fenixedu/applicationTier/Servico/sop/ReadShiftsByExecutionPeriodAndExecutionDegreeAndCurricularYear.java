/*
 * ReadShiftsByExecutionDegreeAndCurricularYear.java
 * 
 * Created on 2003/08/09
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear extends Service {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) throws ExcepcaoPersistencia {

        List infoShifts = null;

        ExecutionPeriod executionPeriod = (ExecutionPeriod) persistentSupport.getIPersistentExecutionPeriod()
                .readByOID(ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());

        ExecutionDegree executionDegree = (ExecutionDegree) persistentSupport.getIPersistentExecutionDegree()
                .readByOID(ExecutionDegree.class, infoExecutionDegree.getIdInternal());

        CurricularYear curricularYear = (CurricularYear) persistentSupport.getIPersistentCurricularYear().readByOID(
                CurricularYear.class, infoCurricularYear.getIdInternal());

        List shifts = persistentSupport.getITurnoPersistente()
                .readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
                        executionPeriod.getIdInternal(), executionDegree.getIdInternal(),
                        curricularYear.getIdInternal());

        infoShifts = new ArrayList();
        for (int i = 0; i < shifts.size(); i++) {
            Shift shift = (Shift) shifts.get(i);

            InfoShift infoShift = new InfoShift();
            infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
            infoShift.setIdInternal(shift.getIdInternal());
            infoShift.setLotacao(shift.getLotacao());
            infoShift.setNome(shift.getNome());
            infoShift.setTipo(shift.getTipo());

            infoShift.setInfoLessons(new ArrayList());
            InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
                    .newInfoFromDomain(((Shift) shifts.get(i)).getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            for (int j = 0; j < ((Shift) shifts.get(i)).getAssociatedLessons().size(); j++) {
                Lesson lesson = ((Shift) shifts.get(i)).getAssociatedLessons().get(j);
                InfoLesson infoLesson = new InfoLesson();
                InfoRoom infoRoom = InfoRoom.newInfoFromDomain(lesson.getSala());
                InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(lesson
                        .getRoomOccupation());

                infoLesson.setDiaSemana(lesson.getDiaSemana());
                infoLesson.setFim(lesson.getFim());
                infoLesson.setIdInternal(lesson.getIdInternal());
                infoLesson.setInicio(lesson.getInicio());
                infoLesson.setTipo(lesson.getTipo());
                infoLesson.setInfoSala(infoRoom);
                infoLesson.setInfoRoomOccupation(infoRoomOccupation);

                infoLesson.setInfoShift(infoShift);
                infoShift.getInfoLessons().add(infoLesson);

            }
            infoShifts.add(infoShift);
        }

        return infoShifts;

    }

}