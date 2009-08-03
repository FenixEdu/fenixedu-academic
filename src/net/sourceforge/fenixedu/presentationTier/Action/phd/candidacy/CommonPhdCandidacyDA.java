package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RejectCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.UploadCandidacyReview;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

abstract public class CommonPhdCandidacyDA extends PhdProcessDA {

    @Override
    protected PhdProgramCandidacyProcess getProcess(HttpServletRequest request) {
	return (PhdProgramCandidacyProcess) super.getProcess(request);
    }

    public ActionForward viewIndividualProgramProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return viewIndividualProgramProcess(request, getProcess(request));
    }

    protected ActionForward viewIndividualProgramProcess(HttpServletRequest request, final PhdProgramCandidacyProcess process) {
	return redirect(String.format("/phdIndividualProgramProcess.do?method=viewProcess&processId=%s", process
		.getIndividualProgramProcess().getExternalId()), request);
    }

    public ActionForward manageCandidacyDocuments(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("manageCandidacyDocuments");
    }

    public ActionForward manageCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);

	final PhdProgramCandidacyProcessStateBean stateBean = new PhdProgramCandidacyProcessStateBean();
	stateBean.setState(PhdProgramCandidacyProcessState.WAITING_FOR_CIENTIFIC_COUNCIL_RATIFICATION);

	request.setAttribute("documentToUpload", bean);
	request.setAttribute("stateBean", stateBean);

	return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReviewInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("documentToUpload", getRenderedObject("documentToUpload"));
	request.setAttribute("stateBean", getRenderedObject("stateBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("manageCandidacyReview");
    }

    public ActionForward uploadCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyDocumentUploadBean bean = (PhdCandidacyDocumentUploadBean) getRenderedObject("documentToUpload");

	if (!bean.hasAnyInformation()) {
	    return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
	}

	try {
	    ExecuteProcessActivity.run(getProcess(request), UploadCandidacyReview.class, Collections.singletonList(bean));
	    addSuccessMessage(request, "message.document.uploaded.with.success");

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    bean.removeFile();
	    return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
	}

	RenderUtils.invalidateViewState();
	return manageCandidacyReview(mapping, actionForm, request, response);
    }

    public ActionForward prepareRejectCandidacyProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdProgramCandidacyProcessStateBean stateBean = new PhdProgramCandidacyProcessStateBean();
	request.setAttribute("stateBean", stateBean);
	return mapping.findForward("rejectCandidacyProcess");
    }

    public ActionForward rejectCandidacyProcess(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) getRenderedObject("stateBean");
	    bean.setState(PhdProgramCandidacyProcessState.REJECTED);
	    ExecuteProcessActivity.run(getProcess(request), RejectCandidacyProcess.class, bean);
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward requestRatifyCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    final PhdProgramCandidacyProcess process = getProcess(request);
	    ExecuteProcessActivity.run(process, RequestRatifyCandidacy.class, getRenderedObject("stateBean"));
	    return viewIndividualProgramProcess(request, process);

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
	}
    }

}
