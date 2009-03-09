package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class QueueJobResult {

    private Boolean done = Boolean.FALSE;
    private Integer failedCounter;
    private DateTime requestDate;
    private DateTime jobStartTime;
    private DateTime jobEndTime;

    private String contentType;
    private byte[] content;

    private Throwable throwable;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Integer getFailedCounter() {
        return failedCounter;
    }

    public void setFailedCounter(Integer failedCounter) {
        this.failedCounter = failedCounter;
    }

    public DateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(DateTime requestDate) {
        this.requestDate = requestDate;
    }

    public DateTime getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(DateTime jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public DateTime getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(DateTime jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

}
