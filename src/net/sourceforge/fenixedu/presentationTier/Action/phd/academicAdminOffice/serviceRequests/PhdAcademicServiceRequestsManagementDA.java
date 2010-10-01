package net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.serviceRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.PhdAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdAcademicServiceRequestManagement", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "listAcademicServiceRequests", path = "/phd/academicAdminOffice/serviceRequests/listAcademicServiceRequests.jsp"),
	@Forward(name = "viewRequest", path = "/phd/academicAdminOffice/serviceRequests/viewRequest.jsp"),
	@Forward(name = "prepareProcessNewState", path = "/phd/academicAdminOffice/serviceRequests/prepareProcessNewState.jsp") })
public class PhdAcademicServiceRequestsManagementDA extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("phdIndividualProgramProcess", getPhdIndividualProgramProcess(request));
	request.setAttribute("phdAademicServiceRequest", getPhdAcademicServiceRequest(request));

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward listAcademicServiceRequests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdIndividualProgramProcess process = getPhdIndividualProgramProcess(request);
	request.setAttribute("phdIndividualProgramProcess", process);

	return mapping.findForward("listAcademicServiceRequests");
    }

    public ActionForward viewAcademicRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
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
	academicServiceRequestCreateBean.createNewRequest();

	return listAcademicServiceRequests(mapping, form, request, response);
    }

    public ActionForward prepareProcessServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.PROCESSING);
    }

    public ActionForward processServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.getAcademicServiceRequest().process();

	return viewAcademicRequest(mapping, form, request, response);
    }

    public ActionForward prepareCancelServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.CANCELLED);
    }

    public ActionForward cancelServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.getAcademicServiceRequest().cancel(academicServiceRequestBean.getJustification());

	return viewAcademicRequest(mapping, form, request, response);
    }

    public ActionForward prepareRejectServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.REJECTED);
    }

    public ActionForward rejectServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.getAcademicServiceRequest().reject(academicServiceRequestBean.getJustification());

	return viewAcademicRequest(mapping, form, request, response);
    }

    public ActionForward prepareConcludeServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return prepareProcessNewState(mapping, form, request, response, AcademicServiceRequestSituationType.REJECTED);
    }

    public ActionForward concludeServiceRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PhdAcademicServiceRequestBean academicServiceRequestBean = getPhdAcademicServiceRequestBean();
	academicServiceRequestBean.getAcademicServiceRequest().conclude();

	return viewAcademicRequest(mapping, form, request, response);
    }

    private ActionForward prepareProcessNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, AcademicServiceRequestSituationType situationType) {
	PhdAcademicServiceRequest academicServiceRequest = getPhdAcademicServiceRequest(request);
	PhdAcademicServiceRequestBean academicServiceRequestBean = new PhdAcademicServiceRequestBean(academicServiceRequest);

	academicServiceRequestBean.setSituationType(situationType);
	request.setAttribute("phdAcademicServiceRequestBean", academicServiceRequestBean);

	return mapping.findForward("prepareProcessNewState");
    }

    public ActionForward processNewState(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("phdAcademicServiceRequestBean", getPhdAcademicServiceRequestBean());
	return mapping.findForward("prepareProcessNewState");
    }

    private PhdAcademicServiceRequestBean getPhdAcademicServiceRequestBean() {
	return (PhdAcademicServiceRequestBean) getObjectFromViewState("phd-academic-service-request-bean");
    }

    private PhdIndividualProgramProcess getPhdIndividualProgramProcess(final HttpServletRequest request) {
	return (PhdIndividualProgramProcess) getDomainObject(request, "phdIndividualCandidacyProcessId");
    }

    private PhdAcademicServiceRequestCreateBean getPhdAcademicServiceRequestCreateBean() {
	return (PhdAcademicServiceRequestCreateBean) getObjectFromViewState("phd-academic-service-request-create-bean");
    }

    private PhdAcademicServiceRequest getPhdAcademicServiceRequest(final HttpServletRequest request) {
	return (PhdAcademicServiceRequest) getDomainObject(request, "phdAcademicServiceRequestId");
    }
}
