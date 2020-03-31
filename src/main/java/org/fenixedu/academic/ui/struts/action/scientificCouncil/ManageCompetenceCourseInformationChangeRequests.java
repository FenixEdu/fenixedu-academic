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
package org.fenixedu.academic.ui.struts.action.scientificCouncil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformationChangeRequest;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.bolonhaManager.CompetenceCourseManagementAccessControl;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.scientificCouncil.ScientificCouncilApplication.ScientificBolonhaProcessApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

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

        request.setAttribute("departments", Bennu.getInstance().getDepartmentsSet().stream()
                .sorted(Comparator.comparing(Department::getName)).collect(Collectors.toList()));

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
        CompetenceCourseInformationChangeRequest changeRequest = (CompetenceCourseInformationChangeRequest) FenixFramework
                .getDomainObject(competenceCourseInformationChangeRequestId);
        return changeRequest;
    }

    private void putChangeRequestInRequest(HttpServletRequest request, Department department) {
        List<CompetenceCourseInformationChangeRequest> requests =
                CompetenceCourse.findByUnit(department.getDepartmentUnit(), true)
                        .flatMap(cc -> cc.getCompetenceCourseInformationChangeRequestsSet().stream()).distinct()
                        .collect(Collectors.toList());
        request.setAttribute("changeRequests", requests);
    }

    private boolean isAllowedToViewChangeRequest(Person loggedPerson, CompetenceCourseInformationChangeRequest changeRequest) {
        return CompetenceCourseManagementAccessControl.isLoggedPersonAllowedToApproveChangeRequestsPredicate(changeRequest);
    }
}