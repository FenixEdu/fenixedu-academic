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
        final Student student = registration.getStudent();
        final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final Set<Enrolment> approvedEnrolments = student.getApprovedEnrolments();
        

        final Collection<Entry> entries = new HashSet<Entry>();
        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        return entries;
    }

}
