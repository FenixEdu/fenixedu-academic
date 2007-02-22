/*
 * Created on 19 de Julho de 2004, 15:30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servico ReadLessonsAndExamsInWeekAndRoom.
 * 
 * @author Ana e Ricardo
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom extends Service {

    // FIXME duplicated code: this method is (almost?) identical to RoomSiteComponentBuilder.getInfoSiteRoomTimeTable
    public List run(InfoRoom infoRoom, Calendar day, InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia, FenixServiceException {
    	final OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(infoRoom.getIdInternal());

        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(infoExecutionPeriod.getIdInternal());

        Calendar startDay = Calendar.getInstance();
        startDay.setTimeInMillis(day.getTimeInMillis());
        startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
        Calendar endDay = Calendar.getInstance();
        endDay.setTimeInMillis(startDay.getTimeInMillis());
        endDay.add(Calendar.DATE, 6);
        //OccupationPeriod weekPeriod = new OccupationPeriod(day, endDay);

        InfoPeriod lessonsInfoPeriod = calculateLessonsSeason(executionPeriod);
        
        if (this.intersectPeriods(day, endDay,lessonsInfoPeriod)) {
            //adicionar as aulas
        	
            List lessonList = room.findLessonsForExecutionPeriod(executionPeriod);
            Iterator iterator = lessonList.iterator();

            while (iterator.hasNext()) {
                Lesson aula = (Lesson) iterator.next();
                RoomOccupation roomOccupation = aula.getRoomOccupation();

                OccupationPeriod period = roomOccupation.getPeriod();
                InfoPeriod infoPeriod = InfoPeriod.newInfoFromDomain(period);

                if (this.intersectPeriods(day, endDay, infoPeriod)) {

                    boolean add = true;
                    if ((roomOccupation.getFrequency().intValue() == RoomOccupation.QUINZENAL)
                            && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period.getStartDate(), period.getEndDate(), 
	                            roomOccupation.getWeekOfQuinzenalStart().intValue(),
                                    roomOccupation.getDayOfWeek(), day, endDay, null))) {
                        add = false;
                    }
                    if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
                        Calendar dayOfLesson = Calendar.getInstance();
                        dayOfLesson.setTimeInMillis(day.getTimeInMillis());
                        dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana().intValue() - Calendar.MONDAY);
                        if (!this.intersectPeriods(dayOfLesson, dayOfLesson, infoPeriod)) {
                            add = false;
                        }
                    }
                    if (add) {
                        Shift shift = aula.getShift();
                        if (shift == null) {
                            continue;
                        }
                        infoShowOccupations.add(InfoLesson.newInfoFromDomain(aula));
                    }
                }
            }
        }

        final Date startDate = startDay.getTime();
        final Date endDate = endDay.getTime();
        
        final YearMonthDay weekStartYearMonthDay = YearMonthDay.fromDateFields(startDate);
        final YearMonthDay weekEndYearMonthDay = YearMonthDay.fromDateFields(endDate).minusDays(1);
        
        for (final RoomOccupation roomOccupation : room.getRoomOccupations()) {
            
            final WrittenEvaluation writtenEvaluation = roomOccupation.getWrittenEvaluation();
            final GenericEvent genericEvent = roomOccupation.getGenericEvent();
            
            // Written Evaluation Room Occupations
            getWrittenEvaluationRoomOccupations(infoShowOccupations, startDate, endDate, writtenEvaluation);
            
            // Generic Event Room Occupations
            getGenericEventRoomOccupations(infoShowOccupations, startDay, endDay, weekStartYearMonthDay, weekEndYearMonthDay, genericEvent);            
        }

        return infoShowOccupations;
    }

    private void getWrittenEvaluationRoomOccupations(List<InfoObject> infoShowOccupations, final Date startDate, final Date endDate, final WrittenEvaluation writtenEvaluation) {
	if (writtenEvaluation != null) {
	    final Date evaluationDate = writtenEvaluation.getDayDate();
	    if (!evaluationDate.before(startDate) && !evaluationDate.after(endDate)) {
	        if (writtenEvaluation instanceof Exam) {
	            final Exam exam = (Exam) writtenEvaluation;
	            infoShowOccupations.add(InfoExam.newInfoFromDomain(exam));
	        } else if (writtenEvaluation instanceof WrittenTest) {
	            final WrittenTest writtenTest = (WrittenTest) writtenEvaluation;
	            infoShowOccupations.add(InfoWrittenTest.newInfoFromDomain(writtenTest));
	        }
	    }                
	}
    }

    private void getGenericEventRoomOccupations(List<InfoObject> infoShowOccupations, Calendar startDay,
	    Calendar endDay, final YearMonthDay weekStartYearMonthDay,
	    final YearMonthDay weekEndYearMonthDay, final GenericEvent genericEvent) {
	
	if (genericEvent != null) {
	    if (genericEvent.getOccupationPeriod().intersectPeriods(startDay, endDay)) {

		List<Interval> genericEventIntervals = genericEvent.getGenericEventIntervals(weekStartYearMonthDay, weekEndYearMonthDay);
		TimeOfDay eightAM = new TimeOfDay(8, 0, 0, 0);

		for (Interval interval : genericEventIntervals) {

		    DateTime pointer = interval.getStart();
		    YearMonthDay intervalStartDay = interval.getStart().toYearMonthDay();
		    YearMonthDay intervalEndDay = interval.getEnd().toYearMonthDay();

		    if (interval.getStart().getHourOfDay() < 8) {
			pointer = intervalStartDay.toDateTime(eightAM);
		    }

		    if (!intervalStartDay.isEqual(intervalEndDay)) {

			if (intervalStartDay.isBefore(weekStartYearMonthDay)) {
			    pointer = weekStartYearMonthDay.toDateTime(eightAM);
			    intervalStartDay = pointer.toYearMonthDay();
			}

			while (!intervalStartDay.isAfter(weekEndYearMonthDay) && !intervalStartDay.isAfter(intervalEndDay)) {

			    Calendar endOfInterval = null;
			    if (intervalStartDay.isEqual(intervalEndDay)) {
				endOfInterval = interval.getEnd().toCalendar(LanguageUtils.getLocale());
			    } else {
				endOfInterval = pointer.toCalendar(LanguageUtils.getLocale());
				endOfInterval.set(Calendar.HOUR_OF_DAY, 23);
				endOfInterval.set(Calendar.MINUTE, 29);
				endOfInterval.set(Calendar.SECOND, 0);
				endOfInterval.set(Calendar.MILLISECOND, 0);
			    }

			    infoShowOccupations.add(new InfoGenericEvent(genericEvent, DiaSemana.getDiaSemana(pointer), pointer
				    .toCalendar(LanguageUtils.getLocale()), endOfInterval));

			    pointer = pointer.plusDays(1);
			    intervalStartDay = pointer.toYearMonthDay();
			    pointer.withHourOfDay(8);
			    pointer.withMinuteOfHour(0);
			    pointer.withSecondOfMinute(0);
			    pointer.withMillisOfSecond(0);
			}

		    } else {
			infoShowOccupations.add(new InfoGenericEvent(genericEvent, DiaSemana.getDiaSemana(interval.getStart())));
		    }
		}
	    }
	}
    }

    private boolean intersectPeriods(Calendar startDate, Calendar endDate, InfoPeriod secondPeriod) {
        while (secondPeriod != null) {
            if (CalendarUtil.intersectDates(startDate, endDate, secondPeriod
                    .getStartDate(), secondPeriod.getEndDate())) {
                return true;
            }
            secondPeriod = secondPeriod.getNextPeriod();
        }
        return false;
    }

    private InfoPeriod calculateLessonsSeason(ExecutionPeriod executionPeriod) throws FenixServiceException {
            int semester = executionPeriod.getSemester().intValue();

            String year = executionPeriod.getExecutionYear().getYear();
            List<ExecutionDegree> executionDegreesList = ExecutionDegree.getAllByExecutionYear(year);
            ExecutionDegree executionDegree = executionDegreesList.get(0);

            Calendar startSeason1 = null;
            Calendar endSeason2 = null;
            if (semester == 1) {
                startSeason1 = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
            } else {
                startSeason1 = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                endSeason2 = executionDegree.getPeriodLessonsSecondSemester().getEndDateOfComposite();
            }

            for (int i = 1; i < executionDegreesList.size(); i++) {
                executionDegree = (ExecutionDegree) executionDegreesList.get(i);
                Calendar startLessons;
                Calendar endLessons;
                if (semester == 1) {
                    startLessons = executionDegree.getPeriodLessonsFirstSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsFirstSemester().getEndDateOfComposite();
                } else {
                    startLessons = executionDegree.getPeriodLessonsSecondSemester().getStartDate();
                    endLessons = executionDegree.getPeriodLessonsSecondSemester()
                            .getEndDateOfComposite();
                }
                if (startLessons.before(startSeason1)) {
                    startSeason1 = startLessons;
                }
                if (endLessons.after(endSeason2)) {
                    endSeason2 = endLessons;
                }

            }
            return new InfoPeriod(startSeason1, endSeason2);
    }

}