package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class TeacherEvaluationProcess extends TeacherEvaluationProcess_Base {

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_EVALUEE = new Comparator<TeacherEvaluationProcess>() {
	@Override
	public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
	    final int i = Collator.getInstance().compare(p1.getEvaluee().getName(), p2.getEvaluee().getName());
	    return i == 0 ? p2.hashCode() - p1.hashCode() : i;
	}
    };

    public static Comparator<TeacherEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<TeacherEvaluationProcess>() {
	@Override
	public int compare(TeacherEvaluationProcess p1, TeacherEvaluationProcess p2) {
	    return FacultyEvaluationProcess.COMPARATOR_BY_INTERVAL.compare(p1.getFacultyEvaluationProcess(), p2
		    .getFacultyEvaluationProcess());
	}
    };

    public TeacherEvaluationProcess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherEvaluationProcess(final FacultyEvaluationProcess facultyEvaluationProcess, final Person evaluee,
	    final Person evaluator) {
	setFacultyEvaluationProcess(facultyEvaluationProcess);
	setEvaluee(evaluee);
	setEvaluator(evaluator);
    }

    public TeacherEvaluation getCurrentTeacherEvaluation() {
	TeacherEvaluation last = null;
	for (TeacherEvaluation evaluation : getTeacherEvaluationSet()) {
	    if (last == null || evaluation.getCreatedDate().isAfter(last.getCreatedDate())) {
		last = evaluation;
	    }
	}
	return last;
    }

    public TeacherEvaluationState getState() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getState() : getFacultyEvaluationProcess().getState();
    }

    public boolean isAutoEvaluationLocked() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current.getAutoEvaluationLock() != null;
    }

    public String getMark() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getEvaluationMark() : null;
    }

    public TeacherEvaluationType getType() {
	TeacherEvaluation current = getCurrentTeacherEvaluation();
	return current != null ? current.getType() : null;
    }
}
