package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.AddPresidentJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.DeleteJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.SubmitJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.SwapJuryElementsOrder;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Pair;

@Mapping(path = "/phdThesisProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "submitJuryElementsDocument", path = "/phd/thesis/academicAdminOffice/submitJuryElementsDocument.jsp"),

@Forward(name = "manageThesisJuryElements", path = "/phd/thesis/academicAdminOffice/manageThesisJuryElements.jsp"),

@Forward(name = "addJuryElement", path = "/phd/thesis/academicAdminOffice/addJuryElement.jsp"),

@Forward(name = "submitThesis", path = "/phd/thesis/academicAdminOffice/submitThesis.jsp"),

@Forward(name = "requestJuryReviews", path = "/phd/thesis/academicAdminOffice/requestJuryReviews.jsp"),

@Forward(name = "addPresidentJuryElement", path = "/phd/thesis/academicAdminOffice/addPresidentJuryElement.jsp")

})
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    // Begin thesis jury elements management

    public ActionForward prepareSubmitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_ELEMENTS));
	request.setAttribute("thesisProcessBean", bean);

	return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocumentInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
	return mapping.findForward("submitJuryElementsDocument");
    }

    public ActionForward submitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    final IViewState viewState = RenderUtils.getViewState("thesisProcessBean.edit.documents");
	    if (!viewState.isValid()) {
		return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
	    }
	    ExecuteProcessActivity.run(getProcess(request), SubmitJuryElements.class, getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.added.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward manageThesisJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("manageThesisJuryElements");
    }

    public ActionForward prepareAddJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
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

    public ActionForward deleteJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), DeleteJuryElement.class, getJuryElement(request));
	    addSuccessMessage(request, "message.thesis.jury.removed");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    private ThesisJuryElement getJuryElement(HttpServletRequest request) {
	return getDomainObject(request, "juryElementId");
    }

    private void swapJuryElements(HttpServletRequest request, ThesisJuryElement e1, ThesisJuryElement e2) {
	try {
	    ExecuteProcessActivity.run(getProcess(request), SwapJuryElementsOrder.class,
		    new Pair<ThesisJuryElement, ThesisJuryElement>(e1, e2));
	    addSuccessMessage(request, "message.thesis.jury.element.swapped");
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	}
    }

    public ActionForward moveUp(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement lower = process.getOrderedThesisJuryElements().lower(juryElement);

	if (lower != null) {
	    swapJuryElements(request, juryElement, lower);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveDown(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement higher = process.getOrderedThesisJuryElements().higher(juryElement);

	if (higher != null) {
	    swapJuryElements(request, juryElement, higher);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement first = process.getOrderedThesisJuryElements().first();

	if (juryElement != first) {
	    swapJuryElements(request, juryElement, first);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcess process = getProcess(request);
	final ThesisJuryElement juryElement = getJuryElement(request);
	final ThesisJuryElement last = process.getOrderedThesisJuryElements().last();

	if (juryElement != last) {
	    swapJuryElements(request, juryElement, last);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward prepareAddPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", new PhdThesisJuryElementBean(getProcess(request)));
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementInvalid(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward prepareAddPresidentJuryElementPostback(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	request.setAttribute("thesisJuryElementBean", getRenderedObject("thesisJuryElementBean"));
	RenderUtils.invalidateViewState();
	return mapping.findForward("addPresidentJuryElement");
    }

    public ActionForward addPresidentJuryElement(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), AddPresidentJuryElement.class,
		    getRenderedObject("thesisJuryElementBean"));
	    addSuccessMessage(request, "message.thesis.added.jury.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareAddPresidentJuryElementInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    // end thesis jury elements management

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
