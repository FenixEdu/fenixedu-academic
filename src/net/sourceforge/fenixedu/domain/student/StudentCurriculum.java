package net.sourceforge.fenixedu.domain.student;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;

public class StudentCurriculum implements Serializable {

    /**
     * Student Curriculum 
     *
     */
    
    private final DomainReference<Registration> registrationDomainReference;
    
    private final DomainReference<ExecutionYear> executionYearDomainReference;
    
    private final StudentCurricularPlan studentCurricularPlan;
    
    private final Collection<Entry> curriculumEntries;

    private final double totalEctsCredits;

    private final int curricularYear;
    
    private final AverageCalculator averageCalculator;

    private final double average;
    
    private final double sumPiCi;
    
    private final double sumPi;

    public StudentCurriculum(final Registration registration, final ExecutionYear executionYear) {
        super();
        
        if (registration == null) {
            throw new NullPointerException("error.registration.cannot.be.null");
        }
        
        registrationDomainReference = new DomainReference<Registration>(registration);
        executionYearDomainReference = new DomainReference<ExecutionYear>(executionYear);
        
        studentCurricularPlan = getRegistration().getStudentCurricularPlan(getExecutionYear());
        if (studentCurricularPlan == null) {
            throw new NullPointerException("error.registration.has.no.student.curricular.plan.for.given.execution.year");
        }
        
        curriculumEntries = retrieveCurriculumEntries();
        
        totalEctsCredits = calculateTotalEctsCredits();
        curricularYear = calculateCurricularYear();
        
        averageCalculator = new AverageCalculator();
        average = averageCalculator.result();
        sumPiCi = averageCalculator.sumPiCi();
        sumPi = averageCalculator.sumPi();
    }

    public StudentCurriculum(final Registration registration) {
        this(registration, null);
    }

    public Registration getRegistration() {
        return registrationDomainReference.getObject();
    }

    public ExecutionYear getExecutionYear() {
        return executionYearDomainReference.getObject();
    }
    
    public StudentCurricularPlan getStudentCurricularPlan() {
	return studentCurricularPlan;
    }


    /**
     * Student Curriculum Data Structures
     *
     */
    
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
	
	public abstract double getWeigth();
	
	public abstract Double getWeigthTimesClassification();

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
	public boolean isNotInDegreeCurriculumEnrolmentEntry() {
	    return false;
	}
	public boolean getIsNotInDegreeCurriculumEnrolmentEntry() {
	    return isNotInDegreeCurriculumEnrolmentEntry();
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
	    return getEnrolment().getEctsCredits().doubleValue();
	}

	@Override
	public double getWeigth() {
	    return getEnrolment().getWeigth().doubleValue();
	}

	@Override
	public Double getWeigthTimesClassification() {
	    final String grade = getEnrolment().getLatestEnrolmentEvaluation().getGrade();
	    return StringUtils.isNumeric(grade) ? Double.valueOf(getWeigth() * Integer.valueOf(grade)) : null;
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
	    return getNotNeedToEnrol().getCurricularCourse().getEctsCredits().doubleValue();
	}

	@Override
	public double getWeigth() {
	    throw new RuntimeException();
	}
	
	@Override
	public Double getWeigthTimesClassification() {
	    return null;
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
		ectsCredits += simpleEntry.getEctsCredits();
	    }
	    return ectsCredits;
	}

	@Override
	public double getWeigth() {
	    double result = 0;
	    for (final SimpleEntry simpleEntry : getEntries()) {
		if (!simpleEntry.isNotNeedToEnrolEntry()) {
		    result += simpleEntry.getWeigth();    
		}
	    }
	    return result;
	}
	
	@Override
	public Double getWeigthTimesClassification() {
	    double result = 0;
	    for (final SimpleEntry simpleEntry : getEntries()) {
		if (simpleEntry.getWeigthTimesClassification() != null) {
		    result += simpleEntry.getWeigthTimesClassification().doubleValue();
		}
	    }
	    
	    return result == 0 ? null : result;
	}

    }

    public static class NotInDegreeCurriculumEnrolmentEntry extends Entry {
        private final DomainReference<Enrolment> enrolmentDomainReference;

        public NotInDegreeCurriculumEnrolmentEntry(final Enrolment enrolment) {
            super();
            this.enrolmentDomainReference = new DomainReference<Enrolment>(enrolment);
        }

        @Override
        public boolean isNotInDegreeCurriculumEnrolmentEntry() {
            return true;
        }

        public Enrolment getEnrolment() {
            return enrolmentDomainReference.getObject();
        }

	@Override
	public double getEctsCredits() {
	    return getEnrolment().getEctsCredits().doubleValue();
	}
	
	@Override
	public double getWeigth() {
	    return getEnrolment().getWeigth().doubleValue();
	}
	
	@Override
	public Double getWeigthTimesClassification() {
	    final String grade = getEnrolment().getLatestEnrolmentEvaluation().getGrade();
	    return StringUtils.isNumeric(grade) ? Double.valueOf(getWeigth() * Integer.valueOf(grade)) : null;
	}

    }


    /**
     * Student Curriculum Entries retrieval 
     *
     */
    
    private Collection<Entry> retrieveCurriculumEntries() {
	final StudentCurricularPlan studentCurricularPlan = getRegistration().getStudentCurricularPlan(getExecutionYear());
        if (studentCurricularPlan == null) {
            return null;
        }

        final EnrolmentSet approvedEnrolments = getApprovedEnrolments();

        final Collection<Entry> entries = new HashSet<Entry>();

        addCurricularEnrolments(entries, approvedEnrolments);

        addNotInDegreeCurriculumEnrolments(entries, approvedEnrolments);

        return entries;
    }

    private EnrolmentSet getApprovedEnrolments() {
	final Registration registration = getRegistration();
        final Student student = registration.getStudent();
        final EnrolmentSet approvedEnrolments = new EnrolmentSet(getExecutionYear());
        student.addApprovedEnrolments(approvedEnrolments);
        return approvedEnrolments;
    }

    private void addCurricularEnrolments(final Collection<Entry> entries, final EnrolmentSet approvedEnrolments) {

        final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.isActive(getExecutionYear())) {
        	final SimpleEntry simpleEntry = getSimpleEntry(entries, approvedEnrolments, curricularCourse);
        	if (simpleEntry == null) {
        	    final Set<SimpleEntry> simpleEntries = findEquivalentEnrolments(entries, approvedEnrolments, curricularCourse);
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

    private SimpleEntry getSimpleEntry(final Collection<Entry> entries, final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
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

    private Set<SimpleEntry> findEquivalentEnrolments(final Collection<Entry> entries, final EnrolmentSet approvedEnrolments, final CurricularCourse curricularCourse) {
        for (final CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
            final Set<SimpleEntry> simpleEntries = new HashSet<SimpleEntry>();
            final EnrolmentSet workingApprovedEnrolments = approvedEnrolments.clone();
            final Set<CurricularCourse> oldCurricularCourses = curricularCourseEquivalence.getOldCurricularCoursesSet();
            for (final CurricularCourse oldCurricularCourse : oldCurricularCourses) {
        	final SimpleEntry simpleEntry = getSimpleEntry(entries, workingApprovedEnrolments, oldCurricularCourse);
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

    private void removeProcessedEnrolments(final EnrolmentSet approvedEnrolments, final SimpleEntry simpleEntry) {
	if (simpleEntry.isEnrolmentEntry()) {
	    final EnrolmentEntry enrolmentEntry = (EnrolmentEntry) simpleEntry;
	    approvedEnrolments.remove(enrolmentEntry.getEnrolment());
	}
    }

    private void addNotInDegreeCurriculumEnrolments(final Collection<Entry> entries, final Set<Enrolment> approvedEnrolments) {
	for (final Enrolment enrolment : approvedEnrolments) {
	    if (enrolment.getStudentCurricularPlan().getDegree() == studentCurricularPlan.getDegree()) {
	    	entries.add(new NotInDegreeCurriculumEnrolmentEntry(enrolment));
	    }
	}
    }

    
    /**
     * Student Curriculum calculations
     * 
     */
    
    private double calculateTotalEctsCredits() {
	double ectsCredits = 0;
	for (final Entry entry : getCurriculumEntries()) {
	    ectsCredits += entry.getEctsCredits();
	}
	return ectsCredits;
    }

    private int calculateCurricularYear() {
	final double ectsCredits = getTotalEctsCredits();
	int degreeCurricularYears = studentCurricularPlan.getDegreeType().getYears();
	int ectsCreditsCurricularYear = (int) Math.floor((((ectsCredits + 24) / 60) + 1));
	return Math.min(ectsCreditsCurricularYear, degreeCurricularYears);
    }

    private class AverageCalculator {
	private double sumPiCi = 0;
	private double sumPi = 0;

	public AverageCalculator() {
	    add(getCurriculumEntries());	    
	}
	
	public double sumPiCi() {
	    return sumPiCi;
	}
	
	public double sumPi() {
	    return sumPi;
	}
	
	public double result() {
	    return sumPi == 0 ? 0 : sumPiCi / sumPi;
	}
	
	public void add(final Collection<Entry> entries) {
	    for (final Entry entry : entries) {
		add(entry);
	    }
	}

	public void add(final Entry entry) {
	    if (entry.getWeigthTimesClassification() != null) {
		sumPi += entry.getWeigth();
		sumPiCi += entry.getWeigthTimesClassification();
	    }
	}

    }
	

    /**
     * Student Curriculum results
     * 
     */

    public Collection<Entry> getCurriculumEntries() {
	return curriculumEntries;
    }
    
    public double getTotalEctsCredits() {
	return totalEctsCredits ;
    }
    
    public int getCurricularYear() {
	return curricularYear;
    }
    
    public double getAverage() {
	return average;
    }
    
    public Double getRoundedAverage(boolean decimal) {
	return decimal ? Math.round((average * 100)) / 100.0 : Math.round((average));
    }
    
    public double getSumPiCi() {
	return sumPiCi;
    }
    
    public double getSumPi() {
	return sumPi;
    }
    
}
