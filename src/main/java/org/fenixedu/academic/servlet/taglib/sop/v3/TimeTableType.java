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
package org.fenixedu.academic.servlet.taglib.sop.v3;

/**
 * @author dcs-rjao at 13/Fev/2003
 */
public abstract class TimeTableType {

    public static final int CLASS_TIMETABLE = 1;

    public static final int SHIFT_TIMETABLE = 2;

    public static final int ROOM_TIMETABLE = 3;

    public static final int EXECUTION_COURSE_TIMETABLE = 4;

    public static final int SOP_CLASS_TIMETABLE = 5;

    public static final int SOP_ROOM_TIMETABLE = 6;

    public static final int CLASS_TIMETABLE_WITHOUT_LINKS = 7;

    public static final int SOP_CLASS_ROOM_TIMETABLE = 8;

    public static final int SHIFT_ENROLLMENT_TIMETABLE = 9;

    public static final int SPACE_MANAGER_TIMETABLE = 10;
}