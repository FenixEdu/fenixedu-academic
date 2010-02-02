package net.sourceforge.fenixedu.presentationTier.Action.phd.externalAccess;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess.JuryDocumentsDownload;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdDocumentsZip;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * Serves as entry point to external phd programs access. To add new operations
 * define new types in PhdProcessAccessType enum and define methods here in the
 * following format: prepare<Descriptor> . Each new method will handle an
 * operation in this page
 */

@Mapping(path = "/phdExternalAccess", module = "publico")
@Forwards(tileProperties = @Tile(extend = "definition.phd.external.access"), value = {

@Forward(name = "showOperations", path = "/phd/externalAccess/showOperations.jsp"),

@Forward(name = "juryDocumentsDownload", path = "/phd/externalAccess/juryDocumentsDownload.jsp")

})
public class PhdExternalAccessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("dont-cache-pages-in-search-engines", Boolean.TRUE);
	request.setAttribute("participant", getPhdParticipant(request));

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("showOperations");
    }

    private PhdParticipant getPhdParticipant(HttpServletRequest request) {
	final PhdExternalOperationBean bean = getOperationBean();
	return bean != null ? bean.getParticipant() : PhdParticipant.readByAccessHashCode(getHash(request));
    }

    private PhdExternalOperationBean getOperationBean() {
	final PhdExternalOperationBean bean = (PhdExternalOperationBean) getRenderedObject("operationBean");
	return bean;
    }

    private String getHash(HttpServletRequest request) {
	return (String) getFromRequest(request, "hash");
    }

    // jury document download

    public ActionForward prepareJuryDocumentsDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("operationBean", new PhdExternalOperationBean(getPhdParticipant(request)));

	return mapping.findForward("juryDocumentsDownload");
    }

    public ActionForward prepareJuryDocumentsDownloadInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("operationBean", getOperationBean());

	return mapping.findForward("juryDocumentsDownload");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return getPhdParticipant(request).getIndividualProcess();
    }

    public ActionForward juryDocumentsDownload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	try {

	    final PhdIndividualProgramProcess process = getProcess(request);
	    ExecuteProcessActivity.run(process.getThesisProcess(), JuryDocumentsDownload.class, getOperationBean());
	    writeFile(response, getZipDocumentsFilename(process), PhdDocumentsZip.ZIP_MIME_TYPE, createZip(process));

	    return null;

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return prepareJuryDocumentsDownloadInvalid(mapping, form, request, response);
	}
    }

    private String getZipDocumentsFilename(PhdIndividualProgramProcess process) {
	return process.getProcessNumber().replace("/", "-") + "-Documents.zip";
    }

    protected byte[] createZip(final PhdIndividualProgramProcess process) throws IOException {
	final PhdDocumentsZip zip = new PhdDocumentsZip();

	zip.add(process.getCandidacyProcess().getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.CV));
	zip.add(process.getThesisProcess().getLastestDocumentVersionFor(PhdIndividualProgramDocumentType.PROVISIONAL_THESIS));

	// TODO: add remaining documents here (Abstract /resume)
	return zip.create();
    }

    // end jury document download

}
