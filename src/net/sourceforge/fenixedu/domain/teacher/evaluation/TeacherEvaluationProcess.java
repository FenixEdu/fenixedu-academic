package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;

public class TeacherEvaluationProcess extends TeacherEvaluationProcess_Base {
    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<TeacherEvaluationProcess>() {
	@Override
	public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
	    return FacultyEvaluationProcess.COMPARATOR_BY_INTERVAL.compare(p1.getFacultyEvaluationProcess(), p2
		    .getFacultyEvaluationProcess());
	}
    };

    public TeacherEvaluationProcess() {
	super();
    }
}
