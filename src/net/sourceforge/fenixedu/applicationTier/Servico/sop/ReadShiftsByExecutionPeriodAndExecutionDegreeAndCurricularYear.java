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
import java.util.Set;

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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadShiftsByExecutionPeriodAndExecutionDegreeAndCurricularYear extends Service {

    public Object run(InfoExecutionPeriod infoExecutionPeriod, InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) throws ExcepcaoPersistencia {
        final ExecutionPeriod executionPeriod = RootDomainObject.getInstance().readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());
        final ExecutionDegree executionDegree = RootDomainObject.getInstance().readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
        final CurricularYear curricularYear = RootDomainObject.getInstance().readCurricularYearByOID(infoCurricularYear.getIdInternal());

        final Set<Shift> shifts = executionDegree.findAvailableShifts(curricularYear, executionPeriod);

        final List infoShifts = new ArrayList();
        for (final Shift shift : shifts) {
        	final InfoShift infoShift = new InfoShift();
            infoShift.setAvailabilityFinal(shift.getAvailabilityFinal());
            infoShift.setIdInternal(shift.getIdInternal());
            infoShift.setLotacao(shift.getLotacao());
            infoShift.setNome(shift.getNome());
            infoShift.setTipo(shift.getTipo());

            infoShift.setInfoLessons(new ArrayList());
            final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain((shift).getDisciplinaExecucao());
            infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);
            for (final Lesson lesson : shift.getAssociatedLessons()) {
            	final InfoLesson infoLesson = new InfoLesson();
            	final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(lesson.getSala());
            	final InfoRoomOccupation infoRoomOccupation = InfoRoomOccupation.newInfoFromDomain(lesson
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