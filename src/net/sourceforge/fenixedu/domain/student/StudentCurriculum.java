package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class StudentCurriculum implements Serializable {

    public static class Entry implements Serializable {
	private final DomainReference<Enrolment> enrolmentDomainReference;

	public Entry(final Enrolment enrolment) {
	    super();
	    this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
	}

	public Enrolment getEnrolment() {
	    return enrolmentDomainReference.getObject();
	}

	public boolean isExtraCurricularEnrolment() {
	    return false;
	}

	public boolean getExtraCurricularEnrolment() {
	    return isExtraCurricularEnrolment();
	}
    }

    public static class ExtraCurricularEntry extends Entry {
	public ExtraCurricularEntry(Enrolment enrolment) {
	    super(enrolment);
	}

	@Override
	public boolean isExtraCurricularEnrolment() {
	    return super.isExtraCurricularEnrolment();
	}
    }

    private final DomainReference<Registration> registrationDomainReference;

    public StudentCurriculum(final Registration registration) {
        super();
        if (registration == null) {
            throw new NullPointerException("error.registration.cannot.be.null");
        }
        this.registrationDomainReference = new DomainReference<Registration>(registration);
    }

    public Registration getRegistration() {
        return registrationDomainReference.getObject();
    }

    public Collection<Entry> getCurriculumEntries(final ExecutionYear executionYear) {
        final Registration registration = getRegistration();
        final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final Student student = registration.getStudent();
        final Set<Enrolment> approvedEnrolments = student.getApprovedEnrolments();

        final Collection<Entry> entries = new HashSet<Entry>();

        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

        addExtraCurricularEnrolments(entries, studentCurricularPlan, approvedEnrolments);

        return entries;
    }

    private void addExtraCurricularEnrolments(final Collection<Entry> entries,
	    final StudentCurricularPlan studentCurricularPlan, final Set<Enrolment> approvedEnrolments) {
	for (final Enrolment enrolment : approvedEnrolments) {
	    if (enrolment.getStudentCurricularPlan() == studentCurricularPlan) {
		entries.add(new ExtraCurricularEntry(enrolment));
	    }
	}
    }

}
