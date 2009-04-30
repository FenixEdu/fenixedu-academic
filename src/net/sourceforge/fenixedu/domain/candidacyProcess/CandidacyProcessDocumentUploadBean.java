/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.candidacyProcess.DocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;

public class CandidacyProcessDocumentUploadBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    DomainReference<IndividualCandidacyProcess> individualCandidacyProcess;
    private DocumentFileType type;
    private transient InputStream stream;
    private long fileSize;
    private String fileName;

    private Long id;

    private byte[] contents;

    public CandidacyProcessDocumentUploadBean() {
	this.id = System.currentTimeMillis();
    }

    public CandidacyProcessDocumentUploadBean(DocumentFileType type) {
	this.id = System.currentTimeMillis();
	this.type = type;
    }

    public DocumentFileType getType() {
	return type;
    }

    public void setType(DocumentFileType type) {
	this.type = type;
    }

    public InputStream getStream() {
	return stream;
    }

    public void setStream(InputStream stream) {
	this.stream = stream;
    }

    public long getFileSize() {
	return fileSize;
    }

    public void setFileSize(long fileSize) {
	this.fileSize = fileSize;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public IndividualCandidacyProcess getIndividualCandidacyProcess() {
	return this.individualCandidacyProcess != null ? this.individualCandidacyProcess.getObject() : null;
    }

    public void setIndividualCandidacyProcess(IndividualCandidacyProcess individualCandidacyProcess) {
	this.individualCandidacyProcess = individualCandidacyProcess != null ? new DomainReference<IndividualCandidacyProcess>(
		individualCandidacyProcess) : null;
    }

    public Long getId() {
	return this.id;
    }

    public byte[] getContents() {
	return this.contents;
    }

    public void setContents(byte[] value) {
	this.contents = value;
    }

    public void fromInputStreamToContents() throws IOException {
	if (this.stream != null) {
	    try {
		
		this.contents = new byte[(int) this.fileSize];
		this.stream.read(this.contents);
	    } finally {
		this.stream.read(this.contents);
		this.stream.close();
	    }
	}
    }
}
