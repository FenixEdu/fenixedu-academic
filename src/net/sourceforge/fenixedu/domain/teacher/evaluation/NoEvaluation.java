package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.HashSet;
import java.util.Set;

public class NoEvaluation extends NoEvaluation_Base {
	public NoEvaluation(TeacherEvaluationProcess process) {
		super();
		setTeacherEvaluationProcess(process);
	}

	@Override
	public TeacherEvaluationType getType() {
		return TeacherEvaluationType.NO_EVALUATION;
	}

	@Override
	public Set<TeacherEvaluationFileType> getAutoEvaluationFileSet() {
		return new HashSet<TeacherEvaluationFileType>();
	}

	@Override
	public Set<TeacherEvaluationFileType> getEvaluationFileSet() {
		return new HashSet<TeacherEvaluationFileType>();
	}

	@Override
	protected void internalLickingBusiness() {
		super.internalLickingBusiness();
		super.lickEvaluationStamp();
		setEvaluationMark(TeacherEvaluationMark.GOOD);
	}

	@Override
	public String getFilenameTypePrefix() {
		return "";
	}

	@Override
	public void copyAutoEvaluation() {
		NoEvaluation copy = new NoEvaluation(getTeacherEvaluationProcess());
		internalCopyAutoEvaluation(copy);
	}
}
