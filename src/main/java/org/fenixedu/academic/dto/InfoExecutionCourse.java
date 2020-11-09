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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

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
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.TEORICA).doubleValue();
    }

//    public Double getWeeklyPraticalHours() {
//        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.PRATICA).doubleValue();
//    }
//
//    public Double getWeeklyTheoPratHours() {
//        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.TEORICO_PRATICA).doubleValue();
//    }

    public Double getWeeklyLabHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.LABORATORIAL).doubleValue();
    }

    public Double getWeeklyFieldWorkHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.FIELD_WORK).doubleValue();
    }

    public Double getWeeklyProblemsHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.PROBLEMS).doubleValue();
    }

    public Double getWeeklySeminaryHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.SEMINARY).doubleValue();
    }

    public Double getWeeklyTrainingPeriodHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.TRAINING_PERIOD).doubleValue();
    }

    public Double getWeeklyTutorialOrientationHours() {
        return getWeeklyCourseLoadTotalQuantityByShiftType(getExecutionCourse(), ShiftType.TUTORIAL_ORIENTATION).doubleValue();
    }

    private static BigDecimal getWeeklyCourseLoadTotalQuantityByShiftType(ExecutionCourse executionCourse, ShiftType type) {
        CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(type);
        return courseLoad != null ? courseLoad.getWeeklyHours() : BigDecimal.ZERO;
    }

    public String getNome() {
        return getExecutionCourse().getNome();
    }

    public String getSigla() {
        return getExecutionCourse().getSigla();
    }

    public Integer getNumberOfAttendingStudents() {
        return getExecutionCourse().getAttendsSet().size();
    }

//    public String getEqualLoad() {
//        return getExecutionCourse().getEqualLoad();
//    }

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

    @Override
    public String toString() {
        return getExecutionCourse().toString();
    }

}
