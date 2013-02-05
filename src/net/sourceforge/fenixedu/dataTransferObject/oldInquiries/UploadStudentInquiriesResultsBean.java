package net.sourceforge.fenixedu.dataTransferObject.oldInquiries;

import java.io.InputStream;

import org.joda.time.LocalDate;

abstract public class UploadStudentInquiriesResultsBean {

    protected transient InputStream file;

    protected String keyExecutionCourseHeader;

    protected String keyExecutionDegreeHeader;

    protected LocalDate resultsDate;

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

    public LocalDate getResultsDate() {
        return resultsDate;
    }

    public void setResultsDate(LocalDate resultsDate) {
        this.resultsDate = resultsDate;
    }
}