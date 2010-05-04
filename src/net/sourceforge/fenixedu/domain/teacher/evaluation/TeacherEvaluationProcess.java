package net.sourceforge.fenixedu.domain.teacher.evaluation;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

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
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public TeacherEvaluationProcess(final FacultyEvaluationProcess facultyEvaluationProcess, final Person evaluee, final Person evaluator) {
	setFacultyEvaluationProcess(facultyEvaluationProcess);
	setEvaluee(evaluee);
	setEvaluator(evaluator);
    }
    
}
