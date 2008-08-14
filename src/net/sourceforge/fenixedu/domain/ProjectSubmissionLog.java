package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.joda.time.DateTime;

public class ProjectSubmissionLog extends ProjectSubmissionLog_Base {

    public static Comparator<ProjectSubmissionLog> COMPARATOR_BY_MOST_RECENT_SUBMISSION_DATE = new Comparator<ProjectSubmissionLog>() {
	public int compare(ProjectSubmissionLog leftProjectSubmissionLog, ProjectSubmissionLog rightProjectSubmissionLog) {
	    int comparationResult = leftProjectSubmissionLog.getSubmissionDateTime().compareTo(
		    rightProjectSubmissionLog.getSubmissionDateTime());
	    return (comparationResult == 0) ? leftProjectSubmissionLog.getIdInternal().compareTo(
		    rightProjectSubmissionLog.getIdInternal()) : -(comparationResult);
	}
    };

    public ProjectSubmissionLog() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
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

}
