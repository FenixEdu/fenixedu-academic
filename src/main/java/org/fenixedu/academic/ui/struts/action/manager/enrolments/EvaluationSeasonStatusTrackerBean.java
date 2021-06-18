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
package org.fenixedu.academic.ui.struts.action.manager.enrolments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionSemester;

public class EvaluationSeasonStatusTrackerBean implements Serializable {

    private static final long serialVersionUID = 7601169267648955212L;

    private EvaluationSeason evaluationSeason;
    private ExecutionSemester executionSemester;
    private Department department;
    private CompetenceCourse competenceCourse;
    private List<Enrolment> enrolments;
    private List<EvaluationSeasonStatusTrackerRegisterBean> entries;

    public EvaluationSeasonStatusTrackerBean() {
        super();
        entries = new ArrayList<EvaluationSeasonStatusTrackerRegisterBean>();
    }

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public CompetenceCourse getCompetenceCourse() {
        return competenceCourse;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(List<Enrolment> enrolments) {
        this.enrolments = enrolments;
    }

    public List<EvaluationSeasonStatusTrackerRegisterBean> getEntries() {
        return entries;
    }

    public void addEntry(Integer studentNumber, String studentName, String degreeSigla, String courseName) {
        EvaluationSeasonStatusTrackerRegisterBean newEntry =
                new EvaluationSeasonStatusTrackerRegisterBean(studentNumber, studentName, degreeSigla, courseName);
        entries.add(newEntry);
    }

    public void clearEntries() {
        entries.clear();
    }
}
