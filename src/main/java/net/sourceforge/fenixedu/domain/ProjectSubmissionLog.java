package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ProjectSubmissionLog extends ProjectSubmissionLog_Base {

    public static Comparator<ProjectSubmissionLog> COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE =
            new Comparator<ProjectSubmissionLog>() {
                @Override
                public int compare(ProjectSubmissionLog leftProjectSubmissionLog, ProjectSubmissionLog rightProjectSubmissionLog) {
                    int comparationResult =
                            leftProjectSubmissionLog.getSubmissionDateTime().compareTo(
                                    rightProjectSubmissionLog.getSubmissionDateTime());
                    return (comparationResult == 0) ? leftProjectSubmissionLog.getExternalId().compareTo(
                            rightProjectSubmissionLog.getExternalId()) : -(comparationResult);
                }
            };

    public ProjectSubmissionLog() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ProjectSubmissionLog(DateTime submissionDateTime, String filename, String fileMimeType, String fileChecksum,
            String fileChecksumAlgorithm, Integer fileSize, StudentGroup studentGroup, Attends attends, Project project) {
        this();
        setSubmissionDateTime(submissionDateTime);
        setFilename(filename);
        setFileMimeType(fileMimeType);
        setFileChecksum(fileChecksum);
        setFileChecksumAlgorithm(fileChecksumAlgorithm);
        setFileSize(fileSize);
        setStudentGroup(studentGroup);
        setAttends(attends);
        setProject(project);
    }

    @Deprecated
    public java.util.Date getSubmission() {
        org.joda.time.DateTime dt = getSubmissionDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setSubmission(java.util.Date date) {
        if (date == null) {
            setSubmissionDateTime(null);
        } else {
            setSubmissionDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public boolean hasFileSize() {
        return getFileSize() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSubmissionDateTime() {
        return getSubmissionDateTime() != null;
    }

    @Deprecated
    public boolean hasProject() {
        return getProject() != null;
    }

    @Deprecated
    public boolean hasFileMimeType() {
        return getFileMimeType() != null;
    }

    @Deprecated
    public boolean hasFileChecksumAlgorithm() {
        return getFileChecksumAlgorithm() != null;
    }

    @Deprecated
    public boolean hasFileChecksum() {
        return getFileChecksum() != null;
    }

    @Deprecated
    public boolean hasAttends() {
        return getAttends() != null;
    }

    @Deprecated
    public boolean hasFilename() {
        return getFilename() != null;
    }

    @Deprecated
    public boolean hasStudentGroup() {
        return getStudentGroup() != null;
    }

}
