package net.sourceforge.fenixedu.domain.teacher.evaluation;


public class NoEvaluation extends NoEvaluation_Base {
    public NoEvaluation(TeacherEvaluationProcess process) {
	super();
	setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
	return TeacherEvaluationType.NO_EVALUATION;
    }

}
