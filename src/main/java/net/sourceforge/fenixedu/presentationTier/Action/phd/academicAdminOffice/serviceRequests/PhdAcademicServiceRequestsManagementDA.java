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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.IPhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdAcademicServiceRequestManagement", module = "academicAdministration",
        functionality = PhdIndividualProgramProcessDA.class)
@Forwards({
        @Forward(name = "prepareCreateNewRequest", path = "/phd/academicAdminOffice/serviceRequests/prepareCreateNewRequest.jsp"),
        @Forward(name = "viewPhdAcademicServiceRequestsHistoric",
                path = "/phd/academicAdminOffice/serviceRequests/viewPhdAcademicServiceRequestsHistoric.jsp"),
        @Forward(name = "viewRequest", path = "/phd/academicAdminOffice/serviceRequests/viewRequest.jsp"),
        @Forward(name = "prepareProcessNewState", path = "/phd/academicAdminOffice/serviceRequests/prepareProcessNewState.jsp"),
        @Forward(name = "processReceiveNewStateOnRectorate",
                path = "/phd/academicAdminOffice/serviceRequests/document/diploma/rectorate/processReceiveNewState.jsp") })
public class PhdAcademicServiceRequestsManagementDA extends PhdDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("phdAcademicServiceRequest", getPhdAcademicServiceRequest(request));
        request.setAttribute("phdIndividualProgramProcess", getPhdIndividualProgramProcess(request));

        if (getPhdAcademicServiceRequest(request) != null) {
            request.setAttribute("phdIndividualProgramProcess", getPhdAcademicServiceRequest(request)
                    .getPhdIndividualProgramProcess());
        }

        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward viewHistoric(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("viewPhdAcademicServiceRequestsHistoric");
    }

    public ActionForward viewAcademicServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        if ("true".equals(request.getParameter("fromHistory"))) {
            request.setAttribute("fromHistory", "true");
        }

        return mapping.findForward("viewRequest");
    }

    public ActionForward prepareCreateNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
        PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = new PhdAcademicServiceRequestCreateBean(process);

        request.setAttribute("phdAcademicServiceRequestCreateBean", academicServiceRequestCreateBean);

        return mapping.findForward("prepareCreateNewRequest");
    }

    public ActionForward createNewRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = getPhdAcademicServiceRequestCreateBean();
        try {
            academicServiceRequestCreateBean.createNewRequest();
        } catch (PhdDomainOperationException exception) {
            addErrorMessage(request, exception.getMessage(), new String[0]);
            return prepareCreateNewRequest(mapping, form, request, response);
        }

        ActionForward forward = viewPhdIndividualProgramProcess(request, academicServiceRequestCreateBean);
        return forward;
    }

    public ActionForward createNewRequestInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdAcademicServiceRequestCreateBean academicServiceRequestCreateBean = getPhdAcademicServiceRequestCreateBean();
        request.setAttribute("phdAcademicServiceRequestCreateBean", academicServiceRequestCreateBean);

        return mapping.findForward("prepareCreateNewRequest");
    }

    /* PROCESS */
    public ActionForward prepareProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.PROCESSING);
    }

    public ActionForward process(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    /* CANCEL */
    public ActionForward prepareCancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.CANCELLED);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    /* REJECT */
    public ActionForward prepareReject(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.REJECTED);
    }

    public ActionForward reject(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    /* CONCLUDE */
    public ActionForward prepareConclude(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.CONCLUDED);
    }

    public ActionForward conclude(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    /* RECEIVE */
    public ActionForward prepareReceive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response,
                AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY);
    }

    public ActionForward receive(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    public ActionForward receiveInRectorate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        handleNewSituation(mapping, form, request, response);

        String batchOid = request.getParameter("batchOid");
        return redirect("/rectorateDocumentSubmission.do?method=viewBatch&batchOid=" + batchOid, request);
    }

    public ActionForward prepareReceiveOnRectorate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        prepareProcessNewState(mapping, form, request, response,
                AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY);

        request.setAttribute("batchOid", request.getParameter("batchOid"));

        return mapping.findForward("processReceiveNewStateOnRectorate");
    }

    /* DELIVER */
    public ActionForward prepareDeliver(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.DELIVERED);
    }

    public ActionForward deliver(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return handleNewSituation(mapping, form, request, response);
    }

    /* DOWNLOAD DOCUMENT */
    public ActionForward downloadLastGeneratedDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PhdAcademicServiceRequest academicServiceRequest = getPhdAcademicServiceRequest(request);

        writeFile(response, academicServiceRequest.getLastGeneratedDocument().getFilename(), academicServiceRequest
                .getLastGeneratedDocument().getContentType(), academicServiceRequest.getLastGeneratedDocument().getContents());

        return null;
    }

    protected ActionForward prepareProcessNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, AcademicServiceRequestSituationType situationType) {
        PhdAcademicServiceRequest academicServiceRequest = getPhdAcademicServiceRequest(request);
        PhdAcademicServiceRequestBean academicServiceRequestBean = new PhdAcademicServiceRequestBean(academicServiceRequest);

        academicServiceRequestBean.setSituationType(situationType);
        request.setAttribute("phdAcademicServiceRequestBean", academicServiceRequestBean);

        return mapping.findForward("prepareProcessNewState");
    }

    protected ActionForward handleNewSituation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
        try {
            academicServiceRequestBean.handleNewSituation();
        } catch (DomainException e) {
            addActionMessage("academicAdminOfficeErrors", request, e.getKey(), e.getArgs());
            request.setAttribute("phdAcademicServiceRequestBean", getPhdAcademicServiceRequestBean());

            return mapping.findForward("prepareProcessNewState");
        }

        return viewPhdIndividualProgramProcess(request, academicServiceRequestBean);
    }

    public ActionForward processNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("phdAcademicServiceRequestBean", getPhdAcademicServiceRequestBean());
        return mapping.findForward("prepareProcessNewState");
    }

    protected PhdAcademicServiceRequestBean getPhdAcademicServiceRequestBean() {
        return (PhdAcademicServiceRequestBean) getObjectFromViewState("phd-academic-service-request-bean");
    }

    protected PhdIndividualProgramProcess getPhdIndividualProgramProcess(final HttpServletRequest request) {
        return (PhdIndividualProgramProcess) getDomainObject(request, "phdIndividualProgramProcessId");
    }

    protected PhdAcademicServiceRequestCreateBean getPhdAcademicServiceRequestCreateBean() {
        return (PhdAcademicServiceRequestCreateBean) getObjectFromViewState("phd-academic-service-request-create-bean");
    }

    protected PhdAcademicServiceRequest getPhdAcademicServiceRequest(final HttpServletRequest request) {
        return (PhdAcademicServiceRequest) getDomainObject(request, "phdAcademicServiceRequestId");
    }

    private ActionForward viewPhdIndividualProgramProcess(HttpServletRequest request,
            IPhdAcademicServiceRequest phdAcademicServiceRequest) {
        ActionForward forward =
                redirect("/phdIndividualProgramProcess.do?method=viewProcess&processId="
                        + phdAcademicServiceRequest.getPhdIndividualProgramProcess().getExternalId(), request);
        return forward;
    }

}
