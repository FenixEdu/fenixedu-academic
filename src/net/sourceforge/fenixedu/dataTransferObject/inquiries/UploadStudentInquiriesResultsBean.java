package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.InputStream;

abstract public class UploadStudentInquiriesResultsBean {

    protected transient InputStream file;

    protected String keyExecutionCourseHeader;

    protected String keyExecutionDegreeHeader;

    public UploadStudentInquiriesResultsBean() {
	super();
    }

    public InputStream getFile() {
	return file;
    }

    public void setFile(InputStream file) {
	this.file = file;
    }

    public String getKeyExecutionCourseHeader() {
	return keyExecutionCourseHeader;
    }

    public void setKeyExecutionCourseHeader(String keyExecutionCourseHeader) {
	this.keyExecutionCourseHeader = keyExecutionCourseHeader;
    }

    public String getKeyExecutionDegreeHeader() {
	return keyExecutionDegreeHeader;
    }

    public void setKeyExecutionDegreeHeader(String keyExecutionDegreeHeader) {
	this.keyExecutionDegreeHeader = keyExecutionDegreeHeader;
    }

}