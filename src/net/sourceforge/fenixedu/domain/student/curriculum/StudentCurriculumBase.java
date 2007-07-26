package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class StudentCurriculumBase implements Serializable {

    final private DomainReference<Registration> registrationDomainReference;

    public StudentCurriculumBase(final Registration registration) {
        super();
        if (registration == null) {
            throw new NullPointerException("error.registration.cannot.be.null");
        }
        this.registrationDomainReference = new DomainReference<Registration>(registration);
    }

    final public Registration getRegistration() {
        return registrationDomainReference.getObject();
    }

    abstract public Collection<CurriculumEntry> getCurriculumEntries(final ExecutionYear executionYear);
    
    abstract protected EnrolmentSet getApprovedEnrolments(final ExecutionYear executionYear);

    final public double getTotalEctsCredits(final ExecutionYear executionYear) {
	double ectsCredits = 0;
	for (final CurriculumEntry entry : getCurriculumEntries(executionYear)) {
	    ectsCredits += entry.getEctsCredits();
	}
	return ectsCredits;
    }

    final public int calculateCurricularYear(final ExecutionYear executionYear) {
	final double ectsCredits = getTotalEctsCredits(executionYear);
	int degreeCurricularYears = getRegistration().getStudentCurricularPlan(executionYear).getDegreeType().getYears();
	int ectsCreditsCurricularYear = (int) Math.floor((((ectsCredits + 24) / 60) + 1));
	return Math.min(ectsCreditsCurricularYear, degreeCurricularYears);
    }

    public enum AverageType {
	SIMPLE,
	WEIGHTED,
	BEST
    }
    
    final private class AverageCalculator {
	private double sumPiCi = 0;
	private double sumPi = 0;

	public AverageCalculator() {
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

    final public double calculateAverage(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.result();
    }

    final public Double getRoundedAverage(final ExecutionYear executionYear, final boolean decimal) {
	return applyRound(calculateAverage(executionYear), decimal);
    }

    static final public Double applyRound(final double d, final boolean decimal) {
	return decimal ? Math.round((d * 100)) / 100.0 : Math.round((d));
    }

    final public double getSumPiCi(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.sumPiCi;
    }
    
    final public double getSumPi(final ExecutionYear executionYear) {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries(executionYear));
	return averageCalculator.sumPi;
    }

}
