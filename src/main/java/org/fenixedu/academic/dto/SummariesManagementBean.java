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
package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.NextPossibleSummaryLessonsAndDatesBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.LessonPlanning;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.Partial;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class SummariesManagementBean implements Serializable {

    private ExecutionCourse executionCourseReference;

    private SummaryType summaryType;

    private ShiftType lessonType;

    private Lesson lessonReference;

    private Shift shiftReference;

    private YearMonthDay summaryDate;

    private Partial summaryTime;

    private Professorship professorshipReference;

    private Teacher teacherReference;

    private String teacherName;

    private Space roomReference;

    private Integer studentsNumber;

    private MultiLanguageString title;

    private MultiLanguageString summaryText;

    private LessonPlanning lessonPlannigReference;

    private Summary lastSummaryReference;

    private Summary summaryReference;

    private Professorship professorshipLoggedReference;

    private Boolean taught;

    private List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean;

    protected SummariesManagementBean() {
    }

    public SummariesManagementBean(SummaryType summaryType, ExecutionCourse executionCourse, Professorship professorship,
            List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean) {
        setSummaryType(summaryType);
        setExecutionCourse(executionCourse);
        setProfessorship(professorship);
        setProfessorshipLogged(professorship);
        setNextPossibleSummaryLessonsAndDatesBean(nextPossibleSummaryLessonsAndDatesBean);
        setTaught(true);
    }

    public SummariesManagementBean(MultiLanguageString title, MultiLanguageString summaryText, Integer studentsNumber,
            SummaryType summaryType, Professorship professorship, String teacherName, Teacher teacher, Shift shift,
            Lesson lesson, YearMonthDay summaryDate, Space summaryRoom, Partial summaryTime, Summary summary,
            Professorship professorshipLogged, ShiftType lessonType, Boolean taught) {

        setTitle(title);
        setSummaryText(summaryText);
        setSummaryType(summaryType);
        setShift(shift);
        setLesson(lesson);
        setProfessorship(professorship);
        setTeacher(teacher);
        setTeacherName(teacherName);
        setSummary(summary);
        setSummaryDate(summaryDate);
        setSummaryRoom(summaryRoom);
        setSummaryTime(summaryTime);
        setStudentsNumber(studentsNumber);
        setExecutionCourse(shift.getExecutionCourse());
        setProfessorshipLogged(professorshipLogged);
        setLessonType(lessonType);
        setTaught(taught);
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    public Summary getSummary() {
        return this.summaryReference;
    }

    public void setSummary(Summary summary) {
        this.summaryReference = summary;
    }

    public ExecutionCourse getExecutionCourse() {
        return this.executionCourseReference;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = executionCourse;
    }

    public Teacher getTeacher() {
        return this.teacherReference;
    }

    public void setTeacher(Teacher teacher) {
        this.teacherReference = teacher;
    }

    public Professorship getProfessorshipLogged() {
        return this.professorshipLoggedReference;
    }

    public void setProfessorshipLogged(Professorship professorship) {
        this.professorshipLoggedReference = professorship;
    }

    public Summary getLastSummary() {
        return this.lastSummaryReference;
    }

    public void setLastSummary(Summary summary) {
        this.lastSummaryReference = summary;
    }

    public LessonPlanning getLessonPlanning() {
        return this.lessonPlannigReference;
    }

    public void setLessonPlanning(LessonPlanning lessonPlanning) {
        this.lessonPlannigReference = lessonPlanning;
    }

    public Professorship getProfessorship() {
        return this.professorshipReference;
    }

    public void setProfessorship(Professorship professorship) {
        this.professorshipReference = professorship;
    }

    public Lesson getLesson() {
        return this.lessonReference;
    }

    public void setLesson(Lesson lesson) {
        this.lessonReference = lesson;
    }

    public Shift getShift() {
        return this.shiftReference;
    }

    public void setShift(Shift shift) {
        this.shiftReference = shift;
    }

    public Space getSummaryRoom() {
        return this.roomReference;
    }

    public void setSummaryRoom(Space room) {
        this.roomReference = room;
    }

    public SummaryType getSummaryType() {
        return summaryType;
    }

    public void setSummaryType(SummaryType summaryType) {
        this.summaryType = summaryType;
    }

    public YearMonthDay getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(YearMonthDay date) {
        this.summaryDate = date;
    }

    public MultiLanguageString getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(MultiLanguageString summary) {
        this.summaryText = summary;
    }

    public MultiLanguageString getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

    public Partial getSummaryTime() {
        return summaryTime;
    }

    public void setSummaryTime(Partial hour) {
        this.summaryTime = hour;
    }

    public String getTeacherChoose() {
        if (getProfessorship() != null) {
            return getProfessorship().getExternalId().toString();
        } else if (!StringUtils.isEmpty(getTeacherName())) {
            return "-1";
        } else if (getTeacher() != null) {
            return "0";
        }
        return "";
    }

    public static enum SummaryType {
        NORMAL_SUMMARY, EXTRA_SUMMARY;
        public String getName() {
            return name();
        }
    }

    public boolean isNewSummary() {
        return getSummary() == null;
    }

    public List<NextPossibleSummaryLessonsAndDatesBean> getNextPossibleSummaryLessonsAndDatesBean() {
        return nextPossibleSummaryLessonsAndDatesBean;
    }

    public void setNextPossibleSummaryLessonsAndDatesBean(
            List<NextPossibleSummaryLessonsAndDatesBean> nextPossibleSummaryLessonsAndDatesBean) {
        this.nextPossibleSummaryLessonsAndDatesBean = nextPossibleSummaryLessonsAndDatesBean;
    }

    public ShiftType getLessonType() {
        return lessonType;
    }

    public void setLessonType(ShiftType lessonType) {
        this.lessonType = lessonType;
    }

    public void setTaught(Boolean taught) {
        this.taught = taught;
    }

    public Boolean getTaught() {
        return taught;
    }
}
