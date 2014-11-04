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
package org.fenixedu.academic.ui.struts.action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "view-document-requests",
        titleKey = "documents.requirement.consult")
@Mapping(path = "/viewDocumentRequests", module = "student", formBean = "documentRequestCreateForm")
@Forwards(value = {
        @Forward(name = "viewDocumentRequests",
                path = "/student/administrativeOfficeServices/documentRequest/viewDocumentRequests.jsp"),
        @Forward(name = "prepareCancelAcademicServiceRequest",
                path = "/student/administrativeOfficeServices/documentRequest/prepareCancelAcademicServiceRequest.jsp"),
        @Forward(name = "cancelSuccess", path = "/student/administrativeOfficeServices/documentRequest/cancelSuccess.jsp"),
        @Forward(name = "viewDocumentRequest",
                path = "/student/administrativeOfficeServices/documentRequest/viewDocumentRequest.jsp") })
public class ViewDocumentRequestsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewDocumentRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("student", getLoggedPerson(request).getStudent());
        request.setAttribute("documentRequests", getDocumentRequest());
        return mapping.findForward("viewDocumentRequests");
    }

    private List<AcademicServiceRequest> getDocumentRequest() {
        final List<AcademicServiceRequest> result = new ArrayList<AcademicServiceRequest>();
        for (final Registration registration : AccessControl.getPerson().getStudent().getRegistrationsSet()) {
            result.addAll(registration.getAcademicServiceRequestsSet());
        }
        return result;
    }

    public ActionForward viewDocumentRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("documentRequest", FenixFramework.getDomainObject(request.getParameter("documentRequestId")));
        return mapping.findForward("viewDocumentRequest");
    }

    public ActionForward prepareCancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {

        getAndSetAcademicServiceRequest(request);
        return mapping.findForward("prepareCancelAcademicServiceRequest");
    }

    public ActionForward cancelAcademicServiceRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        final AcademicServiceRequest academicServiceRequest = getAndSetAcademicServiceRequest(request);
        final String justification = ((DynaActionForm) actionForm).getString("justification");
        try {
            academicServiceRequest.cancel(justification);
        } catch (DomainExceptionWithLabelFormatter ex) {
            addActionMessage(request, ex.getKey(), solveLabelFormatterArgs(request, ex.getLabelFormatterArgs()));
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        } catch (DomainException ex) {
            addActionMessage(request, ex.getKey());
            return mapping.findForward("prepareCancelAcademicServiceRequest");
        }

        return mapping.findForward("cancelSuccess");
    }

    private AcademicServiceRequest getAndSetAcademicServiceRequest(final HttpServletRequest request) {
        final AcademicServiceRequest academicServiceRequest =
                FenixFramework.getDomainObject(request.getParameter("academicServiceRequestId"));
        request.setAttribute("academicServiceRequest", academicServiceRequest);
        return academicServiceRequest;
    }

}
