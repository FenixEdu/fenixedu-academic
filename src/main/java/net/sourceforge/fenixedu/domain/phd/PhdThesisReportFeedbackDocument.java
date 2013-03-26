package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class PhdThesisReportFeedbackDocument extends PhdThesisReportFeedbackDocument_Base {

    private PhdThesisReportFeedbackDocument() {
        super();
    }

    public PhdThesisReportFeedbackDocument(ThesisJuryElement element, String remarks, byte[] content, String filename,
            Person uploader) {
        this();
        String[] args = {};

        // first set jury element and then init document
        if (element == null) {
            throw new DomainException("error.PhdThesisReportFeedbackDocument.invalid.element", args);
        }
        setJuryElement(element);

        init(element.getProcess(), PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK, remarks, content, filename, uploader);
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
                super.setDocumentVersion(process.getLastVersionNumber(documentType) + 1);

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
    @Override
    protected VirtualPath getVirtualPath() {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("PhdIndividualProgram", "PhdIndividualProgram"));
        filePath.addNode(new VirtualPathNode(getJuryElement().getProcess().getIndividualProgramProcess().getIdInternal()
                .toString(), getJuryElement().getProcess().getIndividualProgramProcess().getIdInternal().toString()));
        return filePath;
    }

    @Override
    protected void disconnect() {
        removeJuryElement();
        super.disconnect();
    }

    public boolean isAssignedToProcess() {
        return hasPhdProgramProcess();
    }

    @Override
    public boolean isLast() {
        return getJuryElement().getLastFeedbackDocument() == this;
    }

    @Override
    public PhdProgramProcessDocument getLastVersion() {
        return getJuryElement().getLastFeedbackDocument();
    }
}
