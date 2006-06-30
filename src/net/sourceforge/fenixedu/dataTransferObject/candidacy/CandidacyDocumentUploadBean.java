/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.candidacy;

import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyDocument;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CandidacyDocumentUploadBean implements Serializable {

    private transient InputStream inputStream;

    private String filename;

    private String fileUploadTime;

    private String documentDescription;

    private DomainReference<CandidacyDocument> candidacyDocument;

    public CandidacyDocumentUploadBean(CandidacyDocument candidacyDocument) {
        super();
        this.documentDescription = candidacyDocument.getDocumentDescription();
        this.fileUploadTime = candidacyDocument.getUploadTime();
        this.candidacyDocument = new DomainReference<CandidacyDocument>(candidacyDocument);
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
        return getDocumentDescription();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public String getFileUploadTime() {
        return fileUploadTime;
    }

}
