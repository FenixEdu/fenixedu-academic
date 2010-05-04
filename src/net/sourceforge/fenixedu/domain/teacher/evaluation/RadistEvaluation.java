package net.sourceforge.fenixedu.domain.teacher.evaluation;


public class RadistEvaluation extends RadistEvaluation_Base {
    public RadistEvaluation(TeacherEvaluationProcess process) {
	super();
	setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
	return TeacherEvaluationType.RADIST;
    }
}
