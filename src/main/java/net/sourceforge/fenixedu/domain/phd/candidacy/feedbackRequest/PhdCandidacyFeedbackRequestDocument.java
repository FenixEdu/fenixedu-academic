package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;

import org.apache.commons.lang.StringUtils;

public class PhdCandidacyFeedbackRequestDocument extends PhdCandidacyFeedbackRequestDocument_Base {

    private PhdCandidacyFeedbackRequestDocument() {
        super();
    }

    public PhdCandidacyFeedbackRequestDocument(PhdCandidacyFeedbackRequestElement element, String remarks, byte[] content,
            String filename, Person uploader) {
        this();
        Object obj = element.getProcess();
        String[] args = {};

        // first set jury element and then init document
        if (obj == null) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.candidacyProcess.cannot.be.null", args);
        }
        String[] args1 = {};
        if (element == null) {
            throw new DomainException("error.PhdCandidacyFeedbackRequestDocument.invalid.element", args1);
        }
        setElement(element);

        init(element.getProcess(), PhdIndividualProgramDocumentType.CANDIDACY_FEEDBACK_DOCUMENT, remarks, content, filename,
                uploader);
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
                super.setDocumentVersion(getElement().getFeedbackDocumentsSet().size() + 1);
            }
        } else {
            super.setDocumentVersion(1);
        }
    }

    @Override
    public boolean isLast() {
        return getElement().getLastFeedbackDocument() == this;
    }

    @Override
    public PhdProgramProcessDocument getLastVersion() {
        return getElement().getLastFeedbackDocument();
    }

    @Deprecated
    public boolean hasElement() {
        return getElement() != null;
    }

}
