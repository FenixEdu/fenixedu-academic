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

/**
 * @author Jo�o Mota
 *
 *
 */
//TODO: DELETE
@Deprecated
public class RoomSiteComponentBuilder {

//    public static InfoSiteRoomTimeTable getInfoSiteRoomTimeTable(Calendar day, Space room, ExecutionInterval executionSemester) {
//
//        List<InfoObject> infoShowOccupations = new ArrayList<InfoObject>();
//
//        Calendar startDay = Calendar.getInstance();
//        startDay.setTimeInMillis(day.getTimeInMillis());
//        startDay.add(Calendar.DATE, Calendar.MONDAY - day.get(Calendar.DAY_OF_WEEK));
//
//        Calendar endDay = Calendar.getInstance();
//        endDay.setTimeInMillis(startDay.getTimeInMillis());
//        endDay.add(Calendar.DATE, 6);
//
//        // final boolean isCurrentUserRoomManager =
//        // isCurrentUserRoomManager(room);
//
//        final YearMonthDay weekStartYearMonthDay = YearMonthDay.fromCalendarFields(startDay);
//        final YearMonthDay weekEndYearMonthDay = YearMonthDay.fromCalendarFields(endDay);
//
//        final Interval search =
//                new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(), weekEndYearMonthDay.toDateTimeAtMidnight());
//
//        for (final Occupation roomOccupation : room.getOccupationSet()) {
//
//            if (roomOccupation instanceof LessonSpaceOccupation) {
//                final Lesson lesson = ((LessonSpaceOccupation) roomOccupation).getLesson();
//                getLessonOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lesson);
//            } else if (roomOccupation instanceof LessonInstanceSpaceOccupation) {
//                Collection<LessonInstance> lessonInstances =
//                        ((LessonInstanceSpaceOccupation) roomOccupation).getLessonInstancesSet();
//                getLessonInstanceOccupations(infoShowOccupations, weekStartYearMonthDay, weekEndYearMonthDay, lessonInstances);
//            } else {
//                for (Interval interval : roomOccupation.getIntervals()) {
//                    if (search.overlaps(interval)) {
//                        infoShowOccupations.add(new InfoOccupation(roomOccupation, interval));
//                    }
//                }
//            }
//        }
//        InfoSiteRoomTimeTable component = new InfoSiteRoomTimeTable();
//
//        component.setInfoShowOccupation(infoShowOccupations);
//        component.setInfoRoom(InfoRoom.newInfoFromDomain(room));
//
//        return component;
//    }
//
//    private static void getLessonOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
//            YearMonthDay weekEndYearMonthDay, Lesson lesson) {
//
//        if (lesson != null && lesson.getShift() != null && lesson.containsWithoutCheckInstanceDates(
//                new Interval(weekStartYearMonthDay.toDateTimeAtMidnight(), weekEndYearMonthDay.toDateTimeAtMidnight()))) {
//            infoShowOccupations.add(InfoLesson.newInfoFromDomain(lesson));
//        }
//    }
//
//    private static void getLessonInstanceOccupations(List<InfoObject> infoShowOccupations, YearMonthDay weekStartYearMonthDay,
//            YearMonthDay weekEndYearMonthDay, Collection<LessonInstance> lessonInstances) {
//
//        if (lessonInstances != null) {
//            for (LessonInstance lessonInstance : lessonInstances) {
//                final YearMonthDay lessonInstanceDay = lessonInstance.getDay();
//                if (!lessonInstanceDay.isBefore(weekStartYearMonthDay) && !lessonInstanceDay.isAfter(weekEndYearMonthDay)) {
//                    InfoLessonInstance infoLessonInstance = new InfoLessonInstance(lessonInstance);
//                    infoShowOccupations.add(infoLessonInstance);
//                }
//            }
//        }
//    }

}