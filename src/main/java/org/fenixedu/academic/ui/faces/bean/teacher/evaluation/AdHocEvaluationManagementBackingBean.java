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
package org.fenixedu.academic.ui.faces.bean.teacher.evaluation;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.AdHocEvaluation;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.CreateAdHocEvaluation;
import org.fenixedu.academic.service.services.teacher.DeleteEvaluation;
import org.fenixedu.academic.service.services.teacher.EditAdHocEvaluation;

import pt.ist.fenixframework.FenixFramework;

public class AdHocEvaluationManagementBackingBean extends EvaluationManagementBackingBean {
    protected String name;
    protected AdHocEvaluation adHocEvaluation;
    protected String adHocEvaluationID;

    public AdHocEvaluationManagementBackingBean() {
        super();
    }

    public String createAdHocEvaluation() {
        try {
            CreateAdHocEvaluation.runCreateAdHocEvaluation(getExecutionCourseID(), getName(), getDescription(), getGradeScale());
        } catch (final NotAuthorizedException e) {
            return "";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (final DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "adHocEvaluationsIndex";
    }

    public String editAdHocEvaluation() {
        try {

            EditAdHocEvaluation.run(getExecutionCourseID(), getAdHocEvaluationID(), getName(), getDescription(), getGradeScale());
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "adHocEvaluationsIndex";
    }

    private AdHocEvaluation getAdHocEvaluation() {
        if (this.adHocEvaluation == null && this.getAdHocEvaluationID() != null) {
            this.adHocEvaluation = (AdHocEvaluation) FenixFramework.getDomainObject(getAdHocEvaluationID());
        }
        return this.adHocEvaluation;
    }

    public String deleteAdHocEvaluation() {
        try {
            DeleteEvaluation.runDeleteEvaluation(getExecutionCourseID(), getAdHocEvaluationID());
        } catch (NotAuthorizedException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
        }
        return "adHocEvaluationsIndex";
    }

    public List<AdHocEvaluation> getAssociatedAdHocEvaluations() throws FenixServiceException {
        List<AdHocEvaluation> associatedAdHocEvaluations = getExecutionCourse().getAssociatedAdHocEvaluations();
        Collections.sort(associatedAdHocEvaluations, new BeanComparator("creationDateTime"));
        return associatedAdHocEvaluations;
    }

    public String getName() {
        if (this.name == null && this.getAdHocEvaluation() != null) {
            this.name = this.getAdHocEvaluation().getName();
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        if (this.description == null && this.getAdHocEvaluation() != null) {
            this.description = this.getAdHocEvaluation().getDescription();
        }
        return this.description;
    }

    public String getAdHocEvaluationID() {
        if (this.adHocEvaluationID == null) {
            if (this.getRequestParameter("adHocEvaluationID") != null
                    && !this.getRequestParameter("adHocEvaluationID").equals("")) {
                this.adHocEvaluationID = this.getRequestParameter("adHocEvaluationID");
            }
        }
        return this.adHocEvaluationID;
    }

    public void setAdHocEvaluationID(String adHocEvaluationID) {
        this.adHocEvaluationID = adHocEvaluationID;
    }

    @Override
    public GradeScale getGradeScale() {
        if (gradeScale == null && this.getAdHocEvaluation() != null) {
            this.gradeScale = getAdHocEvaluation().getGradeScale();
        }
        return this.gradeScale;
    }
}