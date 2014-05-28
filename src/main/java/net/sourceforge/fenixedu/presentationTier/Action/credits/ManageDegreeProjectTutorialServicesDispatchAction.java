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
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.credits.util.ProjectTutorialServiceBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.teacher.DegreeProjectTutorialService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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
        for (Attends attend : professorship.getExecutionCourse().getAttends()) {
            if (attend.hasEnrolment()) {
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
