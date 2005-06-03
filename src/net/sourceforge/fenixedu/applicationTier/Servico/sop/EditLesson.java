/*
 * EditarAula.java
 *
 * Created on 27 de Outubro de 2002, 19:05
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o EditarAula.
 * 
 * @author tfc130
 */
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.domain.ShiftType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditLesson implements IService {

    public Object run(InfoLesson aulaAntiga, InfoLesson aulaNova, InfoShift infoShift)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoLessonServiceResult result = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IAulaPersistente aulaPersistente = sp.getIAulaPersistente();
        IRoom salaAntiga = sp.getISalaPersistente().readByName(aulaAntiga.getInfoSala().getNome());
        IRoom salaNova = sp.getISalaPersistente().readByName(aulaNova.getInfoSala().getNome());

        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(aulaNova
                .getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod());

        ILesson aula = (ILesson) aulaPersistente.readByOID(Lesson.class, aulaAntiga.getIdInternal());
        IShift shift = aula.getShift();

        IRoomOccupation roomOccupation = aula.getRoomOccupation();

        ILesson newLesson = new Lesson(aulaNova.getDiaSemana(), aulaNova.getInicio(), aulaNova.getFim(),
                aulaNova.getTipo(), salaNova, roomOccupation, shift /* ,null */
        );
        newLesson.setIdInternal(aula.getIdInternal());
        List associatedLessons = shift.getAssociatedLessons();
        for (int i = 0; i < associatedLessons.size(); i++) {
            ILesson lessonAssociated = (ILesson) associatedLessons.get(i);
            if (lessonAssociated.getIdInternal().equals(aula.getIdInternal())) {
                associatedLessons.remove(i);
                // associatedLessons.set(i, newLesson);
                break;
            }
        }
        shift.setAssociatedLessons(associatedLessons);
        if (aula != null) {
            result = valid(newLesson);
            if (result.getMessageType() == 1) {
                throw new InvalidTimeIntervalServiceException();
            }
            boolean resultB = validNoInterceptingLesson(Cloner
                    .copyInfoRoomOccupation2RoomOccupation(aulaNova.getInfoRoomOccupation()),
                    roomOccupation);

            InfoShiftServiceResult infoShiftServiceResult = valid(shift, newLesson);
            if (result.isSUCESS() && resultB && infoShiftServiceResult.isSUCESS()) {
                // aula = (ILesson) aulaPersistente.readByOId(aula, true);
                aula = (ILesson) aulaPersistente.readByOID(Lesson.class, aula.getIdInternal());
                aulaPersistente.simpleLockWrite(aula);
                // sp.getITurnoPersistente().simpleLockWrite(shift);
                aula.setDiaSemana(aulaNova.getDiaSemana());
                aula.setInicio(aulaNova.getInicio());
                aula.setFim(aulaNova.getFim());
                aula.setTipo(aulaNova.getTipo());
                aula.setSala(salaNova);
                aula.setRoomOccupation(roomOccupation);

                roomOccupation = (IRoomOccupation) sp.getIPersistentRoomOccupation().readByOID(
                        RoomOccupation.class, roomOccupation.getIdInternal());

                sp.getIPersistentRoomOccupation().simpleLockWrite(roomOccupation);

                roomOccupation.setDayOfWeek(aulaNova.getDiaSemana());
                roomOccupation.setStartTime(aulaNova.getInicio());
                roomOccupation.setEndTime(aulaNova.getFim());
                roomOccupation.setRoom(salaNova);

                roomOccupation.setFrequency(aulaNova.getInfoRoomOccupation().getFrequency().intValue());
                roomOccupation.setWeekOfQuinzenalStart(aulaNova.getInfoRoomOccupation()
                        .getWeekOfQuinzenalStart());

            } else if (!infoShiftServiceResult.isSUCESS()) {
                throw new InvalidLoadException(infoShiftServiceResult.toString());
            }
        }

        return result;
    }

    private InfoLessonServiceResult valid(ILesson lesson) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    /**
     * @param aula
     * @return InfoLessonServiceResult
     */
    private boolean validNoInterceptingLesson(IRoomOccupation roomOccupation,
            IRoomOccupation oldroomOccupation) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();

            Iterator iter = roomOccupationInDBList.iterator();
            while (iter.hasNext()) {
                IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();
                if (roomOccupationInDB.equals(oldroomOccupation)) {
                    continue;
                }
                if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB)) {
                    return false;
                }
            }
            return true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
    }

    private InfoShiftServiceResult valid(IShift shift, ILesson lesson) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift, lesson);
        double lessonDuration = (getLessonDurationInMinutes(lesson).doubleValue()) / 60;

        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            } else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue()) {
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
            }
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

    private double getTotalHoursOfShiftType(IShift shift, ILesson alteredLesson)
            throws ExcepcaoPersistencia {
        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        if (associatedLessons == null) {
            associatedLessons = shift.getAssociatedLessons();
            shift.setAssociatedLessons(associatedLessons);
        }
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            if (!lesson.getIdInternal().equals(alteredLesson.getIdInternal())) {
                duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
            }
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

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidLoadException extends FenixServiceException {
        /**
         * 
         */
        private InvalidLoadException() {
            super();
        }

        /**
         * @param s
         */
        InvalidLoadException(String s) {
            super(s);
        }
    }
}