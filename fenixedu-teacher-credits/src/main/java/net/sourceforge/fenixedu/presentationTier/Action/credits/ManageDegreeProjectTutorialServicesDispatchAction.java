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
package org.fenixedu.academic.ui.struts.action.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.credits.util.ProjectTutorialServiceBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.DegreeProjectTutorialService;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixframework.FenixFramework;

public class ManageDegreeProjectTutorialServicesDispatchAction extends FenixDispatchAction {

    public ActionForward showProjectTutorialServiceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        String professorshipID = (String) getFromRequest(request, "professorshipID");
        Professorship professorship = FenixFramework.getDomainObject(professorshipID);
        if (professorship == null) {
            return mapping.findForward("teacher-not-found");
        }
        List<ProjectTutorialServiceBean> projectTutorialServiceBeans = new ArrayList<ProjectTutorialServiceBean>();
        for (Attends attend : professorship.getExecutionCourse().getAttendsSet()) {
            if (attend.getEnrolment() != null) {
                ProjectTutorialServiceBean projectTutorialServiceBean = new ProjectTutorialServiceBean(professorship, attend);
                projectTutorialServiceBeans.add(projectTutorialServiceBean);
            }
        }

        request.setAttribute("professorship", professorship);
        request.setAttribute("projectTutorialServiceBeans", projectTutorialServiceBeans);
        return mapping.findForward("show-project-tutorial-service");
    }

    public ActionForward updateProjectTutorialService(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String professorshipID = (String) getFromRequest(request, "professorshipID");
        Professorship professorship = FenixFramework.getDomainObject(professorshipID);
        List<ProjectTutorialServiceBean> projectTutorialServiceBeans = getRenderedObject("projectTutorialService");
        try {
            DegreeProjectTutorialService.updateProjectTutorialService(projectTutorialServiceBeans);
        } catch (DomainException domainException) {
            addActionMessage("error", request, domainException.getMessage());
            request.setAttribute("professorship", professorship);
            request.setAttribute("projectTutorialServiceBeans", projectTutorialServiceBeans);
            return mapping.findForward("show-project-tutorial-service");
        }
        request.setAttribute("teacherOid", professorship.getTeacher().getExternalId());
        request.setAttribute("executionYearOid", professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear()
                .getNextExecutionYear().getExternalId());
        return mapping.findForward("viewAnnualTeachingCredits");
    }

}
