package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class FileUploadBean implements Serializable {

    private transient InputStream inputStream;
    private String filename;
    private String displayName;
    private byte[] bytes;
    private FacultyEvaluationProcess facultyEvaluationProcess;

    public FileUploadBean() {
    }

    public FileUploadBean(final FacultyEvaluationProcess facultyEvaluationProcess) {
	this();
	this.facultyEvaluationProcess = facultyEvaluationProcess;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public FacultyEvaluationProcess getFacultyEvaluationProcess() {
        return facultyEvaluationProcess;
    }

    public void setFacultyEvaluationProcess(FacultyEvaluationProcess facultyEvaluationProcess) {
        this.facultyEvaluationProcess = facultyEvaluationProcess;
    }

    public void consumeInputStream() throws IOException {
	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	FileUtils.copy(inputStream, byteArrayOutputStream);
	bytes = byteArrayOutputStream.toByteArray();
    }

    @Service
    public void upload() {
	facultyEvaluationProcess.uploadEvaluators(bytes);
    }

}
