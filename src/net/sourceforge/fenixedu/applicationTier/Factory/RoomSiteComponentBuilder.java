/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

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

    public ISiteComponent getComponent(ISiteComponent component, Calendar day, AllocatableSpace room,
	    ExecutionPeriod executionPeriod) throws Exception {

	if (component instanceof InfoSiteRoomTimeTable) {
	    return getInfoSiteRoomTimeTable((InfoSiteRoomTimeTable) component, day, room,
		    executionPeriod);
	}

	return null;
    }
    
    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day,
	    AllocatableSpace room, ExecutionPeriod executionPeriod) throws Exception {

	List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

	Calendar startDay = Calendar.getInstance();
	startDay.setTimeInMillis(day.getTimeInMillis());
	startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
	
	Calendar endDay = Calendar.getInstance();
	endDay.setTimeInMillis(startDay.getTimeInMillis());
	endDay.add(Calendar.DATE, 6);
	
	final YearMonthDay weekStartYearMonthDay = YearMonthDay.fromCalendarFields(startDay);
	final YearMonthDay weekEndYearMonthDay = YearMonthDay.fromCalendarFields(endDay).minusDays(1);

	for (final ResourceAllocation roomOccupation : room.getResourceAllocations()) {
	    	   
	    if(roomOccupation.isWrittenEvaluationSpaceOccupation()) {                
		final WrittenEvaluation writtenEvaluation = ((WrittenEvaluationSpaceOccupation)roomOccupation).getWrittenEvaluation();                                   
		getWrittenEvaluationRoomOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, writtenEvaluation);
	    }
	    
	    if(roomOccupation.isLessonSpaceOccupation()) {
		final Lesson lesson = ((LessonSpaceOccupation)roomOccupation).getLesson();
		getLessonOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lesson);
	    }

	    if(roomOccupation.isLessonInstanceSpaceOccupation()) {
		final LessonInstance lessonInstance = ((LessonInstanceSpaceOccupation)roomOccupation).getLessonInstance();
		getLessonInstanceOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lessonInstance);
	    }
	}

	component.setInfoShowOccupation(infoShowOccupations);
	component.setInfoRoom(InfoRoom.newInfoFromDomain(room));

	return component;
    }
    
    private void getLessonOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay, 
	    YearMonthDay weekEndYearMonthDay, Lesson lesson) {

	if(lesson != null && lesson.hasShift() 
		&& lesson.containsWithoutCheckInstanceDates(new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(),
			weekEndYearMonthDay.toDateTimeAtMidnight()))) {	    	   
	    infoShowOccupations.add(InfoLesson.newInfoFromDomain(lesson));
	}	
    }

    private void getLessonInstanceOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
	    YearMonthDay weekEndYearMonthDay, LessonInstance lessonInstance) {

	if(lessonInstance != null) {
	    final YearMonthDay lessonInstanceDay = lessonInstance.getDay();	    
	    if (!lessonInstanceDay.isBefore(weekStartYearMonthDay) && !lessonInstanceDay.isAfter(weekEndYearMonthDay)) {		
		InfoLessonInstance infoLessonInstance = new InfoLessonInstance(lessonInstance);
		infoShowOccupations.add(infoLessonInstance);		
	    }		 	
	}
    }

    private void getWrittenEvaluationRoomOccupations(List<InfoObject> infoShowOccupations, final YearMonthDay weekStartYearMonthDay,
	    final YearMonthDay weekEndYearMonthDay, final WrittenEvaluation writtenEvaluation) {

	if (writtenEvaluation != null) {

	    final YearMonthDay evaluationDate = writtenEvaluation.getDayDateYearMonthDay();

	    if (!evaluationDate.isBefore(weekStartYearMonthDay) && !evaluationDate.isAfter(weekEndYearMonthDay)) {

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
}