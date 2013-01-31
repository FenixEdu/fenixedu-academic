package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.Serializable;
import java.util.Comparator;

import org.joda.time.DateTime;

public class TeacherEvaluationFileBean implements Serializable {
	public static final Comparator<TeacherEvaluationFileBean> COMPARATOR_BY_TYPE = new Comparator<TeacherEvaluationFileBean>() {
		@Override
		public int compare(TeacherEvaluationFileBean b1, TeacherEvaluationFileBean b2) {
			if (b1.getTeacherEvaluationFileType().compareTo(b2.getTeacherEvaluationFileType()) != 0) {
				return b1.getTeacherEvaluationFileType().compareTo(b2.getTeacherEvaluationFileType());
			} else if (b1.getTeacherEvaluationFile() != null && b2.getTeacherEvaluationFile() != null) {
				return b1.getTeacherEvaluationFile().getExternalId().compareTo(b2.getTeacherEvaluationFile().getExternalId());
			} else if (b1.getTeacherEvaluationFile() != null && b2.getTeacherEvaluationFile() == null) {
				return 1;
			} else {
				return -1;
			}
		}
	};

	private TeacherEvaluationFileType teacherEvaluationFileType;
	private TeacherEvaluationFile teacherEvaluationFile;
	private final TeacherEvaluation teacherEvaluation;

	public TeacherEvaluationFileBean(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType) {
		this.teacherEvaluationFileType = teacherEvaluationFileType;
		this.teacherEvaluation = teacherEvaluation;
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

	public boolean getCanUploadAutoEvaluationFile() {
		return teacherEvaluationFileType.isAutoEvaluationFile()
				&& teacherEvaluation.getTeacherEvaluationProcess().isInAutoEvaluation();
	}

	public boolean getCanUploadEvaluationFile() {
		return !teacherEvaluationFileType.isAutoEvaluationFile()
				&& teacherEvaluation.getTeacherEvaluationProcess().isInEvaluation();
	}

}
