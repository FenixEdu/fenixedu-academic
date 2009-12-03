package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.SubmitThesis;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdThesisProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "manageThesisJuryElements", path = "/phd/thesis/academicAdminOffice/manageThesisJuryElements.jsp"),

@Forward(name = "addJuryElement", path = "/phd/thesis/academicAdminOffice/addJuryElement.jsp"),

@Forward(name = "submitThesis", path = "/phd/thesis/academicAdminOffice/submitThesis.jsp"),

@Forward(name = "requestJuryReviews", path = "/phd/thesis/academicAdminOffice/requestJuryReviews.jsp")

})
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    public ActionForward manageThesisJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("manageThesisJuryElements");
    }

    public ActionForward prepareAddJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean());
	return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	return mapping.findForward("addJuryElement");
    }

    public ActionForward prepareAddJuryElementPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("addJuryElement");
    }

    public ActionForward addJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddJuryElement.class, getRenderedObject("thesisJuryElementBean"));
	    addSuccessMessage(request, "message.thesis.added.jury.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareAddJuryElementInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    // Submit thesis
    public ActionForward prepareSubmitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS));
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.FINAL_THESIS));

	request.setAttribute("submitThesisBean", bean);

	return mapping.findForward("submitThesis");
    }

    public ActionForward prepareSubmitThesisInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	RenderUtils.invalidateViewState("submitThesisBean.edit.documents");

	request.setAttribute("submitThesisBean", getRenderedObject("submitThesisBean"));

	return mapping.findForward("submitThesis");
    }

    public ActionForward submitThesis(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), SubmitThesis.class, getRenderedObject("submitThesisBean"));
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareSubmitThesisInvalid(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of submit thesis

    // Request Jury Reviews
    public ActionForward prepareRequestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("requestJuryReviewsBean", new PhdThesisProcessBean());

	return mapping.findForward("requestJuryReviews");
    }

    public ActionForward prepareRequestJuryReviewsInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("requestJuryReviewsBean", getRenderedObject("requestJuryReviewsBean"));

	return mapping.findForward("requestJuryReviews");
    }

    public ActionForward requestJuryReviews(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    ExecuteProcessActivity
		    .run(getProcess(request), RequestJuryReviews.class, getRenderedObject("requestJuryReviewsBean"));
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareRequestJuryReviewsInvalid(mapping, form, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));

    }

    // End of Request Jury Reviews

}
