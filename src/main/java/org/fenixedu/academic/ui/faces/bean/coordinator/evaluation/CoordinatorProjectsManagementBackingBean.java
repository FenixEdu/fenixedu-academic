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
 * Created on Nov 9, 2005
 *  by jdnf
 */
package org.fenixedu.academic.ui.faces.bean.coordinator.evaluation;

import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.DeleteEvaluation;
import org.fenixedu.academic.util.DateFormatUtil;

import pt.ist.fenixframework.FenixFramework;

public class CoordinatorProjectsManagementBackingBean extends CoordinatorProjectsInformationBackingBean {
    protected String name;
    protected String beginDate;
    protected String beginHour;
    protected String endDate;
    protected String endHour;
    protected String description;

    private String getBeginString() {
        return getBeginDate() + " " + getBeginHour();
    }

    private String getEndString() {
        return getEndDate() + " " + getEndHour();
    }

    public String createProject() {
//        try {
//            CreateProject.runCreateProject(getExecutionCourseID(), getName(),
//                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
//                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription(), null);
//        } catch (final FenixFilterException e) {
//            return "";
//        } catch (final FenixServiceException e) {
//            setErrorMessage(e.getMessage());
//            return "";
//        } catch (final ParseException e) {
//            setErrorMessage("error.invalidDate");
//            return "";
//        }
//        return this.showProjectsForExecutionCourses();
        throw new UnsupportedOperationException();
    }

    public String editProject() {
//        try {
//            EditProject.runEditProject(getExecutionCourseID(), getEvaluationID(), getName(),
//                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getBeginString()),
//                    DateFormatUtil.parse("dd/MM/yyyy HH:mm", getEndString()), getDescription(), null);
//        } catch (final FenixFilterException e) {
//        } catch (final FenixServiceException e) {
//            setErrorMessage(e.getMessage());
//            return "";
//        } catch (ParseException e) {
//            setErrorMessage("error.invalidDate");
//            return "";
//        }
//        return this.showProjectsForExecutionCourses();
        throw new UnsupportedOperationException();
    }

    public String deleteProject() {
        try {
            DeleteEvaluation.runDeleteEvaluation(getExecutionCourseID(), getEvaluationID());
        } catch (NotAuthorizedException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        }
        return this.showProjectsForExecutionCourses();
    }

    @Override
    public Evaluation getEvaluation() {
        try {
            if (this.evaluation == null && this.getEvaluationID() != null) {
                this.evaluation = FenixFramework.getDomainObject(getEvaluationID());
            }
            return this.evaluation;
        } catch (Exception e) {
            return null;
        }
    }

    public String showProjectsForExecutionCourses() {
        setRequestCommonAttributes();
        return "showProjectsForExecutionCourses";
    }

    public String getBeginDate() {
        if (this.beginDate == null) {
            if (this.getEvaluation() != null) {
                this.beginDate = DateFormatUtil.format("dd/MM/yyyy", ((Project) this.getEvaluation()).getBegin());
            } else if (getDay() != null & getMonth() != null & getYear() != null) {
                this.beginDate = getDay() + "/" + getMonth() + "/" + getYear();
            }
        }
        return this.beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        if (this.endDate == null && this.getEvaluation() != null) {
            this.endDate = DateFormatUtil.format("dd/MM/yyyy", ((Project) this.getEvaluation()).getEnd());
        }
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        if (this.name == null && this.getEvaluation() != null) {
            this.name = ((Project) this.getEvaluation()).getName();
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (this.description == null && this.getEvaluation() != null) {
            this.description = ((Project) this.getEvaluation()).getDescription();
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginHour() {
        if (this.beginHour == null && this.getEvaluation() != null) {
            this.beginHour = DateFormatUtil.format("HH:mm", ((Project) this.getEvaluation()).getBegin());
        }
        return this.beginHour;
    }

    public void setBeginHour(String beginHour) {
        this.beginHour = beginHour;
    }

    public String getEndHour() {
        if (this.endHour == null && this.getEvaluation() != null) {
            this.endHour = DateFormatUtil.format("HH:mm", ((Project) this.getEvaluation()).getEnd());
        }
        return this.endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }
}
