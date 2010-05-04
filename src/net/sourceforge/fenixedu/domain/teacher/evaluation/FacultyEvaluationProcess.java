package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcess extends FacultyEvaluationProcess_Base {

    public static Comparator<FacultyEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<FacultyEvaluationProcess>() {
	@Override
	public int compare(FacultyEvaluationProcess p1, FacultyEvaluationProcess p2) {
	    return p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart());
	}
    };

    public FacultyEvaluationProcess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FacultyEvaluationProcess(final MultiLanguageString title, final Interval autoEvaluationInterval,
	    final Interval evaluationInterval) {
	this();
	setTitle(title);
	setAutoEvaluationInterval(autoEvaluationInterval);
	setEvaluationInterval(evaluationInterval);
    }

    public void uploadEvaluators(final byte[] bytes) {
	if (!getAutoEvaluationInterval().isAfterNow()) {
	    throw new DomainException("Evaluation process is already under way.");
	}
	final String contents = new String(bytes);
	final String[] lines = contents.split("\n");
	for (final String line : lines) {
	    final String[] parts = line.split("\t");
	    final String evaluee = parts[0];
	    final String evaluator = parts[1];
	    final Person evalueePerson = findPerson(evaluee);
	    final Person evaluatorPerson = findPerson(evaluator);
	    TeacherEvaluationProcess existingTeacherEvaluationProcess = null;
	    for (final TeacherEvaluationProcess teacherEvaluationProcess : evalueePerson.getTeacherEvaluationProcessFromEvalueeSet()) {
		if (teacherEvaluationProcess.getFacultyEvaluationProcess() == this) {
		    existingTeacherEvaluationProcess = teacherEvaluationProcess;
		    break;
		}
	    }
	    if (existingTeacherEvaluationProcess == null) {
		new TeacherEvaluationProcess(this, evalueePerson, evaluatorPerson);
	    } else {
		existingTeacherEvaluationProcess.setEvaluator(evaluatorPerson);
	    }
	}
    }

    private Person findPerson(final String evaluee) {
	final User user = User.readUserByUserUId(evaluee);
	if (user != null) {
	    return user.getPerson();
	}
	if (StringUtils.isNumeric(evaluee)) {
	    final Teacher teacher = Teacher.readByNumber(new Integer(evaluee));
	    return teacher == null ? null : teacher.getPerson();
	}
	return null;
    }

}
