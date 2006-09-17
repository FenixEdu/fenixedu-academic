package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public abstract class StudentCandidacy extends StudentCandidacy_Base {

    public StudentCandidacy() {
	super();
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

    public static Set<StudentCandidacy> readByExecutionDegreeAndExecutionYear(
	    final ExecutionDegree executionDegree, final ExecutionYear executionYear) {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final Candidacy candidacy : RootDomainObject.getInstance().getCandidaciesSet()) {
	    if (candidacy instanceof StudentCandidacy) {
		final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.getExecutionDegree() == executionDegree
			&& studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
		    result.add(studentCandidacy);
		}
	    }
	}

	return result;
    }
}