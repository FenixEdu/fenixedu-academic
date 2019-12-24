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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.interfaces.HasExecutionDegree;

public class ExecutionCourseManagementBean implements java.io.Serializable, HasExecutionDegree {

    private static final long serialVersionUID = 1L;

    private ExecutionInterval semester;
    private String acronym;
    private String name;
    private EntryPhase entryPhase;
    private String comments;

    private List<CurricularCourse> curricularCourseList;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionCourse executionCourse;

    private CurricularYear curricularYear;

    public ExecutionCourseManagementBean(ExecutionInterval semester) {
        setSemester(semester);
        setCurricularCourseList(new ArrayList<CurricularCourse>());
    }

    public ExecutionCourseManagementBean(final ExecutionInterval semester, final CurricularCourse curricularCourse) {
        setSemester(semester);
        setCurricularCourseList(new ArrayList<CurricularCourse>());
        getCurricularCourseList().add(curricularCourse);
        setName(curricularCourse.getNameI18N().getContent(org.fenixedu.academic.util.LocaleUtils.PT));
    }

    public ExecutionCourseManagementBean(final ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    public ExecutionCourseManagementBean(final DegreeCurricularPlan degreeCurricularPlan) {
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ExecutionCourseManagementBean() {

    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public ExecutionInterval getSemester() {
        return semester;
    }

    public void setSemester(ExecutionInterval semester) {
        this.semester = semester;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public List<CurricularCourse> getCurricularCourseList() {
        return curricularCourseList;
    }

    public void setCurricularCourseList(List<CurricularCourse> curricularCourseList) {
        this.curricularCourseList = curricularCourseList;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        if (semester != null && degreeCurricularPlan != null) {
            ExecutionDegree degree = degreeCurricularPlan.getExecutionDegreeByAcademicInterval(semester.getAcademicInterval());

            return degree;
        }

        return null;
    }

}
