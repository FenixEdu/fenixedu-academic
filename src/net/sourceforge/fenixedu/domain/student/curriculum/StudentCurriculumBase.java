package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.YearMonthDay;

public abstract class StudentCurriculumBase implements Serializable {

    private final DomainReference<Registration> registrationDomainReference;

    public StudentCurriculumBase(final Registration registration) {
        super();
        if (registration == null) {
            throw new NullPointerException("error.registration.cannot.be.null");
        }
        this.registrationDomainReference = new DomainReference<Registration>(registration);
    }

    public Registration getRegistration() {
        return registrationDomainReference.getObject();
    }

    public abstract Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear);
    
    protected abstract EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear);

    public StudentCurricularPlan getStudentCurricularPlan(final ExecutionYear executionYear) {
	final Registration registration = getRegistration();
	return executionYear == null ? registration.getStudentCurricularPlan(new YearMonthDay()) : registration.getStudentCurricularPlan(executionYear);
    }

    public double getTotalEctsCredits(final ExecutionYear executionYear) {
	double ectsCredits = 0;
	for (final CurriculumEntry entry : getCurriculumEntries(executionYear)) {
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
	private double sumPiCi = 0;
	private double sumPi = 0;

	public AverageCalculator() {
	}

	public AverageCalculator(final Collection<CurriculumEntry> curriculumEntries) {
	    add(curriculumEntries);
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
	
	public void add(final Collection<CurriculumEntry> entries) {
	    for (final CurriculumEntry entry : entries) {
		add(entry);
	    }
	}

	public void add(final CurriculumEntry entry) {
	    if (entry.getWeigthTimesClassification() != null) {
		sumPi += entry.getWeigth();
		sumPiCi += entry.getWeigthTimesClassification();
	    }
	}

    }

    public double calculateAverage(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.result();
    }

    public Double getRoundedAverage(final ExecutionYear executionYear, final boolean decimal) {
	final double average = calculateAverage(executionYear);
	return decimal ? Math.round((average * 100)) / 100.0 : Math.round((average));
    }

    public double getSumPiCi(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.sumPiCi;
    }
    
    public double getSumPi(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.sumPi;
    }

}
