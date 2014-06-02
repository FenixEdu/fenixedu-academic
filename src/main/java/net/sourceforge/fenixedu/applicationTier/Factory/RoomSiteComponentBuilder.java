/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRoomTimeTable;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonInstance;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.space.LessonInstanceSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.LessonSpaceOccupation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
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

    public ISiteComponent getComponent(ISiteComponent component, Calendar day, Space room, ExecutionSemester executionSemester)
            throws Exception {

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

    private ISiteComponent getInfoSiteRoomTimeTable(InfoSiteRoomTimeTable component, Calendar day, Space room,
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

        final Interval search =
                new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(), weekEndYearMonthDay.toDateTimeAtMidnight());

        for (final Occupation roomOccupation : room.getOccupationSet()) {

            if (roomOccupation instanceof WrittenEvaluationSpaceOccupation) {
                Collection<WrittenEvaluation> writtenEvaluations =
                        ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                getWrittenEvaluationRoomOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay,
                        writtenEvaluations);
            } else if (roomOccupation instanceof LessonSpaceOccupation) {
                final Lesson lesson = ((LessonSpaceOccupation) roomOccupation).getLesson();
                getLessonOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lesson);
            } else if (roomOccupation instanceof LessonInstanceSpaceOccupation) {
                Collection<LessonInstance> lessonInstances =
                        ((LessonInstanceSpaceOccupation) roomOccupation).getLessonInstancesSet();
                getLessonInstanceOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lessonInstances);
            } else {
                for (Interval interval : roomOccupation.getIntervals()) {
                    if (search.overlaps(interval)) {
                        infoShowOccupations.add(new InfoOccupation(roomOccupation, interval));
                    }
                }
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