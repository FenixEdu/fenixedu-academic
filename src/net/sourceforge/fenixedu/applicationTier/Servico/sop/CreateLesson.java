/* 
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.DiaSemana;

public class CreateLesson extends Service {

    public InfoLessonServiceResult run(DiaSemana weekDay, Calendar begin, Calendar end, Integer frequency, 
    		Integer weekOfQuinzenalStart, InfoRoomOccupation infoRoomOccupation, InfoShift infoShift)
            throws FenixServiceException {
    	
        final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(
                        infoShift.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal());

        final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());

        InfoLessonServiceResult result = validTimeInterval(begin, end);
        if (result.getMessageType() == 1) {
            throw new InvalidTimeIntervalServiceException();
        }
        
        boolean resultB = true;
        if(infoRoomOccupation != null) {
             resultB = validNoInterceptingLesson(infoRoomOccupation);
        }
        
        if (result.isSUCESS() && resultB) {
        	
                InfoShiftServiceResult infoShiftServiceResult = valid(shift, begin, end);
                if (infoShiftServiceResult.isSUCESS()) {
                    
                    RoomOccupation roomOccupation = null;
                    if(infoRoomOccupation != null) { 
                        roomOccupation = new RoomOccupation();
                        roomOccupation.setDayOfWeek(infoRoomOccupation.getDayOfWeek());
                        roomOccupation.setStartTime(infoRoomOccupation.getStartTime());
                        roomOccupation.setEndTime(infoRoomOccupation.getEndTime());                       
                    }
                   
                    OldRoom sala = null;
                    if(infoRoomOccupation.getInfoRoom() != null) {
                        sala = OldRoom.findOldRoomByName(infoRoomOccupation.getInfoRoom().getNome());                       
                    }
                    
                    if(roomOccupation != null) {
                        roomOccupation.setRoom(sala);                        
                        final OccupationPeriod period = rootDomainObject.readOccupationPeriodByOID(infoRoomOccupation.getInfoPeriod().getIdInternal());
                        roomOccupation.setPeriod(period);
                    }
                    Lesson aula2 = new Lesson(weekDay, begin, end, infoShift.getTipo(), sala,
                            roomOccupation, shift, weekOfQuinzenalStart, frequency);

                    aula2.setExecutionPeriod(executionPeriod);
                } else {
                    throw new InvalidLoadException(infoShiftServiceResult.toString());
                }

        } else {
            if (!resultB) {
                throw new InterceptingLessonException();
            } else {
                result.setMessageType(2);                
            }
        }

        return result;
    }

    private boolean validNoInterceptingLesson(InfoRoomOccupation infoRoomOccupation) {
        final OldRoom room = OldRoom.findOldRoomByName(infoRoomOccupation.getInfoRoom().getNome());
        final List<RoomOccupation> roomOccupations = room.getRoomOccupations();

        for (final RoomOccupation roomOccupation : roomOccupations) {
            if (roomOccupation.roomOccupationForDateAndTime(
                    infoRoomOccupation.getInfoPeriod().getStartDate(),
                    infoRoomOccupation.getInfoPeriod().getEndDate(),
                    infoRoomOccupation.getStartTime(),
                    infoRoomOccupation.getEndTime(),
                    infoRoomOccupation.getDayOfWeek(),
                    infoRoomOccupation.getFrequency(),
                    infoRoomOccupation.getWeekOfQuinzenalStart())) {
                return false;
            }
        }
        return true;
    }

    private InfoLessonServiceResult validTimeInterval(Calendar begin, Calendar end) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (begin.getTime().getTime() >= end.getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    private InfoShiftServiceResult valid(Shift shift, Calendar begin, Calendar end) {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCCESS);
        double hours = getTotalHoursOfShiftType(shift);
        double lessonDuration = (getLessonDurationInMinutes(begin, end).doubleValue()) / 60;
        
        if (shift.getTipo().equals(ShiftType.TEORICA)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue(), 
                InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED, InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(ShiftType.PRATICA)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getPraticalHours().doubleValue(), 
                    InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED, InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);                      
        } else if (shift.getTipo().equals(ShiftType.TEORICO_PRATICA)) {           
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getTheoPratHours().doubleValue(), 
                    InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED, InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);                       
        } else if (shift.getTipo().equals(ShiftType.LABORATORIAL)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getLabHours().doubleValue(), 
                    InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED, InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);                     
        } else if (shift.getTipo().equals(ShiftType.SEMINARY)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getSeminaryHours().doubleValue(), 
                    InfoShiftServiceResult.SEMINARY_LIMIT_REACHED, InfoShiftServiceResult.SEMINARY_LIMIT_EXCEEDED);                     
        } else if (shift.getTipo().equals(ShiftType.PROBLEMS)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getProblemsHours().doubleValue(), 
                    InfoShiftServiceResult.PROBLEMS_LIMIT_REACHED, InfoShiftServiceResult.PROBLEMS_LIMIT_EXCEEDED);                     
        } else if (shift.getTipo().equals(ShiftType.FIELD_WORK)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getFieldWorkHours().doubleValue(), 
                    InfoShiftServiceResult.FIELD_WORK_LIMIT_REACHED, InfoShiftServiceResult.FIELD_WORK_LIMIT_EXCEEDED);                     
        } else if (shift.getTipo().equals(ShiftType.TRAINING_PERIOD)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getTrainingPeriodHours().doubleValue(), 
                    InfoShiftServiceResult.TRAINING_PERIOD_LIMIT_REACHED, InfoShiftServiceResult.TRAINING_PERIOD_LIMIT_EXCEEDED);                     
        } else if (shift.getTipo().equals(ShiftType.TUTORIAL_ORIENTATION)) {
            validForType(result, hours, lessonDuration, shift.getDisciplinaExecucao().getTutorialOrientationHours().doubleValue(), 
                    InfoShiftServiceResult.TUTORIAL_ORIENTATION_LIMIT_REACHED, InfoShiftServiceResult.TUTORIAL_ORIENTATION_LIMIT_EXCEEDED);                     
        }        
        
        return result;
    }
    
    private void validForType(InfoShiftServiceResult infoShiftServiceResult, double hours, double lessonDuration, 
            double maxLessonHoursForType, int reachedType, int limitExceededType) {        
        if (hours == maxLessonHoursForType) {
            infoShiftServiceResult.setMessageType(reachedType);
        }
        else if ((hours + lessonDuration) > maxLessonHoursForType) {
            infoShiftServiceResult.setMessageType(limitExceededType);
        }
    }

    private double getTotalHoursOfShiftType(Shift shift) {
        Lesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (Lesson) associatedLessons.get(i);
            lesson.getIdInternal();
            duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(Lesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;
        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    private Integer getLessonDurationInMinutes(Calendar begin, Calendar end) {
        int beginHour = begin.get(Calendar.HOUR_OF_DAY);
        int beginMinutes = begin.get(Calendar.MINUTE);
        int endHour = end.get(Calendar.HOUR_OF_DAY);
        int endMinutes = end.get(Calendar.MINUTE);
        return Integer.valueOf((endHour - beginHour) * 60 + (endMinutes - beginMinutes));
    }

    public class InvalidLoadException extends FenixServiceException {
        private InvalidLoadException() {
            super();
        }

        InvalidLoadException(String s) {
            super(s);
        }
    }

    public class InterceptingLessonException extends FenixServiceException {
        private InterceptingLessonException() {
            super();
        }

        InterceptingLessonException(String s) {
            super(s);
        }
    }
}
