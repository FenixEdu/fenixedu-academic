package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.User;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Interval;

import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class FacultyEvaluationProcess extends FacultyEvaluationProcess_Base {

    public static Comparator<FacultyEvaluationProcess> COMPARATOR_BY_INTERVAL = new Comparator<FacultyEvaluationProcess>() {
	@Override
	public int compare(FacultyEvaluationProcess p1, FacultyEvaluationProcess p2) {
	    if (p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart()) != 0) {
		return p1.getAutoEvaluationInterval().getStart().compareTo(p2.getAutoEvaluationInterval().getStart());
	    } else {
		if (p1.getTitle().compareTo(p2.getTitle()) != 0) {
		    return p1.getTitle().compareTo(p2.getTitle());
		} else {
		    return p1.getExternalId().compareTo(p2.getExternalId());
		}
	    }
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
	final String contents = new String(bytes);
	final String[] lines = contents.split("\n");
	int lineNumber = 0;
	final StringBuilder stringBuilder = new StringBuilder();
	final Set<Person> evaluees = new HashSet<Person>();
	for (final String line : lines) {
	    lineNumber++;
	    final String[] parts = line.split("\t");
	    if (parts.length < 2) {
		throw new DomainException("error.invalid.file.format");
	    }
	    final String evaluee = parts[0];
	    final String evaluator = parts[1];
	    final String coevaluator = parts.length > 2 ? parts[2] : null;
	    final String coevaluatorString = parts.length > 3 ? parts[3].trim() : null;

	    final Person evalueePerson = findPerson(evaluee);
	    final Person evaluatorPerson = findPerson(evaluator);
	    if (evalueePerson == null) {
		appendMessage(stringBuilder, lineNumber, "error.evaluee.not.found", new String[] { evaluee });
	    } else {
		if (evaluees.contains(evalueePerson)) {
		    appendMessage(stringBuilder, lineNumber, "error.evaluee.duplicate", new String[] { evaluee });
		} else {
		    evaluees.add(evalueePerson);
		}
	    }
	    if (evaluatorPerson == null) {
		appendMessage(stringBuilder, lineNumber, "error.evaluator.not.found", new String[] { evaluator });
	    }
	    if (evalueePerson != null && evaluatorPerson != null) {
		final Person coEvaluatorPerson = findPerson(coevaluator);
		TeacherEvaluationProcess existingTeacherEvaluationProcess = null;
		for (final TeacherEvaluationProcess teacherEvaluationProcess : evalueePerson
			.getTeacherEvaluationProcessFromEvalueeSet()) {
		    if (teacherEvaluationProcess.getFacultyEvaluationProcess() == this) {
			existingTeacherEvaluationProcess = teacherEvaluationProcess;
			break;
		    }
		}
		if (existingTeacherEvaluationProcess == null) {
		    existingTeacherEvaluationProcess = new TeacherEvaluationProcess(this, evalueePerson, evaluatorPerson);
		} else {
		    if (evaluatorPerson != existingTeacherEvaluationProcess.getEvaluator()) {
			existingTeacherEvaluationProcess.setEvaluator(evaluatorPerson);

			final TeacherEvaluation teacherEvaluation = existingTeacherEvaluationProcess
				.getCurrentTeacherEvaluation();
			if (teacherEvaluation != null) {
			    teacherEvaluation.copyAutoEvaluation();
			}
		    }
		}

		boolean updatedCoEvaluator = false;
		boolean updatedCoEvaluatorString = false;
		for (final TeacherEvaluationCoEvaluator teacherEvaluationCoEvaluator : existingTeacherEvaluationProcess
			.getTeacherEvaluationCoEvaluatorSet()) {
		    if (teacherEvaluationCoEvaluator instanceof InternalCoEvaluator) {
			final InternalCoEvaluator internalCoEvaluator = (InternalCoEvaluator) teacherEvaluationCoEvaluator;
			updatedCoEvaluator = true;
			if (coEvaluatorPerson == null) {
			    internalCoEvaluator.delete();
			} else {
			    internalCoEvaluator.setPerson(coEvaluatorPerson);
			}
		    } else if (teacherEvaluationCoEvaluator instanceof ExternalCoEvaluator) {
			final ExternalCoEvaluator externalCoEvaluator = (ExternalCoEvaluator) teacherEvaluationCoEvaluator;
			updatedCoEvaluatorString = true;
			if (coevaluatorString == null || coevaluatorString.isEmpty()) {
			    externalCoEvaluator.delete();
			} else {
			    externalCoEvaluator.setName(coevaluatorString);
			}
		    } else {
			throw new DomainException("unknown type: " + teacherEvaluationCoEvaluator.getClass().getName());
		    }
		}
		if (coEvaluatorPerson != null && !updatedCoEvaluator) {
		    new InternalCoEvaluator(existingTeacherEvaluationProcess, coEvaluatorPerson);
		}
		if (coevaluatorString != null && !coevaluatorString.isEmpty() && !updatedCoEvaluatorString) {
		    new ExternalCoEvaluator(existingTeacherEvaluationProcess, coevaluatorString);
		}
	    }
	}
	if (stringBuilder.length() > 0) {
	    throw new DomainException("error.invalid.file.contents", stringBuilder.toString());
	}
    }

    private void appendMessage(final StringBuilder stringBuilder, final int lineNumber, final String key, final String[] args) {
	final String description = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", key, args);
	final String message = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources",
		"error.invalid.file.contents.line", Integer.toString(lineNumber), description);
	stringBuilder.append("\n\t");
	stringBuilder.append(message);
    }

    private Person findPerson(final String string) {
	if (string != null) {
	    final User user = User.readUserByUserUId(string);
	    if (user != null) {
		return user.getPerson();
	    }
	    if (!string.isEmpty() && StringUtils.isNumeric(string)) {
		final int number = Integer.parseInt(string);
		if (number > 0) {
		    final Teacher teacher = Teacher.readByNumber(new Integer(number));
		    return teacher == null ? null : teacher.getPerson();
		}
	    }
	}
	return null;
    }

    public SortedSet<TeacherEvaluationProcess> getSortedTeacherEvaluationProcess() {
	final SortedSet<TeacherEvaluationProcess> result = new TreeSet<TeacherEvaluationProcess>(
		TeacherEvaluationProcess.COMPARATOR_BY_EVALUEE);
	result.addAll(getTeacherEvaluationProcessSet());
	return result;
    }

    public TeacherEvaluationState getState() {
	if (getAutoEvaluationInterval().isAfterNow()) {
	    return null;
	} else {
	    return TeacherEvaluationState.AUTO_EVALUATION;
	}
    }

    public int getAutoEvaluatedCount() {
	int count = 0;
	for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
	    final TeacherEvaluation currentTeacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
	    if (currentTeacherEvaluation != null && currentTeacherEvaluation.getAutoEvaluationLock() != null) {
		count++;
	    }
	}
	return count;
    }

    public int getEvaluatedCount() {
	int count = 0;
	for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
	    final TeacherEvaluation currentTeacherEvaluation = teacherEvaluationProcess.getCurrentTeacherEvaluation();
	    if (currentTeacherEvaluation != null && currentTeacherEvaluation.getEvaluationLock() != null) {
		count++;
	    }
	}
	return count;
    }

    @Service
    public void delete() {
	for (final TeacherEvaluationProcess teacherEvaluationProcess : getTeacherEvaluationProcessSet()) {
	    teacherEvaluationProcess.delete();
	}
	removeRootDomainObject();
	deleteDomainObject();
    }

}
