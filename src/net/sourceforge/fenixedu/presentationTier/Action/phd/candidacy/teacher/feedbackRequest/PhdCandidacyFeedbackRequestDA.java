package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.teacher.feedbackRequest;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcess.UploadCandidacyFeedback;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdCandidacyFeedbackRequest", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

@Forward(name = "uploadCandidacyFeedback", path = "/phd/candidacy/teacher/feedbackRequest/uploadCandidacyFeedback.jsp")

})
public class PhdCandidacyFeedbackRequestDA extends CommonPhdCandidacyDA {

    /*
     * Upload candidacy feedback
     */
    public ActionForward prepareUploadCandidacyFeedback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Person person = getLoggedPerson(request);
	final PhdCandidacyFeedbackRequestProcess feedbackRequest = getProcess(request).getFeedbackRequest();

	final PhdProgramDocumentUploadBean bean = new PhdProgramDocumentUploadBean();
	bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_FEEDBACK_DOCUMENT);
	
	request.setAttribute("documentBean", bean);
	request.setAttribute("canUploadDocuments", feedbackRequest.canUploadDocuments());
	request.setAttribute("sharedDocuments", feedbackRequest.getSharedDocumentsContent());
	request.setAttribute("lastFeedbackDocument", feedbackRequest.getElement(person).getLastFeedbackDocument());

	return mapping.findForward("uploadCandidacyFeedback");
    }

    public ActionForward prepareUploadCandidacyFeedbackInvalid(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	final Person person = getLoggedPerson(request);
	final PhdCandidacyFeedbackRequestProcess feedbackRequest = getProcess(request).getFeedbackRequest();

	request.setAttribute("documentBean", getRenderedObject("documentBean"));
	request.setAttribute("canUploadDocuments", feedbackRequest.canUploadDocuments());
	request.setAttribute("sharedDocuments", feedbackRequest.getSharedDocumentsContent());
	request.setAttribute("lastFeedbackDocument", feedbackRequest.getElement(person).getLastFeedbackDocument());

	return mapping.findForward("uploadCandidacyFeedback");
    }

    public ActionForward uploadCandidacyFeedback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {

	    ExecuteProcessActivity.run(getProcess(request).getFeedbackRequest(), UploadCandidacyFeedback.class,
		    getRenderedObject("documentBean"));

	    addSuccessMessage(request, "message.phd.candidacy.feedback.document.uploaded.with.success");

	} catch (final DomainException e) {
	    addErrorMessage(request, e.getMessage(), e.getArgs());
	    return prepareUploadCandidacyFeedbackInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

    /*
     * End of upload candidacy feedback
     */

    /*
     * Download candidacy feedback request shared documents
     */

    public ActionForward candidacyFeedbackDocumentsDownload(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	final PhdProgramCandidacyProcess process = getProcess(request);
	final Set<PhdProgramProcessDocument> documents = process.getFeedbackRequest().getSharedDocumentsContent();

	if (!documents.isEmpty()) {
	    writeFile(response, getZipDocumentsFilename(process.getIndividualProgramProcess()), PhdDocumentsZip.ZIP_MIME_TYPE,
		    createZip(documents));
	    return null;
	}

	return prepareUploadCandidacyFeedbackInvalid(mapping, actionForm, request, response);
    }
}
