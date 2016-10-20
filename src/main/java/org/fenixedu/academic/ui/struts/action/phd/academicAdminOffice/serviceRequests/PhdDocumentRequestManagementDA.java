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
package org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.serviceRequests;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Mapping(path = "/phdDocumentRequestManagement", module = "academicAdministration",
        functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "createNewDocumentRequest",
        path = "/phd/academicAdminOffice/serviceRequests/document/createNewDocumentRequest.jsp") })
public class PhdDocumentRequestManagementDA extends PhdAcademicServiceRequestsManagementDA {

    @Override
    protected PhdDocumentRequestCreateBean getPhdAcademicServiceRequestCreateBean() {
        return (PhdDocumentRequestCreateBean) super.getPhdAcademicServiceRequestCreateBean();
    }

    @Override
    protected PhdDocumentRequest getPhdAcademicServiceRequest(HttpServletRequest request) {
        return (PhdDocumentRequest) super.getPhdAcademicServiceRequest(request);
    }

    @Override
    public ActionForward prepareCreateNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
        PhdDocumentRequestCreateBean phdDocumentRequestCreateBean = new PhdDocumentRequestCreateBean(process);
        request.setAttribute("phdAcademicServiceRequestCreateBean", phdDocumentRequestCreateBean);

        return mapping.findForward("createNewDocumentRequest");
    }

    @Override
    public ActionForward createNewRequestInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        super.createNewRequestInvalid(mapping, form, request, response);
        return mapping.findForward("prepareCreateNewRequest");
    }

    public ActionForward createNewRequestPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("phdAcademicServiceRequestCreateBean", getPhdAcademicServiceRequestCreateBean());

        RenderUtils.invalidateViewState("phd-academic-service-request-create-bean");
        RenderUtils.invalidateViewState("phd-academic-service-request-create-bean-choose-document-type");

        return mapping.findForward("createNewDocumentRequest");
    }

    public ActionForward printDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException, FenixServiceException {
        final IDocumentRequest documentRequest = getPhdAcademicServiceRequest(request);
        try {
            byte[] data = documentRequest.generateDocument();

            response.setContentLength(data.length);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + documentRequest.getReportFileName() + ".pdf");

            final ServletOutputStream writer = response.getOutputStream();
            writer.write(data);
            writer.flush();
            writer.close();

            response.flushBuffer();
            return null;
        } catch (DomainException e) {
            throw e;
        }
    }

}
