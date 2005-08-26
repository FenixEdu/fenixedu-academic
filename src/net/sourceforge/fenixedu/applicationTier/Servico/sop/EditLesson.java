package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.DiaSemana;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditLesson implements IService {

    public Object run(InfoLesson aulaAntiga, InfoLesson aulaNova, InfoShift infoShift)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoLessonServiceResult result = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IAulaPersistente aulaPersistente = sp.getIAulaPersistente();

        IRoom salaNova = sp.getISalaPersistente().readByName(aulaNova.getInfoSala().getNome());
        ILesson aula = (ILesson) aulaPersistente.readByOID(Lesson.class, aulaAntiga.getIdInternal());
        IShift shift = aula.getShift();

        IRoomOccupation roomOccupation = aula.getRoomOccupation();

        if (aula != null) {
            result = valid(aulaNova.getInicio(), aulaNova.getFim());
            if (result.getMessageType() == 1) {
                throw new InvalidTimeIntervalServiceException();
            }
            boolean resultB = validNoInterceptingLesson(
                    aulaNova.getInfoRoomOccupation().getStartTime(),
                    aulaNova.getInfoRoomOccupation().getEndTime(), 
                    aulaNova.getInfoRoomOccupation().getDayOfWeek(), 
                    aulaNova.getInfoRoomOccupation().getFrequency(), 
                    aulaNova.getInfoRoomOccupation().getWeekOfQuinzenalStart(), 
                    roomOccupation);

            InfoShiftServiceResult infoShiftServiceResult = valid(shift, aula.getIdInternal(), aulaNova.getInicio(), aulaNova.getFim());
            if (result.isSUCESS() && resultB && infoShiftServiceResult.isSUCESS()) {
                aula.getShift();

                aula.setDiaSemana(aulaNova.getDiaSemana());
                aula.setInicio(aulaNova.getInicio());
                aula.setFim(aulaNova.getFim());
                aula.setTipo(aulaNova.getTipo());
                aula.setSala(salaNova);
                
                roomOccupation.setDayOfWeek(aulaNova.getDiaSemana());
                roomOccupation.setStartTime(aulaNova.getInicio());
                roomOccupation.setEndTime(aulaNova.getFim());
                roomOccupation.setRoom(salaNova);

                roomOccupation.setFrequency(aulaNova.getInfoRoomOccupation().getFrequency());
                roomOccupation.setWeekOfQuinzenalStart(aulaNova.getInfoRoomOccupation()
                        .getWeekOfQuinzenalStart());

            } else if (!infoShiftServiceResult.isSUCESS()) {
                throw new InvalidLoadException(infoShiftServiceResult.toString());
            }
        }

        return result;
    }

    private InfoLessonServiceResult valid(Calendar start, Calendar end) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (start.getTime().getTime() >= end.getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    /**
     * @param aula
     * @return InfoLessonServiceResult
     */
    private boolean validNoInterceptingLesson(Calendar startTime, Calendar endTime,
            DiaSemana dayOfWeek, Integer frequency, Integer week, IRoomOccupation oldroomOccupation) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();

            Iterator iter = roomOccupationInDBList.iterator();
            while (iter.hasNext()) {
                IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();
                if (roomOccupationInDB.equals(oldroomOccupation)) {
                    continue;
                }
                if (roomOccupationInDB.roomOccupationForDateAndTime(oldroomOccupation.getPeriod(), startTime, endTime, dayOfWeek, frequency, week, oldroomOccupation.getRoom())) {
                    return false;
                }
            }
            return true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
    }

    private InfoShiftServiceResult valid(IShift shift, Integer lessonId, Calendar start, Calendar end) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift, lessonId, start, end);
        double lessonDuration = (getLessonDurationInMinutes(start, end).doubleValue()) / 60;

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

    private double getTotalHoursOfShiftType(IShift shift, Integer lessonId, Calendar start, Calendar end)
            throws ExcepcaoPersistencia {
        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            if (!lesson.getIdInternal().equals(lessonId)) {
                duration += (getLessonDurationInMinutes(start, end).doubleValue() / 60);
            }
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(Calendar start, Calendar end) {
        int beginHour = start.get(Calendar.HOUR_OF_DAY);
        int beginMinutes = start.get(Calendar.MINUTE);
        int endHour = end.get(Calendar.HOUR_OF_DAY);
        int endMinutes = end.get(Calendar.MINUTE);
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
