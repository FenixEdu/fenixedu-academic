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
package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificBolonhaProcessApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificBolonhaProcessApp.class, path = "manage-versions",
        titleKey = "navigation.competenceCourseVersionManagement")
@Mapping(module = "scientificCouncil", path = "/competenceCourses/manageVersions")
@Forwards({ @Forward(name = "manageVersions", path = "/scientificCouncil/bolonha/manageVersions.jsp"),
        @Forward(name = "listRequests", path = "/scientificCouncil/bolonha/listVersions.jsp"),
        @Forward(name = "viewVersionDetails", path = "/scientificCouncil/bolonha/viewVersionDetails.jsp") })
public class ManageCompetenceCourseInformationChangeRequests extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("departments", Bennu.getInstance().getDepartmentsSet());

        return mapping.findForward("manageVersions");
    }

    public ActionForward displayRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        String departmentID = request.getParameter("departmentID");
        Department department = (Department) FenixFramework.getDomainObject(departmentID);
        putChangeRequestInRequest(request, department);

        return mapping.findForward("listRequests");

    }

    public ActionForward viewVersion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            request.setAttribute("changeRequest", changeRequest);
        }
        return mapping.findForward("viewVersionDetails");
    }

    public ActionForward approveRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            try {
                changeRequest.approve(getLoggedPerson(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return displayRequest(mapping, form, request, response);
    }

    public ActionForward rejectRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        CompetenceCourseInformationChangeRequest changeRequest = getChangeRequest(request);
        if (changeRequest != null && isAllowedToViewChangeRequest(getLoggedPerson(request), changeRequest)) {
            try {
                changeRequest.reject(getLoggedPerson(request));
            } catch (DomainException e) {
                addActionMessage(request, e.getMessage());
            }
        }

        return displayRequest(mapping, form, request, response);
    }

    private CompetenceCourseInformationChangeRequest getChangeRequest(HttpServletRequest request) {
        String competenceCourseInformationChangeRequestId = request.getParameter("changeRequestID");
        CompetenceCourseInformationChangeRequest changeRequest =
                (CompetenceCourseInformationChangeRequest) FenixFramework
                        .getDomainObject(competenceCourseInformationChangeRequestId);
        return changeRequest;
    }

    private CompetenceCourse getCompetenceCourse(HttpServletRequest request) {
        String competenceCourseID = request.getParameter("competenceCourseID");
        CompetenceCourse course = (CompetenceCourse) FenixFramework.getDomainObject(competenceCourseID);
        return course;
    }

    private void putChangeRequestInRequest(HttpServletRequest request, Department department) {
        List<CompetenceCourseInformationChangeRequest> requests = new ArrayList<CompetenceCourseInformationChangeRequest>();
        for (CompetenceCourse courses : department.getDepartmentUnit().getCompetenceCourses()) {
            requests.addAll(courses.getCompetenceCourseInformationChangeRequests());
        }
        request.setAttribute("changeRequests", requests);
    }

    private boolean isAllowedToViewChangeRequest(Person loggedPerson, CompetenceCourseInformationChangeRequest changeRequest) {
        return loggedPerson.hasPersonRoles(Role.getRoleByRoleType(RoleType.SCIENTIFIC_COUNCIL));
    }
}