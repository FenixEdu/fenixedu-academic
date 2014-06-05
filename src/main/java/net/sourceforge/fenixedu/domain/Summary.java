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
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Manuel Pinto
 * 
 */
public class Summary extends Summary_Base {

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

    public Summary(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Professorship professorship, String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
            Space room, Partial hour, ShiftType type, Boolean taught) {

        super();
        setRootDomainObject(Bennu.getInstance());
        fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName, teacher, shift,
                lesson, date, room, hour, type, taught);

        ContentManagementLog.createLog(shift.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.summary.added", title.getContent(), shift.getPresentationName(), shift
                        .getExecutionCourse().getNome(), shift.getExecutionCourse().getDegreePresentationString());
    }

    public void edit(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber, Boolean isExtraLesson,
            Professorship professorship, String teacherName, Teacher teacher, Shift shift, Lesson lesson, YearMonthDay date,
            Space room, Partial hour, ShiftType type, Boolean taught) {

        fillSummaryWithInfo(title, summaryText, studentsNumber, isExtraLesson, professorship, teacherName, teacher, shift,
                lesson, date, room, hour, type, taught);

        ContentManagementLog.createLog(shift.getExecutionCourse(), Bundle.MESSAGING,
                "log.executionCourse.content.summary.edited", title.getContent(), shift.getPresentationName(), shift
                        .getExecutionCourse().getNome(), shift.getExecutionCourse().getDegreePresentationString());
    }

    private void fillSummaryWithInfo(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
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
            if (!hasLessonInstance()) {
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
            lessonInstance.summaryAndCourseLoadManagement(this, lesson);
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
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.getAllContents().isEmpty()) {
            throw new DomainException("error.summary.no.title");
        }
        super.setTitle(title);
    }

    @Override
    public void setSummaryText(MultiLanguageString summaryText) {
        if (summaryText == null || summaryText.getAllContents().isEmpty()) {
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

    private void checkIfSummaryDateIsValid(YearMonthDay date, ExecutionSemester executionSemester, Lesson lesson,
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
        } else if (!executionSemester.containsDay(date)) {
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
        } else if (hasLessonInstance()) {
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

    public ShiftType getShiftType() {
        return getLessonInstance().getCourseLoad().getType();
    }

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
        net.sourceforge.fenixedu.util.HourMinuteSecond hms = getSummaryHourHourMinuteSecond();
        return (hms == null) ? null : new java.util.Date(0, 0, 1, hms.getHour(), hms.getMinuteOfHour(), hms.getSecondOfMinute());
    }

    @Deprecated
    public void setSummaryHour(java.util.Date date) {
        if (date == null) {
            setSummaryHourHourMinuteSecond(null);
        } else {
            setSummaryHourHourMinuteSecond(net.sourceforge.fenixedu.util.HourMinuteSecond.fromDateFields(date));
        }
    }

    @Deprecated
    public boolean hasSummaryHourHourMinuteSecond() {
        return getSummaryHourHourMinuteSecond() != null;
    }

    @Deprecated
    public boolean hasExecutionCourse() {
        return getExecutionCourse() != null;
    }

    @Deprecated
    public boolean hasSummaryDateYearMonthDay() {
        return getSummaryDateYearMonthDay() != null;
    }

    @Deprecated
    public boolean hasSummaryType() {
        return getSummaryType() != null;
    }

    @Deprecated
    public boolean hasTeacherName() {
        return getTeacherName() != null;
    }

    @Deprecated
    public boolean hasTeacher() {
        return getTeacher() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSummaryText() {
        return getSummaryText() != null;
    }

    @Deprecated
    public boolean hasProfessorship() {
        return getProfessorship() != null;
    }

    @Deprecated
    public boolean hasStudentsNumber() {
        return getStudentsNumber() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

    @Deprecated
    public boolean hasLessonInstance() {
        return getLessonInstance() != null;
    }

    @Deprecated
    public boolean hasIsExtraLesson() {
        return getIsExtraLesson() != null;
    }

    @Deprecated
    public boolean hasRoom() {
        return getRoom() != null;
    }

    @Deprecated
    public boolean hasLastModifiedDateDateTime() {
        return getLastModifiedDateDateTime() != null;
    }

    @Deprecated
    public boolean hasTaught() {
        return getTaught() != null;
    }

}
