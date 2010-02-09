package net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.academicAdminOffice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.AddPresidentJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.DeleteJuryElement;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryElements;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RequestJuryReviews;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitJuryElementsDocuments;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SwapJuryElementsOrder;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.ValidateJury;
import net.sourceforge.fenixedu.presentationTier.Action.phd.thesis.CommonPhdThesisProcessDA;
import net.sourceforge.fenixedu.presentationTier.docs.phd.thesis.PhdThesisJuryElementsDocument;
import net.sourceforge.fenixedu.util.report.ReportsUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.Pair;

@Mapping(path = "/phdThesisProcess", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "requestJuryElements", path = "/phd/thesis/academicAdminOffice/requestJuryElements.jsp"),

@Forward(name = "submitJuryElementsDocument", path = "/phd/thesis/academicAdminOffice/submitJuryElementsDocument.jsp"),

@Forward(name = "manageThesisJuryElements", path = "/phd/thesis/academicAdminOffice/manageThesisJuryElements.jsp"),

@Forward(name = "rejectJuryElements", path = "/phd/thesis/academicAdminOffice/rejectJuryElements.jsp"),

@Forward(name = "addJuryElement", path = "/phd/thesis/academicAdminOffice/addJuryElement.jsp"),

@Forward(name = "validateJury", path = "/phd/thesis/academicAdminOffice/validateJury.jsp"),

@Forward(name = "submitThesis", path = "/phd/thesis/academicAdminOffice/submitThesis.jsp"),

@Forward(name = "requestJuryReviews", path = "/phd/thesis/academicAdminOffice/requestJuryReviews.jsp"),

@Forward(name = "addPresidentJuryElement", path = "/phd/thesis/academicAdminOffice/addPresidentJuryElement.jsp")

})
public class PhdThesisProcessDA extends CommonPhdThesisProcessDA {

    // Begin thesis jury elements management

    public ActionForward prepareRequestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	request.setAttribute("thesisProcessBean", bean);
	return mapping.findForward("requestJuryElements");
    }

    public ActionForward requestJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    ExecuteProcessActivity.run(getProcess(request), RequestJuryElements.class, getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.requested.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("thesisProcessBean", getRenderedObject("thesisProcessBean"));
	    return mapping.findForward("requestJuryElements");
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    public ActionForward prepareSubmitJuryElementsDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_ELEMENTS));
	bean.addDocument(new PhdProgramDocumentUploadBean(PhdIndividualProgramDocumentType.JURY_PRESIDENT_ELEMENT));
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
		RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
		return submitJuryElementsDocumentInvalid(mapping, actionForm, request, response);
	    }
	    ExecuteProcessActivity.run(getProcess(request), SubmitJuryElementsDocuments.class,
		    getRenderedObject("thesisProcessBean"));
	    addSuccessMessage(request, "message.thesis.jury.elements.added.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    RenderUtils.invalidateViewState("thesisProcessBean.edit.documents");
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

    public ActionForward prepareValidateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdThesisProcessBean bean = new PhdThesisProcessBean();
	bean.setWhenJuryValidated(new LocalDate());

	request.setAttribute("thesisBean", bean);
	return mapping.findForward("validateJury");
    }

    public ActionForward prepareValidateJuryInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
	return mapping.findForward("validateJury");
    }

    public ActionForward validateJury(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), ValidateJury.class, getRenderedObject("thesisBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareValidateJuryInvalid(mapping, actionForm, request, response);
	}

	return manageThesisJuryElements(mapping, actionForm, request, response);
    }

    public ActionForward printJuryElementsDocument(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JRException {

	final PhdThesisJuryElementsDocument report = new PhdThesisJuryElementsDocument(getProcess(request));

	writeFile(response, report.getReportFileName() + ".pdf", "application/pdf", ReportsUtils
		.exportToProcessedPdfAsByteArray(report));

	return null;
    }

    public ActionForward prepareRejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("thesisBean", new PhdThesisProcessBean());
	return mapping.findForward("rejectJuryElements");
    }

    public ActionForward rejectJuryElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    ExecuteProcessActivity.run(getProcess(request), RejectJuryElements.class, getRenderedObject("thesisBean"));

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    request.setAttribute("thesisBean", getRenderedObject("thesisBean"));
	    return mapping.findForward("rejectJuryElements");
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
