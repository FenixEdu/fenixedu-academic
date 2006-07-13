/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocumentFile;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CandidacyDocumentUploadBean implements Serializable {

    private transient InputStream inputStream;

    private String filename;

    private String documentDescription;

    private String actualFile;

    private DomainReference<CandidacyDocument> candidacyDocument;

    public CandidacyDocumentUploadBean(CandidacyDocument candidacyDocument) {
        super();
        CandidacyDocumentFile file = candidacyDocument.getFile();
        this.documentDescription = candidacyDocument.getDocumentDescription();
        this.actualFile = (file != null) ? file.getFilename() + " - "
                + file.getUploadTime().toString("dd/MM/yyyy hh:mm") : null;
        this.candidacyDocument = new DomainReference<CandidacyDocument>(candidacyDocument);
    }

    public String getActualFile() {
        return (actualFile == null) ? new LabelFormatter().appendLabel("label.file.not.uploaded.yet",
                "resources.CandidateResources").toString() : actualFile;
    }

    public CandidacyDocument getCandidacyDocument() {
        return candidacyDocument.getObject();
    }

    public InputStream getFileInputStream() {
        return inputStream;
    }

    public void setFileInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDocumentDescription() {
        return new LabelFormatter().appendLabel("label." + documentDescription,
                "resources.CandidateResources").toString();
    }

    public boolean getIsFileUploaded() {
        return actualFile != null;
    }

}
