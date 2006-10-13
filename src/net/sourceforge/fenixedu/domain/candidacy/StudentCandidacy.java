package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.util.EntryPhase;

public abstract class StudentCandidacy extends StudentCandidacy_Base {

    public StudentCandidacy() {
	super();
    }

    protected void init(Person person, ExecutionDegree executionDegree) {
	if (executionDegree == null) {
	    throw new DomainException("execution degree cannot be null");
	}
	if (person == null) {
	    throw new DomainException("person cannot be null");
	}
	if (person.hasStudentCandidacyForExecutionDegree(executionDegree)) {
	    throw new DomainException("error.candidacy.already.created");
	}
	setExecutionDegree(executionDegree);
	setPerson(person);
	setPrecedentDegreeInformation(new PrecedentDegreeInformation());
    }

    public static StudentCandidacy createStudentCandidacy(ExecutionDegree executionDegree,
	    Person studentPerson) {

	switch (executionDegree.getDegree().getTipoCurso()) {

	case BOLONHA_DEGREE:
	    return new DegreeCandidacy(studentPerson, executionDegree);

	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    return new DFACandidacy(studentPerson, executionDegree);

	case BOLONHA_PHD_PROGRAM:
	    return new PHDProgramCandidacy(studentPerson, executionDegree);

	case BOLONHA_INTEGRATED_MASTER_DEGREE:
	    return new IMDCandidacy(studentPerson, executionDegree);

	case BOLONHA_MASTER_DEGREE:
	    return new MDCandidacy(studentPerson, executionDegree);

	case BOLONHA_SPECIALIZATION_DEGREE:
	    return new SDCandidacy(studentPerson, executionDegree);

	default:
	    return null;
	}

    }

    public static Set<StudentCandidacy> readByIds(final List<Integer> studentCandidacyIds) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();

	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		if (studentCandidacyIds.contains(candidacy.getIdInternal())) {
		    result.add((StudentCandidacy) candidacy);
		}
	    }
	}

	return result;
    }

    public static Set<StudentCandidacy> readByExecutionYear(final ExecutionYear executionYear) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
		    result.add(studentCandidacy);
		}
	    }
	}

	return result;
    }

    public static Set<StudentCandidacy> readNotConcludedBy(final ExecutionDegree executionDegree,
	    final ExecutionYear executionYear, final EntryPhase entryPhase) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (!studentCandidacy.isConcluded()
			&& studentCandidacy.getExecutionDegree() == executionDegree
			&& studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear
			&& studentCandidacy.getEntryPhase() != null
			&& studentCandidacy.getEntryPhase().equals(entryPhase)) {
		    result.add(studentCandidacy);
		}
	    }
	}

	return result;
    }
}