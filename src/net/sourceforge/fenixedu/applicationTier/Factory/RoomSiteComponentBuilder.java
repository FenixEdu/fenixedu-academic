/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.OccupationPeriod;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.util.CalendarUtil;
import net.sourceforge.fenixedu.util.DateFormatUtil;

/**
 * @author João Mota
 * 
 * 
 */
public class RoomSiteComponentBuilder {

    private static RoomSiteComponentBuilder instance = null;

    public RoomSiteComponentBuilder() {
    }

    public static RoomSiteComponentBuilder getInstance() {
	if (instance == null) {
	    instance = new RoomSiteComponentBuilder();
	}
	return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, Calendar day, OldRoom room,
	    ExecutionPeriod executionPeriod) throws Exception {

	if (component instanceof InfoSiteRoomTimeTable) {
	    return getInfoSiteRoomTimeTable((InfoSiteRoomTimeTable) component, day, room,
		    executionPeriod);
	}

	return null;

    }

    // TODO (rspl): alterar as aulas a ler e o dia da semana
    // FIXME duplicated code: this method is (almost?) identical to
        // ReadLessonsAndExamsInWeekAndRoom.run
    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day,
	    OldRoom room, ExecutionPeriod executionPeriod) throws Exception {

	List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

	Calendar startDay = Calendar.getInstance();
	startDay.setTimeInMillis(day.getTimeInMillis());
	startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
	Calendar endDay = Calendar.getInstance();
	endDay.setTimeInMillis(startDay.getTimeInMillis());
	endDay.add(Calendar.DATE, 6);

	InfoPeriod lessonsInfoPeriod = calculateLessonsSeason(executionPeriod);
	if (this.intersectPeriods(day, endDay, lessonsInfoPeriod)) {
	    // adicionar as aulas
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
			    && (!RoomOccupation.periodQuinzenalContainsWeekPeriod(period.getStartDate(),
				    period.getEndDate(), roomOccupation.getWeekOfQuinzenalStart()
					    .intValue(), roomOccupation.getDayOfWeek(), day, endDay,
				    null))) {
			add = false;
		    }
		    if (roomOccupation.getFrequency().intValue() == RoomOccupation.SEMANAL) {
			Calendar dayOfLesson = Calendar.getInstance();
			dayOfLesson.setTimeInMillis(day.getTimeInMillis());
			dayOfLesson.add(Calendar.DATE, roomOccupation.getDayOfWeek().getDiaSemana()
				.intValue()
				- Calendar.MONDAY);
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
	for (final RoomOccupation roomOccupation : room.getRoomOccupations()) {
	    final WrittenEvaluation writtenEvaluation = roomOccupation.getWrittenEvaluation();
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

	component.setInfoShowOccupation(infoShowOccupations);
	component.setInfoRoom(InfoRoom.newInfoFromDomain(room));

	return component;
    }

    private boolean intersectPeriods(Calendar startDate, Calendar endDate, InfoPeriod secondPeriod) {
	while (secondPeriod != null) {
	    if (CalendarUtil.intersectDates(startDate, endDate, secondPeriod.getStartDate(),
		    secondPeriod.getEndDate())) {
		return true;
	    }
	    secondPeriod = secondPeriod.getNextPeriod();
	}
	return false;
    }

    private InfoPeriod calculateLessonsSeason(ExecutionPeriod executionPeriod) throws Exception {
	int semester = executionPeriod.getSemester().intValue();

	List executionDegreesList = ExecutionDegree.getAllByExecutionYear(executionPeriod
		.getExecutionYear().getYear());
	ExecutionDegree executionDegree = (ExecutionDegree) executionDegreesList.get(0);

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
		endLessons = executionDegree.getPeriodLessonsSecondSemester().getEndDateOfComposite();
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