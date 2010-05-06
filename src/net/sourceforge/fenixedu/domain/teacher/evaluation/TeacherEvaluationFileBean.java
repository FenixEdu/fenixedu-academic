package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.Serializable;

import net.sourceforge.fenixedu.applicationTier.IUserView;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.security.UserView;

public class TeacherEvaluationFileBean implements Serializable {
    private TeacherEvaluationFileType teacherEvaluationFileType;
    private TeacherEvaluationFile teacherEvaluationFile;

    public TeacherEvaluationFileBean(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType) {
	this.teacherEvaluationFileType = teacherEvaluationFileType;
	for (TeacherEvaluationFile teacherEvaluationFile : teacherEvaluation.getTeacherEvaluationFileSet()) {
	    if (teacherEvaluationFile.getTeacherEvaluationFileType().equals(getTeacherEvaluationFileType())
		    && (this.teacherEvaluationFile == null || this.teacherEvaluationFile.getUploadTime().isBefore(
			    teacherEvaluationFile.getUploadTime()))) {
		this.teacherEvaluationFile = teacherEvaluationFile;
	    }
	}
    }

    public TeacherEvaluationFileType getTeacherEvaluationFileType() {
	return teacherEvaluationFileType;
    }

    public void setTeacherEvaluationFileType(TeacherEvaluationFileType teacherEvaluationFileType) {
	this.teacherEvaluationFileType = teacherEvaluationFileType;
    }

    public TeacherEvaluationFile getTeacherEvaluationFile() {
	return teacherEvaluationFile;
    }

    public void setTeacherEvaluationFile(TeacherEvaluationFile teacherEvaluationFile) {
	this.teacherEvaluationFile = teacherEvaluationFile;
    }

    public DateTime getTeacherEvaluationFileUploadDate() {
	return hasTeacherEvaluationFile() ? teacherEvaluationFile.getUploadTime() : null;
    }

    public boolean hasTeacherEvaluationFile() {
	return teacherEvaluationFile != null;
    }

    public boolean getCanUploadEvaluationFile() {
	IUserView user = UserView.getUser();
	return !hasTeacherEvaluationFile() || teacherEvaluationFile.getCreatedBy().equals(user.getPerson());
    }

}
