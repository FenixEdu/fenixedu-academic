package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public class ReportStudentsUTLCandidatesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionYear executionYear = null;
    private InputStream xlsFile = null;
    private String fileName;
    private Integer fileSize;
    private Boolean forFirstYear;

    public ReportStudentsUTLCandidatesBean() {
    }

    public ExecutionYear getExecutionYear() {
	return executionYear;
    }

    public void setExecutionYear(final ExecutionYear executionYear) {
	this.executionYear = executionYear;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public Integer getFileSize() {
	return fileSize;
    }

    public void setFileSize(Integer fileSize) {
	this.fileSize = fileSize;
    }

    public InputStream getXlsFile() {
	return xlsFile;
    }

    public void setXlsFile(InputStream xlsFile) {
	this.xlsFile = xlsFile;
    }

    public byte[] readXLSContents() throws IOException {
	byte[] contents = new byte[fileSize];
	xlsFile.read(contents);

	return contents;
    }

    public Boolean getForFirstYear() {
	return forFirstYear;
    }

    public void setForFirstYear(Boolean forFirstYear) {
	this.forFirstYear = forFirstYear;
    }

}
