package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class StudentCurriculum implements Serializable {

    public static class EnrolmentSet extends TreeSet<Enrolment> {
        private final ExecutionYear executionYear;

        public EnrolmentSet(final ExecutionYear executionYear) {
            super(Enrolment.REVERSE_COMPARATOR_BY_EXECUTION_PERIOD);
            this.executionYear = executionYear;
        }

        @Override
        public boolean add(final Enrolment enrolment) {
            final ExecutionYear enrolmentExecutionYear = enrolment.getExecutionPeriod().getExecutionYear();
            return executionYear == null || executionYear.compareTo(enrolmentExecutionYear) > 0 ?
                    super.add(enrolment) : false;
        }

        @Override
        public boolean addAll(final Collection<? extends Enrolment> enrolments) {
            boolean changed = false;
            for (final Enrolment enrolment : enrolments) {
                changed &= add(enrolment);
            }
            return changed;
        }

        @Override
        public EnrolmentSet clone() {
            final EnrolmentSet clone = new EnrolmentSet(executionYear);
            clone.addAll(this);
            return clone;
        }
    }

    public static class Entry implements Serializable {
	public boolean isExtraCurricularEnrolment() {
	    return false;
	}

	public boolean getExtraCurricularEnrolment() {
	    return isExtraCurricularEnrolment();
	}
    }

    public static class EnrolmentEntry extends Entry {
        private final DomainReference<Enrolment> enrolmentDomainReference;

        public EnrolmentEntry(final Enrolment enrolment) {
            super();
            this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
        }

        public Enrolment getEnrolment() {
            return enrolmentDomainReference.getObject();
        }
    }

    public static class EquivalentEnrolmentEntry extends Entry {
        private final DomainReference<CurricularCourse> curricularCourseDomainReference;
        private final Collection<DomainReference<Enrolment>> enrolmentDomainReferences = new ArrayList<DomainReference<Enrolment>>();

        public EquivalentEnrolmentEntry(final CurricularCourse curricularCourse, final Collection<Enrolment> enrolments) {
            super();
            this.curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
            for (final Enrolment enrolment : enrolments) {
                enrolmentDomainReferences.add(new DomainReference<Enrolment>(enrolment));
            }
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourseDomainReference.getObject();
        }

        public Collection<Enrolment> getEnrolments() {
            final Collection<Enrolment> enrolments = new ArrayList<Enrolment>();
            for (final DomainReference<Enrolment> enrolmentDomainReference : enrolmentDomainReferences) {
                enrolments.add(enrolmentDomainReference.getObject());
            }
            return enrolments;
        }
    }

    public static class ExtraCurricularEntry extends EnrolmentEntry {
	public ExtraCurricularEntry(Enrolment enrolment) {
	    super(enrolment);
	}

	@Override
	public boolean isExtraCurricularEnrolment() {
	    return true;
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

        final EnrolmentSet approvedEnrolments = getApprovedEnrolments(executionYear, registration);

        final Collection<Entry> entries = new HashSet<Entry>();

        addCurricularEnrolments(entries, studentCurricularPlan, approvedEnrolments, executionYear);

        addExtraCurricularEnrolments(entries, studentCurricularPlan, approvedEnrolments);

        return entries;
    }

    private void addCurricularEnrolments(final Collection<Entry> entries, final StudentCurricularPlan studentCurricularPlan,
            final EnrolmentSet approvedEnrolments, final ExecutionYear executionYear) {

        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.isActive(executionYear)) {

                final Enrolment enrolment = findEnrolmentForSameCompetence(approvedEnrolments, curricularCourse);
                if (enrolment != null) {
                    entries.add(new EnrolmentEntry(enrolment));
                    approvedEnrolments.remove(enrolment);
                }

                final Set<Enrolment> enrolments = findEquivalentEnrolments(approvedEnrolments, curricularCourse);
                if (enrolments != null && !enrolments.isEmpty()) {
                    entries.add(new EquivalentEnrolmentEntry(curricularCourse, enrolments));
                    approvedEnrolments.removeAll(enrolments);
                }
            }
        }
    }

    private Set<Enrolment> findEquivalentEnrolments(final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
            final EnrolmentSet workingApprovedEnrolments = approvedEnrolments.clone();
            final Set<Enrolment> enrolments = new HashSet<Enrolment>();
            final Set<CurricularCourse> oldCurricularCourses = curricularCourseEquivalence.getOldCurricularCoursesSet();
            for (final CurricularCourse oldCurricularCourse : oldCurricularCourses) {
                final Enrolment enrolment = findEnrolmentForSameCompetence(approvedEnrolments, oldCurricularCourse);
                if (enrolment == null) {
                    break;
                } else {
                    enrolments.add(enrolment);
                    workingApprovedEnrolments.remove(enrolment);
                }
            }
            if (oldCurricularCourses.size() == enrolments.size()) {
                return enrolments;
            }
        }
        return null;
    }

    private Enrolment findEnrolmentForSameCompetence(final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
        for (final Enrolment enrolment : approvedEnrolments) {
            final CurricularCourse enrolmentCurricularCourse = enrolment.getCurricularCourse();
            final CompetenceCourse enrolmentCompetenceCourse = enrolmentCurricularCourse.getCompetenceCourse();
            if (curricularCourse == enrolmentCurricularCourse || (competenceCourse != null && competenceCourse == enrolmentCompetenceCourse)) {
                return enrolment;
            }
        }
        return null;
    }

    private void addExtraCurricularEnrolments(final Collection<Entry> entries,
	    final StudentCurricularPlan studentCurricularPlan, final Set<Enrolment> approvedEnrolments) {
	for (final Enrolment enrolment : approvedEnrolments) {
	    if (enrolment.getStudentCurricularPlan() == studentCurricularPlan) {
		entries.add(new ExtraCurricularEntry(enrolment));
	    }
	}
    }

    private EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear, final Registration registration) {
        final Student student = registration.getStudent();
        final EnrolmentSet approvedEnrolments = new EnrolmentSet(executionYear);
        student.addApprovedEnrolments(approvedEnrolments);
        return approvedEnrolments;
    }

}
