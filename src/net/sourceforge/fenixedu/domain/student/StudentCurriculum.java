package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
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
import net.sourceforge.fenixedu.domain.curriculum.GradeType;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

import org.joda.time.YearMonthDay;

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

    public static abstract class Entry implements Serializable {
	public abstract double getEctsCredits();

	public boolean isEnrolmentEntry() {
	    return false;
	}
	public boolean getIsEnrolmentEntry() {
	    return isEnrolmentEntry();
	}
	public boolean isNotNeedToEnrolEntry() {
	    return false;
	}
	public boolean getIsNotNeedToEnrolEntry() {
	    return isNotNeedToEnrolEntry();
	}
	public boolean isEquivalentEnrolmentEntry() {
	    return false;
	}
	public boolean getIsEquivalentEnrolmentEntry() {
	    return isEquivalentEnrolmentEntry();
	}
	public boolean isExtraCurricularEnrolmentEntry() {
	    return false;
	}
	public boolean getIsExtraCurricularEnrolmentEntry() {
	    return isExtraCurricularEnrolmentEntry();
	}

	protected double ectsCredits(final CurricularCourse curricularCourse) {
	    final double ectsCredits = curricularCourse.getEctsCredits().doubleValue();
	    return ectsCredits == 0 ? 6.0 : ectsCredits;
	}
    }

    public static abstract class SimpleEntry extends Entry {
	private final DomainReference<CurricularCourse> curricularCourseDomainReference;

        public SimpleEntry(final CurricularCourse curricularCourse) {
            super();
            this.curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
        }

        public CurricularCourse getCurricularCourse() {
            return curricularCourseDomainReference.getObject();
        }
    }

    public static class EnrolmentEntry extends SimpleEntry {
        private final DomainReference<Enrolment> enrolmentDomainReference;

        public EnrolmentEntry(final CurricularCourse curricularCourse, final Enrolment enrolment) {
            super(curricularCourse);
            this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
        }

        @Override
	public boolean isEnrolmentEntry() {
            return true;
	}

        public Enrolment getEnrolment() {
            return enrolmentDomainReference.getObject();
        }

	@Override
	public double getEctsCredits() {
	    final Enrolment enrolment = getEnrolment();
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    return ectsCredits(curricularCourse);
	}
    }

    public static class NotNeedToEnrolEntry extends SimpleEntry {
	private final DomainReference<NotNeedToEnrollInCurricularCourse> notNeedToEnrolDomainReference;

	public NotNeedToEnrolEntry(final CurricularCourse curricularCourse, final NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse) {
	    super(curricularCourse);
	    this.notNeedToEnrolDomainReference = new DomainReference<NotNeedToEnrollInCurricularCourse>(notNeedToEnrollInCurricularCourse);
	}

	@Override
	public boolean isNotNeedToEnrolEntry() {
	    return true;
	}

	public NotNeedToEnrollInCurricularCourse getNotNeedToEnrol() {
	    return notNeedToEnrolDomainReference.getObject();
	}

	@Override
	public double getEctsCredits() {
	    final NotNeedToEnrollInCurricularCourse notNeedToEnroll = getNotNeedToEnrol();
	    final CurricularCourse curricularCourse = notNeedToEnroll.getCurricularCourse();
	    return ectsCredits(curricularCourse);
	}
    }

    public static class EquivalentEnrolmentEntry extends Entry {
        private final DomainReference<CurricularCourse> curricularCourseDomainReference;
        private final Set<SimpleEntry> entries;

        public EquivalentEnrolmentEntry(final CurricularCourse curricularCourse, final Set<SimpleEntry> simpleEntries) {
            super();
            this.curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
            this.entries = new HashSet<SimpleEntry>(simpleEntries);
        }

        @Override
	public boolean isEquivalentEnrolmentEntry() {
            return true;
	}

	public CurricularCourse getCurricularCourse() {
            return curricularCourseDomainReference.getObject();
        }
	public Set<SimpleEntry> getEntries() {
	    return entries;
	}

	@Override
	public double getEctsCredits() {
	    double ectsCredits = 0;
	    for (final SimpleEntry simpleEntry : getEntries()) {
		ectsCredits += ectsCredits(simpleEntry.getCurricularCourse());
	    }
	    return ectsCredits;
	}
    }

    public static class ExtraCurricularEnrolmentEntry extends Entry {
        private final DomainReference<Enrolment> enrolmentDomainReference;

        public ExtraCurricularEnrolmentEntry(final Enrolment enrolment) {
            super();
            this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
        }

        @Override
        public boolean isExtraCurricularEnrolmentEntry() {
            return true;
        }

        public Enrolment getEnrolment() {
            return enrolmentDomainReference.getObject();
        }

	@Override
	public double getEctsCredits() {
	    final Enrolment enrolment = getEnrolment();
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    return ectsCredits(curricularCourse);
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
        final StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(executionYear);
        if (studentCurricularPlan == null) {
            return null;
        }

        final EnrolmentSet approvedEnrolments = getApprovedEnrolments(executionYear);

        final Collection<Entry> entries = new HashSet<Entry>();

        addCurricularEnrolments(entries, studentCurricularPlan, approvedEnrolments, executionYear);

        addExtraCurricularEnrolments(entries, studentCurricularPlan, approvedEnrolments);

        return entries;
    }

    public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
	final Registration registration = getRegistration();
	return executionYear == null ? registration.getStudentCurricularPlan(new YearMonthDay()) : registration.getStudentCurricularPlan(executionYear);
    }

    private void addCurricularEnrolments(final Collection<Entry> entries, final StudentCurricularPlan studentCurricularPlan,
            final EnrolmentSet approvedEnrolments, final ExecutionYear executionYear) {

        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.isActive(executionYear)) {
        	final SimpleEntry simpleEntry = getSimpleEntry(entries, studentCurricularPlan, approvedEnrolments, curricularCourse);
        	if (simpleEntry == null) {
        	    final Set<SimpleEntry> simpleEntries = findEquivalentEnrolments(entries, studentCurricularPlan, approvedEnrolments, curricularCourse);
        	    if (simpleEntries != null && !simpleEntries.isEmpty()) {
        		entries.add(new EquivalentEnrolmentEntry(curricularCourse, simpleEntries));
        		for (final SimpleEntry otherSimpleEntry : simpleEntries) {
        		    removeProcessedEnrolments(approvedEnrolments, otherSimpleEntry);
        		}
        	    }        	    
        	} else {
        	    entries.add(simpleEntry);
        	    removeProcessedEnrolments(approvedEnrolments, simpleEntry);
        	}
            }
        }
    }

    private void removeProcessedEnrolments(final EnrolmentSet approvedEnrolments, final SimpleEntry simpleEntry) {
	if (simpleEntry.isEnrolmentEntry()) {
	    final EnrolmentEntry enrolmentEntry = (EnrolmentEntry) simpleEntry;
	    approvedEnrolments.remove(enrolmentEntry.getEnrolment());
	}
    }

    private SimpleEntry getSimpleEntry(final Collection<Entry> entries, final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        final Enrolment enrolment = findEnrolmentForSameCompetence(approvedEnrolments, curricularCourse);
        if (enrolment != null) {
            return new EnrolmentEntry(curricularCourse, enrolment);
        } else {
            final NotNeedToEnrollInCurricularCourse notNeedToEnrol = studentCurricularPlan.findNotNeddToEnrol(curricularCourse);
            if (notNeedToEnrol != null && !hasBeenProcessed(entries, notNeedToEnrol)) {
        	return new NotNeedToEnrolEntry(curricularCourse, notNeedToEnrol);
            }
        }
        return null;
    }

    private boolean hasBeenProcessed(final Collection<Entry> entries, final NotNeedToEnrollInCurricularCourse notNeedToEnrol) {
	for (final Entry entry : entries) {
	    if (entry.isNotNeedToEnrolEntry()) {
		final NotNeedToEnrolEntry notNeedToEnrolEntry = (NotNeedToEnrolEntry) entry;
		if (notNeedToEnrol == notNeedToEnrolEntry.getNotNeedToEnrol()) {
		    return true;
		}
	    } else if (entry.isEquivalentEnrolmentEntry()) {
		final EquivalentEnrolmentEntry equivalentEnrolmentEntry = (EquivalentEnrolmentEntry) entry;
		if (hasBeenProcessed((Collection) equivalentEnrolmentEntry.getEntries(), notNeedToEnrol)) {
		    return true;
		}
	    }
	}
	return false;
    }

    private Set<SimpleEntry> findEquivalentEnrolments(final Collection<Entry> entries, final StudentCurricularPlan studentCurricularPlan,
	    final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
            final Set<SimpleEntry> simpleEntries = new HashSet<SimpleEntry>();
            final EnrolmentSet workingApprovedEnrolments = approvedEnrolments.clone();
            final Set<CurricularCourse> oldCurricularCourses = curricularCourseEquivalence.getOldCurricularCoursesSet();
            for (final CurricularCourse oldCurricularCourse : oldCurricularCourses) {
        	final SimpleEntry simpleEntry = getSimpleEntry(entries, studentCurricularPlan, workingApprovedEnrolments, oldCurricularCourse);
        	if (simpleEntry == null) {
        	    break;
        	} else {
        	    simpleEntries.add(simpleEntry);
        	    removeProcessedEnrolments(workingApprovedEnrolments, simpleEntry);
        	}
            }

            if (oldCurricularCourses.size() == simpleEntries.size()) {
        	return simpleEntries;
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
		entries.add(new ExtraCurricularEnrolmentEntry(enrolment));
	    }
	}
    }

    private EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear) {
	final Registration registration = getRegistration();
        final Student student = registration.getStudent();
        final EnrolmentSet approvedEnrolments = new EnrolmentSet(executionYear);
        student.addApprovedEnrolments(approvedEnrolments);
        return approvedEnrolments;
    }

    public double getTotalEctsCredits(final ExecutionYear executionYear) {
	double ectsCredits = 0;
	for (final Entry entry : getCurriculumEntries(executionYear)) {
	    ectsCredits += entry.getEctsCredits();
	}
	return ectsCredits;
    }

    public int calculateCurricularYear(final ExecutionYear executionYear) {
	final double ectsCredits = getTotalEctsCredits(executionYear);
	int degreeCurricularYears = getStudentCurricularPlan(executionYear).getDegreeCurricularPlan().getDegree().getDegreeType().getYears();
	int ectsCreditsCurricularYear = (int) Math.floor((((ectsCredits + 24) / 60) + 1));
	return Math.min(ectsCreditsCurricularYear, degreeCurricularYears);
    }

    private class AverageCalculator {
	private double pc = 0;
	private double p = 0;

	public double result() {
	    return p == 0 ? 0 : pc / p;
	}

	public void add(final Collection<Entry> entries) {
	    for (final Entry entry : entries) {
		add(entry);
	    }
	}

	public void add(final Entry entry) {
	    if (entry instanceof EnrolmentEntry) {
		final EnrolmentEntry enrolmentEntry = (EnrolmentEntry) entry;
		final Enrolment enrolment = enrolmentEntry.getEnrolment();
		add(enrolment);
	    } else if (entry instanceof ExtraCurricularEnrolmentEntry) {
		final ExtraCurricularEnrolmentEntry extraCurricularEnrolmentEntry = (ExtraCurricularEnrolmentEntry) entry;
		final Enrolment enrolment = extraCurricularEnrolmentEntry.getEnrolment();
		add(enrolment);
	    } else if (entry instanceof EquivalentEnrolmentEntry) {
		final EquivalentEnrolmentEntry equivalentEnrolmentEntry = (EquivalentEnrolmentEntry) entry;
		for (final Entry otherEntry : equivalentEnrolmentEntry.getEntries()) {
		    add(otherEntry);
		}
	    }
	}

	public void add(final Enrolment enrolment) {
	    // TODO : do some magin here ...
	    final IGrade grade = enrolment.getGradeFinal();
	    if (grade != null) {
		final Object gradeValue = grade.getGrade();
		if (grade.getGradeType() == GradeType.GRADETWENTY && gradeValue instanceof Integer) {
		    final double w = enrolment.getWeigth().doubleValue();
		    p += w;
		    final int intGrade = ((Integer) gradeValue).intValue();
		    pc += w * intGrade;
		}
	    } else {
		System.out.println("Shit dude: enrolment: " + enrolment.getIdInternal() + " has null grade.");
	    }
	}
    }

    public double calculateAverage(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.result();
    }

    public int calculateRoundedAverage(final ExecutionYear executionYear) {
	return Long.valueOf(Math.round(calculateAverage(executionYear))).intValue();
    }

}
