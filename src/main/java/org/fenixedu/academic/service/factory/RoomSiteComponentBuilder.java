/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 5/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.fenixedu.academic.service.factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.domain.space.LessonInstanceSpaceOccupation;
import org.fenixedu.academic.domain.space.LessonSpaceOccupation;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.dto.InfoExam;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoObject;
import org.fenixedu.academic.dto.InfoOccupation;
import org.fenixedu.academic.dto.InfoRoom;
import org.fenixedu.academic.dto.InfoSiteRoomTimeTable;
import org.fenixedu.academic.dto.InfoWrittenTest;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

/**
 * @author Jo�o Mota
 *
 *
 */
@Deprecated
public class RoomSiteComponentBuilder {

    public static InfoSiteRoomTimeTable getInfoSiteRoomTimeTable(Calendar day, Space room, ExecutionSemester executionSemester) {

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
        final YearMonthDay weekEndYearMonthDay = YearMonthDay.fromCalendarFields(endDay);

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
        InfoSiteRoomTimeTable component = new InfoSiteRoomTimeTable();

        component.setInfoShowOccupation(infoShowOccupations);
        component.setInfoRoom(InfoRoom.newInfoFromDomain(room));

        return component;
    }

    private static void getLessonOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
            YearMonthDay weekEndYearMonthDay, Lesson lesson) {

        if (lesson != null
                && lesson.getShift() != null
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
                        if (exam.isExamsMapPublished()) {
                            infoShowOccupations.add(InfoExam.newInfoFromDomain(exam));
                        }
                    } else if (writtenEvaluation instanceof WrittenTest) {
                        final WrittenTest writtenTest = (WrittenTest) writtenEvaluation;
                        infoShowOccupations.add(InfoWrittenTest.newInfoFromDomain(writtenTest));
                    }
                }
            }
        }
    }
}