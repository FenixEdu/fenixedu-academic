/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;

public class CandidacyProcessDocumentUploadBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    DomainReference<IndividualCandidacyProcess> individualCandidacyProcess;
    private IndividualCandidacyDocumentFileType type;
    private transient InputStream stream;
    private long fileSize;
    private String fileName;

    private Long id;

    private byte[] contents;
    
    private java.io.File file;

    public CandidacyProcessDocumentUploadBean() {
	this.id = System.currentTimeMillis();
    }

    public CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType type) {
	this.id = System.currentTimeMillis();
	this.type = type;
    }

    public IndividualCandidacyDocumentFileType getType() {
	return type;
    }

    public void setType(IndividualCandidacyDocumentFileType type) {
	this.type = type;
    }

    public InputStream getStream() throws FileNotFoundException {
	if(file != null) return new FileInputStream(file);
	return this.stream;
    }

    public void setStream(InputStream stream) throws IOException {
	if(stream != null) {
	    this.file = pt.utl.ist.fenix.tools.util.FileUtils.copyToTemporaryFile(stream);
	}
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
	InputStream localStream = getStream();
	if (localStream != null) {
	    try {
		this.contents = new byte[(int) this.fileSize];
		localStream.read(this.contents);
	    } finally {
		localStream.close();
	    }
	}
    }
}
