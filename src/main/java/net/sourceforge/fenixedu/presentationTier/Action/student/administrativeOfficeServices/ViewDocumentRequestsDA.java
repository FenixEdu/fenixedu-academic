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
package net.sourceforge.fenixedu.presentationTier.Action.student.administrativeOfficeServices;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
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
