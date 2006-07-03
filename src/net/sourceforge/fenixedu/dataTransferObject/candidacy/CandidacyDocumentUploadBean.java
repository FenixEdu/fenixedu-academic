/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.InputStream;
import java.io.Serializable;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;
import net.sourceforge.fenixedu.util.LabelFormatter;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CandidacyDocumentUploadBean implements Serializable {

    private transient InputStream inputStream;

    private String filename;

    private DateTime fileUploadTime;

    private String documentDescription;

    private DomainReference<CandidacyDocument> candidacyDocument;

    public CandidacyDocumentUploadBean(CandidacyDocument candidacyDocument) {
        super();
        this.documentDescription = candidacyDocument.getDocumentDescription();
        this.fileUploadTime = candidacyDocument.getFileUploadTime();
        this.candidacyDocument = new DomainReference<CandidacyDocument>(candidacyDocument);
        this.filename = (candidacyDocument.getFile() != null) ? candidacyDocument.getFile()
                .getFilename() : null;
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

    public String getFileUploadTime() {

        if (fileUploadTime != null) {
            return fileUploadTime.toString("dd/MM/yyyy hh:mm");
        }

        return new LabelFormatter().appendLabel("label." + "file.not.uploaded.yet",
                "resources.CandidateResources").toString();
    }

    public boolean getIsFileUploaded() {
        return fileUploadTime != null;
    }

}
