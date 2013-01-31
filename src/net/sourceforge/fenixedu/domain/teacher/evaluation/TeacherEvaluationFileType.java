package net.sourceforge.fenixedu.domain.teacher.evaluation;

public enum TeacherEvaluationFileType {
	AUTO_ACTIVITY_DESCRIPTION(true), AUTO_CURRICULAR_EVALUATION_EXCEL(true), AUTO_MULTI_CRITERIA_EXCEL(true),
	CURRICULAR_EVALUATION_EXCEL(false), MULTI_CRITERIA_EXCEL(false);

	private boolean autoEvaluationFile;

	private TeacherEvaluationFileType(boolean autoEvaluationFile) {
		this.autoEvaluationFile = autoEvaluationFile;
	}

	public boolean isAutoEvaluationFile() {
		return autoEvaluationFile;
	}

	public String getName() {
		return name();
	}
}
