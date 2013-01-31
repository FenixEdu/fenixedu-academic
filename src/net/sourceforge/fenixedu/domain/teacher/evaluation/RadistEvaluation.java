package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.HashSet;
import java.util.Set;

public class RadistEvaluation extends RadistEvaluation_Base {
	public RadistEvaluation(TeacherEvaluationProcess process) {
		super();
		setTeacherEvaluationProcess(process);
	}

	@Override
	public TeacherEvaluationType getType() {
		return TeacherEvaluationType.RADIST;
	}

	@Override
	public Set<TeacherEvaluationFileType> getAutoEvaluationFileSet() {
		Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
		teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.AUTO_MULTI_CRITERIA_EXCEL);
		return teacherEvaluationFileTypeSet;
	}

	@Override
	public Set<TeacherEvaluationFileType> getEvaluationFileSet() {
		Set<TeacherEvaluationFileType> teacherEvaluationFileTypeSet = new HashSet<TeacherEvaluationFileType>();
		teacherEvaluationFileTypeSet.add(TeacherEvaluationFileType.MULTI_CRITERIA_EXCEL);
		return teacherEvaluationFileTypeSet;
	}

	@Override
	public String getFilenameTypePrefix() {
		return "mc";
	}

	@Override
	public void copyAutoEvaluation() {
		RadistEvaluation copy = new RadistEvaluation(getTeacherEvaluationProcess());
		internalCopyAutoEvaluation(copy);
	}
}
