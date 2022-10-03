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
 * Created on 21/Jul/2003
 *
 * 
 */
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.HourMinuteSecond;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

/**
 * @author Manuel Pinto
 * 
 */
public class Summary extends Summary_Base {
    public static final String CREATE_SIGNAL = "academic.summary.create.signal";
    public static final String EDIT_SIGNAL = "academic.summary.edit.signal";

    public static final Comparator<Summary> COMPARATOR_BY_DATE_AND_HOUR = new Comparator<Summary>() {
        @Override
        public int compare(final Summary o1, final Summary o2) {
            final int c1 = o2.getSummaryDateYearMonthDay().compareTo(o1.getSummaryDateYearMonthDay());
            if (c1 == 0) {
                final int c2 = o2.getSummaryHourHourMinuteSecond().compareTo(o1.getSummaryHourHourMinuteSecond());
                return c2 == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c2;
            } else {
                return c1;
            }
        }
    };

    public static final Comparator<Summary> COMPARATOR_BY_DATE_AND_HOUR_ASC = new Comparator<Summary>() {
        @Override
        public int compare(final Summary o1, final Summary o2) {
            final int c1 = o2.getSummaryDateYearMonthDay().compareTo(o1.getSummaryDateYearMonthDay());
            if (c1 == 0) {
                final int c2 = o2.getSummaryHourHourMinuteSecond().compareTo(o1.getSummaryHourHourMinuteSecond());
                return -1 * (c2 == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c2);
            } else {
                return -1 * c1;
            }
        }
    };

    public Summary(LocalizedString title, LocalizedString summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Professorship professorship, String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
            Space room, Partial hour, ShiftType type, Boolean taught) {

        super();
        setRootDomainObject(Bennu.getInstance());
        fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName, teacher, shift,
                lesson, date, room, hour, type, taught);

        ContentManagementLog.createLog(shift.getExecutionCourse(), Bundle.MESSAGING, "log.executionCourse.content.summary.added",
                title.getContent(), shift.getPresentationName(), shift.getExecutionCourse().getNome(), shift.getExecutionCourse()
                        .getDegreePresentationString());
    }

    public void edit(LocalizedString title, LocalizedString summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Professorship professorship, String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
            Space room, Partial hour, ShiftType type, Boolean taught) {

        fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName, teacher, shift,
                lesson, date, room, hour, type, taught);

        ContentManagementLog.createLog(shift.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.summary.edited", title.getContent(), shift.getPresentationName(), shift
                        .getExecutionCourse().getNome(), shift.getExecutionCourse().getDegreePresentationString());

        Signal.emit(EDIT_SIGNAL, new DomainObjectEvent<Summary>(this));
    }

    private void fillSummaryWithInfo(LocalizedString title, LocalizedString summaryText, Integer studentsNumber,
            Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher, Shift shift, Lesson lesson,
            YearMonthDay day, Space room, Partial hour, ShiftType type, Boolean taught) {

        setShift(shift);
        setSummaryDateYearMonthDay(day);
        setExecutionCourse(shift.getExecutionCourse());
        setTitle(title);
        setSummaryText(summaryText);
        setIsExtraLesson(isExtraLesson);

        checkSpecialParameters(isExtraLesson, professorship, teacherName, teacher, lesson, hour, type);
        checkIfInternalTeacherHasProfessorhipInExecutionCourse(teacher, shift.getExecutionCourse());
        checkIfSummaryDateIsValid(day, shift.getExecutionPeriod(), lesson, isExtraLesson);

        setStudentsNumber(studentsNumber);
        setProfessorship(professorship);
        setTeacherName(teacherName);
        setTeacher(teacher);
        setLastModifiedDateDateTime(new DateTime());
        setSummaryType(type);
        setTaught(taught);

        if (isExtraLesson) {
            super.setLessonInstance(null);
            setRoom(room);
            HourMinuteSecond hourMinuteSecond =
                    new HourMinuteSecond(hour.get(DateTimeFieldType.hourOfDay()), hour.get(DateTimeFieldType.minuteOfHour()), 0);
            setSummaryHourHourMinuteSecond(hourMinuteSecond);
        } else {
            setRoom(lesson.getSala());
            setSummaryHourHourMinuteSecond(lesson.getBeginHourMinuteSecond());
            lessonInstanceManagement(lesson, day, lesson.getSala());
            if (getLessonInstance() == null) {
                throw new DomainException("error.Summary.empty.LessonInstances");
            }
        }
    }

    public void delete() {

        ContentManagementLog.createLog(getShift().getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.summary.removed", getTitle().getContent(), getShift().getPresentationName(),
                getShift().getExecutionCourse().getNome(), getShift().getExecutionCourse().getDegreePresentationString());

        super.setExecutionCourse(null);
        super.setShift(null);
        super.setLessonInstance(null);
        setRoom(null);
        setProfessorship(null);
        setTeacher(null);
        setRootDomainObject(null);
        deleteDomainObject();

    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
        return getTitle() != null && !getTitle().isEmpty() && getSummaryText() != null && !getSummaryText().isEmpty()
                && getSummaryDateYearMonthDay() != null && getSummaryHourHourMinuteSecond() != null && getIsExtraLesson() != null;
    }

    private void lessonInstanceManagement(Lesson lesson, YearMonthDay day, Space room) {
        LessonInstance lessonInstance = lesson.getLessonInstanceFor(day);
        if (lessonInstance == null) {
            new LessonInstance(this, lesson);
        } else {
            lessonInstance.setSummary(this);
        }
    }

    public Lesson getLesson() {
        return getLessonInstance() != null ? getLessonInstance().getLesson() : null;
    }

    @Override
    public void setSummaryHourHourMinuteSecond(HourMinuteSecond summaryHourHourMinuteSecond) {
        if (summaryHourHourMinuteSecond == null) {
            throw new DomainException("error.Summary.empty.time");
        }
        super.setSummaryHourHourMinuteSecond(summaryHourHourMinuteSecond);
    }

    @Override
    public void setIsExtraLesson(Boolean isExtraLesson) {
        if (isExtraLesson == null) {
            throw new DomainException("error.summary.no.type");
        }
        super.setIsExtraLesson(isExtraLesson);
    }

    @Override
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        if (executionCourse == null) {
            throw new DomainException("error.summary.no.executionCourse");
        }
        super.setExecutionCourse(executionCourse);
    }

    @Override
    public void setSummaryDateYearMonthDay(YearMonthDay summaryDateYearMonthDay) {
        if (summaryDateYearMonthDay == null) {
            throw new DomainException("error.summary.no.date");
        }
        super.setSummaryDateYearMonthDay(summaryDateYearMonthDay);
    }

    @Override
    public void setTitle(LocalizedString title) {
        if (title == null || title.getLocales().isEmpty()) {
            throw new DomainException("error.summary.no.title");
        }
        super.setTitle(title);
    }

    @Override
    public void setSummaryText(LocalizedString summaryText) {
        if (summaryText == null || summaryText.getLocales().isEmpty()) {
            throw new DomainException("error.summary.no.summaryText");
        }
        super.setSummaryText(summaryText);
    }

    @Override
    public void setLessonInstance(LessonInstance lessonInstance) {
        if (lessonInstance == null) {
            throw new DomainException("error.Summary.empty.lessonInstance");
        }
        super.setLessonInstance(lessonInstance);
    }

    @Override
    public void setShift(Shift shift) {
        if (shift == null) {
            throw new DomainException("error.summary.no.shift");
        }
        super.setShift(shift);
    }

    private void checkIfSummaryDateIsValid(YearMonthDay date, ExecutionInterval executionSemester, Lesson lesson,
            Boolean isExtraLesson) {
        if (!isExtraLesson) {
            Summary summary = lesson.getSummaryByDate(date);
            if (summary != null && !summary.equals(this)) {
                throw new DomainException("error.summary.already.exists");
            }
            if (!lesson.isDateValidToInsertSummary(date)) {
                throw new DomainException("error.summary.no.valid.date.to.lesson");
            }
            if (!lesson.isTimeValidToInsertSummary(new HourMinuteSecond(), date)) {
                throw new DomainException("error.summary.no.valid.time.to.lesson");
            }
        } else if (!executionSemester.getAcademicInterval().contains(date.toDateTimeAtCurrentTime())) {
            throw new DomainException("error.summary.no.valid.date");
        }
    }

    private void checkIfInternalTeacherHasProfessorhipInExecutionCourse(Teacher teacher, ExecutionCourse executionCourse) {
        if (teacher != null && teacher.getProfessorshipByExecutionCourse(executionCourse) != null) {
            throw new DomainException("error.summary.teacher.is.executionCourse.professorship");
        }
    }

    private void checkSpecialParameters(Boolean isExtraLesson, Professorship professorship, String teacherName, Teacher teacher,
            Lesson lesson, Partial hour, ShiftType type) {

        if (professorship == null && StringUtils.isEmpty(teacherName) && teacher == null) {
            throw new DomainException("error.summary.no.teacher");
        }
        if (isExtraLesson) {
            if (hour == null) {
                throw new DomainException("error.summary.no.hour");
            }
        } else {
            if (lesson == null) {
                throw new DomainException("error.summary.no.lesson");
            }
            if (type == null) {
                throw new DomainException("error.summary.no.shifType");
            }
        }
    }

    public String getOrder() {
        StringBuilder stringBuilder = new StringBuilder();
        Lesson lesson = getLesson();
        if (lesson != null) {
            SortedSet<YearMonthDay> allLessonDates = lesson.getAllLessonDates();
            List<YearMonthDay> lessonDates = new ArrayList<YearMonthDay>(allLessonDates);
            if (!lessonDates.isEmpty()) {
                int index = lessonDates.indexOf(getSummaryDateYearMonthDay());
                if (index != -1) {
                    stringBuilder.append("(").append(index + 1).append("/");
                    return stringBuilder.append(lessonDates.size()).append(")").toString();
                }
            }
        }
        return "";
    }

    @Override
    public Space getRoom() {
        if (isExtraSummary()) {
            return super.getRoom();
        } else if (getLessonInstance() != null) {
            return getLessonInstance().getRoom();
        }
        return null;
    }

    public void moveFromTeacherToProfessorship(Professorship professorship) {
        if (getTeacher() != null && professorship != null && professorship.getExecutionCourse().equals(getExecutionCourse())
                && professorship.getTeacher().equals(getTeacher())) {

            setTeacher(null);
            setProfessorship(professorship);
        }
    }

//    public ShiftType getShiftType() {
//        return getLessonInstance().getCourseLoad().getType();
//    }

    public boolean isExtraSummary() {
        return getIsExtraLesson().booleanValue();
    }

    public DateTime getSummaryDateTime() {
        HourMinuteSecond time = getSummaryHourHourMinuteSecond();
        return getSummaryDateYearMonthDay().toDateTime(
                new TimeOfDay(time.getHour(), time.getMinuteOfHour(), time.getSecondOfMinute(), 0));
    }

    @Deprecated
    public java.util.Date getLastModifiedDate() {
        org.joda.time.DateTime dt = getLastModifiedDateDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModifiedDate(java.util.Date date) {
        if (date == null) {
            setLastModifiedDateDateTime(null);
        } else {
            setLastModifiedDateDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Date getSummaryDate() {
        org.joda.time.YearMonthDay ymd = getSummaryDateYearMonthDay();
        return (ymd == null) ? null : new java.util.Date(ymd.getYear() - 1900, ymd.getMonthOfYear() - 1, ymd.getDayOfMonth());
    }

    @Deprecated
    public void setSummaryDate(java.util.Date date) {
        if (date == null) {
            setSummaryDateYearMonthDay(null);
        } else {
            setSummaryDateYearMonthDay(org.joda.time.YearMonthDay.fromDateFields(date));
        }
    }

    @Deprecated
    public java.util.Date getSummaryHour() {
        org.fenixedu.academic.util.HourMinuteSecond hms = getSummaryHourHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setSummaryHour(java.util.Date date) {
        if (date == null) {
            setSummaryHourHourMinuteSecond(null);
        } else {
            setSummaryHourHourMinuteSecond(org.fenixedu.academic.util.HourMinuteSecond.fromDateFields(date));
        }
    }

}
