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
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoCourseReport;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

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
        return getExecutionCourse().getNome();
    }

    public String getSigla() {
        return getExecutionCourse().getSigla();
    }

    public String getComment() {
        return getExecutionCourse().getComment();
    }

    public Boolean getHasSite() {
        return getExecutionCourse().hasSite();
    }

    public Integer getNumberOfAttendingStudents() {
        return getExecutionCourse().getAttends().size();
    }

    public String getCourseReportFilled() {
        return (!getExecutionCourse().hasCourseReport() || getExecutionCourse().getCourseReport().getReport() == null) ? "false" : "true";
    }

    public String getEqualLoad() {
        return getExecutionCourse().getEqualLoad();
    }

    public Boolean getAvailableForInquiries() {
        return getExecutionCourse().getAvailableForInquiries();
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

    public InfoCourseReport getInfoCourseReport() {
        return InfoCourseReport.newInfoFromDomain(getExecutionCourse().getCourseReport());
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

        for (final NonAffiliatedTeacher nonAffiliatedTeacher : getExecutionCourse().getNonAffiliatedTeachers()) {
            result.add(InfoNonAffiliatedTeacher.newInfoFromDomain(nonAffiliatedTeacher));
        }

        return result;
    }

    public List<InfoEvaluation> getAssociatedInfoEvaluations() {
        final List<InfoEvaluation> result = new ArrayList<InfoEvaluation>();

        for (final Evaluation nonAffiliatedTeacher : getExecutionCourse().getAssociatedEvaluations()) {
            result.add(InfoEvaluation.newInfoFromDomain(nonAffiliatedTeacher));
        }

        return result;
    }

    public List<InfoCurricularCourse> getAssociatedInfoCurricularCourses() {
        if (filteredAssociatedInfoCurricularCourses == null) {
            List<InfoCurricularCourse> result = new ArrayList<InfoCurricularCourse>();

            for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCourses()) {
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

        for (final CurricularCourse curricularCourse : getExecutionCourse().getAssociatedCurricularCourses()) {
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
        return getExecutionCourse().getCourseLoads();
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

    // useful for coordinator portal
    protected InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    public InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics() {
        return infoSiteEvaluationStatistics;
    }

    public void setInfoSiteEvaluationStatistics(InfoSiteEvaluationStatistics infoSiteEvaluationStatistics) {
        this.infoSiteEvaluationStatistics = infoSiteEvaluationStatistics;
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
