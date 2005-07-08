/*
 * AdicionarAula.java
 *
 * Created on 26 de Outubro de 2002, 19:26
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Service AdicionarAula.
 * 
 * @author tfc130
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AdicionarAula implements IService {

    /**
     * The actor of this class.
     */
    public AdicionarAula() {
    }

    public List run(InfoShift infoShift, String[] classesList) throws FenixServiceException {

        //ITurnoAula turnoAula = null;
        InfoShiftServiceResult result = null;
        List serviceResult = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoShift
                    .getInfoDisciplinaExecucao());

            IShift turno1 = sp.getITurnoPersistente().readByNameAndExecutionCourse(infoShift.getNome(),
                    executionCourse.getIdInternal());

            int i = 0;
            while (i < classesList.length) {
                Integer lessonId = new Integer(classesList[i]);
                ILesson lesson = (ILesson) sp.getIAulaPersistente().readByOID(Lesson.class, lessonId);
                if (lesson != null) {
                    //turnoAula = new TurnoAula(turno1, lesson);
                    result = valid(turno1, lesson);
                    serviceResult.add(result);
                    if (result.isSUCESS()) {
                        /*
                         * try { sp.getITurnoAulaPersistente().simpleLockWrite(
                         * turnoAula); } catch (ExistingPersistentException ex) {
                         * throw new ExistingServiceException(ex); }
                         */
                        try {
                            sp.getIAulaPersistente().simpleLockWrite(lesson);
                            lesson.setShift(turno1);
                        } catch (ExcepcaoPersistencia ex) {

                        }
                    }
                }
                i++;
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return serviceResult;
    }

    private InfoShiftServiceResult valid(IShift shift, ILesson lesson) {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);

        double hours = getTotalHoursOfShiftType(shift);
        double lessonDuration = (getLessonDurationInMinutes(lesson).doubleValue()) / 60;

        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.PRATICA)) {
            if (hours == shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getPraticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoPratHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
            if (hours == shift.getDisciplinaExecucao().getLabHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getLabHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
        }

        return result;
    }

    private double getTotalHoursOfShiftType(IShift shift) {
        /*
         * IShift shiftCriteria = new Shift();
         * shiftCriteria.setNome(shift.getNome());
         * shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());
         * 
         * List lessonsOfShiftType = PersistenceSupportFactory.getDefaultPersistenceSupport()
         * .getITurnoAulaPersistente().readLessonsByShift(shiftCriteria);
         * 
         * ILesson lesson = null; double duration = 0; for (int i = 0; i <
         * lessonsOfShiftType.size(); i++) { lesson = ((ITurnoAula)
         * lessonsOfShiftType.get(i)).getAula(); duration +=
         * (getLessonDurationInMinutes(lesson).doubleValue() / 60); } return
         * duration;
         */

        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();

        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
        }

        return duration;
    }

    private Integer getLessonDurationInMinutes(ILesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;

        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

}