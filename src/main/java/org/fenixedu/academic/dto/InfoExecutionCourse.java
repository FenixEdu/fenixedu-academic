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
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */
package org.fenixedu.academic.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseScope;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.NonAffiliatedTeacher;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.teacher.InfoNonAffiliatedTeacher;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.commons.i18n.LocalizedString;

/**
 * @author tfc130
 */
public class InfoExecutionCourse extends InfoObject {

    private final ExecutionCourse executionCourseDomainReference;

    public InfoExecutionCourse(final ExecutionCourse executionCourse) {
        executionCourseDomainReference = executionCourse;
    }

    public static InfoExecutionCourse newInfoFromDomain(final ExecutionCourse executionCourse) {
        return executionCourse == null ? null : new InfoExecutionCourse(executionCourse);
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourseDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoExecutionCourse && getExecutionCourse() == ((InfoExecutionCourse) obj).getExecutionCourse();
    }

    @Override
    public int hashCode() {
        return getExecutionCourse().hashCode();
    }

    @Override
    public String getExternalId() {
        return getExecutionCourse().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    // =================== FIELDS RETRIEVED BY DOMAIN LOGIC
    // =======================

    public Double getWeeklyTheoreticalHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.TEORICA).doubleValue();
    }

    public Double getWeeklyPraticalHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.PRATICA).doubleValue();
    }

    public Double getWeeklyTheoPratHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.TEORICO_PRATICA).doubleValue();
    }

    public Double getWeeklyLabHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.LABORATORIAL).doubleValue();
    }

    public Double getWeeklyFieldWorkHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.FIELD_WORK).doubleValue();
    }

    public Double getWeeklyProblemsHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.PROBLEMS).doubleValue();
    }

    public Double getWeeklySeminaryHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.SEMINARY).doubleValue();
    }

    public Double getWeeklyTrainingPeriodHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.TRAINING_PERIOD).doubleValue();
    }

    public Double getWeeklyTutorialOrientationHours() {
        return getExecutionCourse().getWeeklyCourseLoadTotalQuantityByShiftType(ShiftType.TUTORIAL_ORIENTATION).doubleValue();
    }

    public String getNome() {
        return getExecutionCourse().getNameI18N().getContent(LocaleUtils.PT);
    }
    
    public String getNomePT() {
        return getExecutionCourse().getNameI18N().getContent(LocaleUtils.PT);
    }
    
    public String getNomeEN() {
        return getExecutionCourse().getNameI18N().getContent(LocaleUtils.EN);
    }

    public LocalizedString getNameI18N() {
        return getExecutionCourse().getNameI18N();
    }
    
    public String getSigla() {
        return getExecutionCourse().getSigla();
    }

    public String getComment() {
        return getExecutionCourse().getComment();
    }

    public Integer getNumberOfAttendingStudents() {
        return getExecutionCourse().getAttendsSet().size();
    }

    public String getEqualLoad() {
        return getExecutionCourse().getEqualLoad();
    }

    public Boolean getAvailableGradeSubmission() {
        return getExecutionCourse().getAvailableGradeSubmission();
    }

    @Deprecated
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(getExecutionCourse().getExecutionPeriod());
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionCourse().getAcademicInterval();
    }

    public List<InfoShift> getAssociatedInfoShifts() {
        final List<InfoShift> result = new ArrayList<InfoShift>();

        for (final Shift shift : getExecutionCourse().getAssociatedShifts()) {
            result.add(InfoShift.newInfoFromDomain(shift));
        }

        return result;
    }

    public List<InfoNonAffiliatedTeacher> getNonAffiliatedTeachers() {
        final List<InfoNonAffiliatedTeacher> result = new ArrayList<InfoNonAffiliatedTeacher>();

        for (final NonAffiliatedTeacher nonAffiliatedTeacher : getExecutionCourse().getNonAffiliatedTeachersSet()) {
            result.add(InfoNonAffiliatedTeacher.newInfoFromDomain(nonAffiliatedTeacher));
        }

        return result;
    }

    public List<InfoEvaluation> getAssociatedInfoEvaluations() {
        final List<InfoEvaluation> result = new ArrayList<InfoEvaluation>();

        for (final Evaluation nonAffiliatedTeacher : getExecutionCourse().getAssociatedEvaluationsSet()) {
            result.add(InfoEvaluation.newInfoFromDomain(nonAffiliatedTeacher));
        }

        return result;
    }

    public List<InfoCurricularCourse> getAssociatedInfoCurricularCourses() {
        if (filteredAssociatedInfoCurricularCourses == null) {
            List<InfoCurricularCourse> result = new ArrayList<InfoCurricularCourse>();

            for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
                final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                infoCurricularCourse.setInfoScopes(getInfoScopes(curricularCourse.getScopesSet()));

                result.add(infoCurricularCourse);
            }

            setFilteredAssociatedInfoCurricularCourses(result);
            return result;
        } else {
            return getFilteredAssociatedInfoCurricularCourses();
        }
    }

    public boolean getCanRemoveCurricularCourses() {
        return getAssociatedInfoCurricularCourses().size() > 1;
    }

    private List<InfoCurricularCourseScope> getInfoScopes(final Collection<CurricularCourseScope> curricularCourseScopes) {
        final List<InfoCurricularCourseScope> result = new ArrayList<InfoCurricularCourseScope>();

        for (final CurricularCourseScope curricularCourseScope : curricularCourseScopes) {
            result.add(InfoCurricularCourseScope.newInfoFromDomain(curricularCourseScope));
        }

        return result;
    }

    public List<InfoCurricularCourse> getAssociatedInfoCurricularCourses(final ExecutionYear executionYear) {
        List<InfoCurricularCourse> result = new ArrayList<InfoCurricularCourse>();

        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            final InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(getInfoScopes(curricularCourse.findCurricularCourseScopesIntersectingPeriod(
                    executionYear.getBeginDate(), executionYear.getEndDate())));

            result.add(infoCurricularCourse);
        }

        return result;
    }

    public List<InfoExam> getAssociatedInfoExams() {
        if (filteredAssociatedInfoExams == null) {
            List<InfoExam> result = new ArrayList<InfoExam>();
            for (final Exam exam : getExecutionCourse().getAssociatedExams()) {
                result.add(InfoExam.newInfoFromDomain(exam));
            }
            return result;
        } else {
            return getFilteredAssociatedInfoExams();
        }
    }

    public List<InfoGrouping> getInfoGroupings() {
        if (filteredInfoGroupings == null) {
            List<InfoGrouping> result = new ArrayList<InfoGrouping>();

            for (final Grouping grouping : getExecutionCourse().getGroupings()) {
                result.add(InfoGrouping.newInfoFromDomain(grouping));
            }

            return result;
        } else {
            return getFilteredInfoGroupings();
        }
    }

    public Collection<CourseLoad> getCourseLoads() {
        return getExecutionCourse().getCourseLoadsSet();
    }

    // =================== FIELDS NOT RETRIEVED BY DOMAIN LOGIC
    // =======================

    // The following variable serves the purpose of indicating the
    // the curricular year in which the execution course is given
    // for a certain execution degree through which
    // the execution course was obtained. It should serve only for
    // view purposes!!!
    // It was created to be used and set by the ExamsMap Utilities.
    // It has no meaning in the buisness logic.
    private Integer curricularYear;

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(Integer integer) {
        curricularYear = integer;
    }

    private Double occupancy;

    public Double getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Double occupancy) {
        this.occupancy = occupancy;
    }

    private List<InfoCurricularCourse> filteredAssociatedInfoCurricularCourses;

    private List<InfoCurricularCourse> getFilteredAssociatedInfoCurricularCourses() {
        return filteredAssociatedInfoCurricularCourses;
    }

    public void setFilteredAssociatedInfoCurricularCourses(
            final List<InfoCurricularCourse> filteredAssociatedInfoCurricularCourses) {
        this.filteredAssociatedInfoCurricularCourses = filteredAssociatedInfoCurricularCourses;
    }

    private List<InfoExam> filteredAssociatedInfoExams;

    private List<InfoExam> getFilteredAssociatedInfoExams() {
        return filteredAssociatedInfoExams;
    }

    public void setFilteredAssociatedInfoExams(final List<InfoExam> filteredAssociatedInfoExams) {
        this.filteredAssociatedInfoExams = filteredAssociatedInfoExams;
    }

    @Override
    public String toString() {
        return getExecutionCourse().toString();
    }

    private List<InfoGrouping> filteredInfoGroupings;

    private List<InfoGrouping> getFilteredInfoGroupings() {
        return filteredInfoGroupings;
    }

    public void setFilteredInfoGroupings(List<InfoGrouping> filteredInfoGroupings) {
        this.filteredInfoGroupings = filteredInfoGroupings;
    }

    public EntryPhase getEntryPhase() {
        return getExecutionCourse().getEntryPhase();
    }

}
