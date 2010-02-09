package net.sourceforge.fenixedu.domain.phd;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdThesisReportFeedbackDocument extends PhdThesisReportFeedbackDocument_Base {

    private PhdThesisReportFeedbackDocument() {
	super();
    }

    public PhdThesisReportFeedbackDocument(ThesisJuryElement element, PhdIndividualProgramDocumentType documentType,
	    String remarks, byte[] content, String filename, Person uploader) {
	this();

	// first set jury element and then init document
	check(element, "error.PhdThesisReportFeedbackDocument.invalid.element");
	setJuryElement(element);

	init(null, documentType, remarks, content, filename, uploader);
    }

    @Override
    protected void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, byte[] content,
	    String filename, Person uploader) {

	if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
	    throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
	}

    }

    @Override
    protected void setDocumentVersion(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType) {
	if (documentType.isVersioned()) {
	    if (process != null) {
		final Set<PhdProgramProcessDocument> documentsByType = process.getDocumentsByType(documentType);
		super.setDocumentVersion(documentsByType.isEmpty() ? 1 : documentsByType.size() + 1);

	    } else {
		super.setDocumentVersion(getJuryElement().getFeedbackDocumentsCount() + 1);
	    }
	} else {
	    super.setDocumentVersion(1);
	}
    }

    /**
     * <pre>
     * Format /PhdIndividualProgram/{processId}
     * </pre>
     * 
     * @return
     */
    protected VirtualPath getVirtualPath() {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("PhdIndividualProgram", "PhdIndividualProgram"));
	filePath.addNode(new VirtualPathNode(getJuryElement().getProcess().getIndividualProgramProcess().getIdInternal()
		.toString(), getJuryElement().getProcess().getIndividualProgramProcess().getIdInternal().toString()));
	return filePath;
    }

    @Service
    public void associateToProcess(final PhdThesisProcess process) {
	check(process, "error.phd.PhdProgramProcessDocument.process.cannot.be.null");
	setPhdProgramProcess(process);
    }

    @Override
    protected void disconnect() {
	super.disconnect();
	removeJuryElement();
    }

    public boolean isAssignedToProcess() {
	return hasPhdProgramProcess();
    }

}
