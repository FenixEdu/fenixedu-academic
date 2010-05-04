package net.sourceforge.fenixedu.domain.teacher.evaluation;


public class CurricularEvaluation extends CurricularEvaluation_Base {
    public CurricularEvaluation(TeacherEvaluationProcess process) {
	super();
	setTeacherEvaluationProcess(process);
    }

    @Override
    public TeacherEvaluationType getType() {
	return TeacherEvaluationType.CURRICULAR;
    }
}
