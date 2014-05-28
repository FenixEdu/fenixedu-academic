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
package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.serviceRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdDocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDocumentRequest;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

}
