/*
 * Created on 19 de Julho de 2004, 15:30
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.dataTransferObject.spaceManager.ViewEventSpaceOccupationsBean;
import net.sourceforge.fenixedu.domain.Exam;
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
import net.sourceforge.fenixedu.util.DiaSemana;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ReadLessonsExamsAndPunctualRoomsOccupationsInWeekAndRoom {

    @Atomic
    public static List<InfoObject> run(AllocatableSpace room, YearMonthDay day) throws FenixServiceException {

        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();

        final YearMonthDay weekStartYearMonthDay =
                day.toDateTimeAtMidnight().withDayOfWeek(ViewEventSpaceOccupationsBean.MONDAY_IN_JODA_TIME).toYearMonthDay();
        final YearMonthDay weekEndYearMonthDay =
                day.toDateTimeAtMidnight().withDayOfWeek(ViewEventSpaceOccupationsBean.SATURDAY_IN_JODA_TIME).toYearMonthDay();

        for (final ResourceAllocation roomOccupation : room.getResourceAllocationsSet()) {

            if (roomOccupation.isWrittenEvaluationSpaceOccupation()) {
                Collection<WrittenEvaluation> writtenEvaluations =
                        ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                getWrittenEvaluationRoomOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay,
                        writtenEvaluations);
            }

            if (roomOccupation.isGenericEventSpaceOccupation()) {
                final GenericEvent genericEvent = ((GenericEventSpaceOccupation) roomOccupation).getGenericEvent();
                getGenericEventRoomOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, genericEvent);
            }

            if (roomOccupation.isLessonSpaceOccupation()) {
                final Lesson lesson = ((LessonSpaceOccupation) roomOccupation).getLesson();
                getLessonOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay.plusDays(1), lesson);
            }

            if (roomOccupation.isLessonInstanceSpaceOccupation()) {
                Collection<LessonInstance> lessonInstances =
                        ((LessonInstanceSpaceOccupation) roomOccupation).getLessonInstancesSet();
                getLessonInstanceOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lessonInstances);
            }
        }

        return infoShowOccupations;
    }

    private static void getLessonOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
            YearMonthDay weekEndYearMonthDay, Lesson lesson) {

        if (lesson != null
                && lesson.hasShift()
                && lesson.containsWithoutCheckInstanceDates(new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(),
                        weekEndYearMonthDay.toDateTimeAtMidnight()))) {
            infoShowOccupations.add(InfoLesson.newInfoFromDomain(lesson));
        }
    }

    private static void getLessonInstanceOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
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

    private static void getWrittenEvaluationRoomOccupations(List<InfoObject> infoShowOccupations,
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

    public static void getGenericEventRoomOccupations(List<InfoObject> infoShowOccupations,
            final YearMonthDay weekStartYearMonthDay, final YearMonthDay weekEndYearMonthDay, final GenericEvent genericEvent) {

        if (genericEvent != null) {

            if (genericEvent.intersects(weekStartYearMonthDay, weekEndYearMonthDay)) {

                List<Interval> genericEventIntervals =
                        genericEvent.getGenericEventIntervals(weekStartYearMonthDay, weekEndYearMonthDay);
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
                                endOfInterval = interval.getEnd().toCalendar(Language.getLocale());

                            } else {
                                endOfInterval = pointer.toCalendar(Language.getLocale());
                                endOfInterval.set(Calendar.HOUR_OF_DAY, 23);
                                endOfInterval.set(Calendar.MINUTE, 59);
                                endOfInterval.set(Calendar.SECOND, 59);
                                endOfInterval.set(Calendar.MILLISECOND, 0);
                            }

                            infoShowOccupations.add(new InfoGenericEvent(genericEvent, DiaSemana.getDiaSemana(pointer), pointer
                                    .toCalendar(Language.getLocale()), endOfInterval));

                            pointer = pointer.plusDays(1);
                            intervalStartDay = pointer.toYearMonthDay();
                            pointer = pointer.withHourOfDay(8);
                            pointer = pointer.withMinuteOfHour(0);
                            pointer = pointer.withSecondOfMinute(0);
                            pointer = pointer.withMillisOfSecond(0);
                        }

                    } else {
                        infoShowOccupations.add(new InfoGenericEvent(genericEvent, DiaSemana.getDiaSemana(interval.getStart())));
                    }
                }
            }
        }
    }
}