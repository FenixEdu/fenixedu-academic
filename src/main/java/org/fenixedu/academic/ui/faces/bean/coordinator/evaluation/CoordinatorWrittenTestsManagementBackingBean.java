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
 * Created on Oct 26, 2005
 *  by jdnf
 */
package org.fenixedu.academic.ui.faces.bean.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeModuleScope;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.WrittenTest;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.CreateWrittenEvaluation;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.DeleteWrittenEvaluation;
import org.fenixedu.academic.service.services.resourceAllocationManager.exams.EditWrittenEvaluation;

public class CoordinatorWrittenTestsManagementBackingBean extends CoordinatorWrittenTestsInformationBackingBean {

    private Integer beginHour;
    private Integer beginMinute;
    private Integer endHour;
    private Integer endMinute;
    private String description;

    public String createWrittenTest() {
        try {
            final ExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> degreeModuleScopeIDs = getDegreeModuleScopeIDs(executionCourse);

            CreateWrittenEvaluation.runCreateWrittenEvaluation(this.getExecutionCourseID(), this.getBegin().getTime(), this
                    .getBegin().getTime(), this.getEnd().getTime(), executionCourseIDs, degreeModuleScopeIDs, null, null, null,
                    this.getDescription());

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String editWrittenTest() {
        try {
            final ExecutionCourse executionCourse = getExecutionCourse();
            if (executionCourse == null) {
                this.setErrorMessage("error.noExecutionCourse");
                return "";
            }
            final List<String> executionCourseIDs = new ArrayList<String>(1);
            executionCourseIDs.add(this.getExecutionCourseID().toString());
            final List<String> degreeModuleScopeIDs = getDegreeModuleScopeIDs(executionCourse);

            EditWrittenEvaluation.runEditWrittenEvaluation(executionCourse.getExternalId(), this.getBegin().getTime(), this
                    .getBegin().getTime(), this.getEnd().getTime(), executionCourseIDs, degreeModuleScopeIDs, null, this
                    .getEvaluationID(), null, this.getDescription(), null);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String deleteWrittenTest() {
        try {
            DeleteWrittenEvaluation.runDeleteWrittenEvaluation(this.getExecutionCourseID(), this.getEvaluationID());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }
        return this.showWrittenTestsForExecutionCourses();
    }

    public String showWrittenTestsForExecutionCourses() {
        setRequestCommonAttributes();
        return "showWrittenTestsForExecutionCourses";
    }

    private List<String> getDegreeModuleScopeIDs(final ExecutionCourse executionCourse) {
        final List<String> degreeModuleScopeIDs = new ArrayList<String>();
        for (final CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCoursesSet()) {
            List<DegreeModuleScope> degreeModuleScopes = curricularCourse.getDegreeModuleScopes();
            for (DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
                if (degreeModuleScope.getCurricularSemester().equals(executionCourse.getExecutionPeriod().getSemester())) {
                    degreeModuleScopeIDs.add(degreeModuleScope.getKey());
                }
            }
        }
        return degreeModuleScopeIDs;
    }

    private Calendar getBegin() {
        final Calendar result = Calendar.getInstance();
        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getBeginHour(), this.getBeginMinute());

        return result;
    }

    private Calendar getEnd() {
        final Calendar result = Calendar.getInstance();
        result.set(this.getYear(), this.getMonth() - 1, this.getDay(), this.getEndHour(), this.getEndMinute());

        return result;
    }

    public Integer getBeginHour() {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.HOUR_OF_DAY);
        }
        return this.beginHour;
    }

    public void setBeginHour(Integer beginHour) {
        this.beginHour = beginHour;
    }

    public Integer getBeginMinute() {
        if (this.beginMinute == null && this.getEvaluation() != null) {
            this.beginMinute = ((WrittenEvaluation) getEvaluation()).getBeginning().get(Calendar.MINUTE);
        }
        return this.beginMinute;
    }

    public void setBeginMinute(Integer beginMinute) {
        this.beginMinute = beginMinute;
    }

    public String getDescription() {
        if (this.description == null && this.getEvaluation() != null) {
            final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) getEvaluation();
            if (writtenEvaluation instanceof WrittenTest) {
                this.description = ((WrittenTest) writtenEvaluation).getDescription();
            }
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEndHour() {
        if (this.endHour == null && this.getEvaluation() != null) {
            this.endHour = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.HOUR_OF_DAY);
        }
        return this.endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        if (this.endMinute == null && this.getEvaluation() != null) {
            this.endMinute = ((WrittenEvaluation) getEvaluation()).getEnd().get(Calendar.MINUTE);
        }
        return this.endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }
}
