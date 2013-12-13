/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.GenericEvent;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.resource.ResourceAllocation;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.GenericEventSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * @author Jo�o Mota
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
            ExecutionSemester executionSemester) throws Exception {

        if (component instanceof InfoSiteRoomTimeTable) {
            return getInfoSiteRoomTimeTable((InfoSiteRoomTimeTable) component, day, room, executionSemester);
        }

        return null;
    }

    // private boolean isCurrentUserRoomManager(AllocatableSpace room) {
    // User view = (User) Authenticate.getUser();
    // Person person = view == null ? null : view.getPerson();
    // return person != null ? room.isActiveManager(person) : false;
    // }

    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day, AllocatableSpace room,
            ExecutionSemester executionSemester) throws Exception {

        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

        Calendar startDay = Calendar.getInstance();
        startDay.setTimeInMillis(day.getTimeInMillis());
        startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));

        Calendar endDay = Calendar.getInstance();
        endDay.setTimeInMillis(startDay.getTimeInMillis());
        endDay.add(Calendar.DATE, 6);

        // final boolean isCurrentUserRoomManager =
        // isCurrentUserRoomManager(room);

        final YearMonthDay weekStartYearMonthDay = YearMonthDay.fromCalendarFields(startDay);
        final YearMonthDay weekEndYearMonthDay = YearMonthDay.fromCalendarFields(endDay).minusDays(1);

        for (final ResourceAllocation roomOccupation : room.getResourceAllocationsSet()) {

            if (roomOccupation.isWrittenEvaluationSpaceOccupation()) {
                Collection<WrittenEvaluation> writtenEvaluations =
                        ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                getWrittenEvaluationRoomOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay,
                        writtenEvaluations);
            }

            if (/* isCurrentUserRoomManager && */roomOccupation.isGenericEventSpaceOccupation()) {
                final GenericEvent genericEvent = ((GenericEventSpaceOccupation) roomOccupation).getGenericEvent();
                ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom.getGenericEventRoomOccupations(infoShowOccupations,
                        weekStartYearMonthDay, weekEndYearMonthDay, genericEvent);
            }

            if (roomOccupation.isLessonSpaceOccupation()) {
                final Lesson lesson = ((LessonSpaceOccupation) roomOccupation).getLesson();
                getLessonOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lesson);
            }

            if (roomOccupation.isLessonInstanceSpaceOccupation()) {
                Collection<LessonInstance> lessonInstances =
                        ((LessonInstanceSpaceOccupation) roomOccupation).getLessonInstancesSet();
                getLessonInstanceOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lessonInstances);
            }
        }

        component.setInfoShowOccupation(infoShowOccupations);
        component.setInfoRoom(InfoRoom.newInfoFromDomain(room));

        return component;
    }

    private void getLessonOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
            YearMonthDay weekEndYearMonthDay, Lesson lesson) {

        if (lesson != null
                && lesson.hasShift()
                && lesson.containsWithoutCheckInstanceDates(new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(),
                        weekEndYearMonthDay.toDateTimeAtMidnight()))) {
            infoShowOccupations.add(InfoLesson.newInfoFromDomain(lesson));
        }
    }

    private void getLessonInstanceOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
            YearMonthDay weekEndYearMonthDay, Collection<LessonInstance> lessonInstances) {

        if (lessonInstances != null) {
            for (LessonInstance lessonInstance : lessonInstances) {
                final YearMonthDay lessonInstanceDay = lessonInstance.getDay();
                if (!lessonInstanceDay.isBefore(weekStartYearMonthDay) && !lessonInstanceDay.isAfter(weekEndYearMonthDay)) {
                    InfoLessonInstance infoLessonInstance = new InfoLessonInstance(lessonInstance);
                    infoShowOccupations.add(infoLessonInstance);
                }
            }
        }
    }

    private void getWrittenEvaluationRoomOccupations(List<InfoObject> infoShowOccupations,
            final YearMonthDay weekStartYearMonthDay, final YearMonthDay weekEndYearMonthDay,
            final Collection<WrittenEvaluation> writtenEvaluations) {

        if (writtenEvaluations != null) {

            for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {

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
}