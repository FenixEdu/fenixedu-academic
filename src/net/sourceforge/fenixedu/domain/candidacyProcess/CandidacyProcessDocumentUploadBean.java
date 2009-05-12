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
    
    private DomainReference<IndividualCandidacyDocumentFile> documentFile;

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
	return this.stream;
    }

    public void setStream(InputStream stream) throws IOException {
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
    
    public IndividualCandidacyDocumentFile getDocumentFile() {
	return this.documentFile != null ? this.documentFile.getObject() : null;
    }
    
    public void setDocumentFile(IndividualCandidacyDocumentFile documentFile) {
	this.documentFile = documentFile != null ? new DomainReference<IndividualCandidacyDocumentFile>(documentFile) : null;
    }
}
