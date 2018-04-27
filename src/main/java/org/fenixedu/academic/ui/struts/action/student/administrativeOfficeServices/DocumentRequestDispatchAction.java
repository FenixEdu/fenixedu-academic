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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.service.factoryExecutors.DocumentRequestCreator;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = StudentAcademicOfficeServices.class, path = "create-document-request",
        titleKey = "documents.requirement")
@Mapping(module = "student", path = "/documentRequest", formBean = "documentRequestCreateForm")
@Forwards(value = {
        @Forward(name = "createDocumentRequests",
                path = "/student/administrativeOfficeServices/documentRequest/createDocumentRequests.jsp"),
        @Forward(name = "createSuccess", path = "/student/administrativeOfficeServices/documentRequest/createSuccess.jsp"),
        @Forward(name = "viewDocumentRequestsToCreate",
                path = "/student/administrativeOfficeServices/documentRequest/viewDocumentRequestsToCreate.jsp"),
        @Forward(name = "chooseRegistration",
                path = "/student/administrativeOfficeServices/documentRequest/chooseRegistration.jsp") })
public class DocumentRequestDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward chooseRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("registrations", getLoggedPerson(request).getStudent().getRegistrationsSet());

        return mapping.findForward("chooseRegistration");
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(request, actionForm)));

        return mapping.findForward("createDocumentRequests");
    }

    private Registration getRegistration(final HttpServletRequest request, final ActionForm actionForm) {
        String registrationId = getStringFromRequest(request, "registrationId");
        if (registrationId == null) {
            registrationId = ((DynaActionForm) actionForm).getString("registrationId");
        }
        return FenixFramework.getDomainObject(registrationId);
    }

    public ActionForward prepareCreateDocumentRequest(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("documentRequestCreateBean", new DocumentRequestCreator(getRegistration(request, actionForm)));

        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    public ActionForward documentRequestTypeChosenPostBack(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();
        RenderUtils.invalidateViewState();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("createDocumentRequests");
    }

    private void setAdditionalInformationSchemaName(HttpServletRequest request, final DocumentRequestCreateBean requestCreateBean) {
        if (requestCreateBean.getHasAdditionalInformation()) {
            ServiceRequestType serviceRequestType = requestCreateBean.getChosenServiceRequestType();
            request.setAttribute("additionalInformationSchemaName", "DocumentRequestCreateBean." + serviceRequestType.getCode()
                    + ".AdditionalInformation");
        }
    }

    public ActionForward viewDocumentRequestToCreate(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final DocumentRequestCreateBean requestCreateBean =
                (DocumentRequestCreateBean) RenderUtils.getViewState().getMetaObject().getObject();

        setAdditionalInformationSchemaName(request, requestCreateBean);
        request.setAttribute("documentRequestCreateBean", requestCreateBean);
        return mapping.findForward("viewDocumentRequestsToCreate");
    }

    public ActionForward create(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        try {
            executeFactoryMethod();
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            return viewDocumentRequestToCreate(mapping, actionForm, request, response);
        }

        request.setAttribute("documentRequestCreateBean", ((DocumentRequestCreateBean) getRenderedObject()).getRegistration());

        return mapping.findForward("createSuccess");
    }

}
