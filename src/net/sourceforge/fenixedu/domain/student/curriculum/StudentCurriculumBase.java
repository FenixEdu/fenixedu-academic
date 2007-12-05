package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

abstract public class StudentCurriculumBase implements Serializable, ICurriculum {

    final private DomainReference<Registration> registrationDomainReference;
    
    final private DomainReference<ExecutionYear> executionYearDomainReference;

    static final protected int SCALE = 2;

    static final protected RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public StudentCurriculumBase(final Registration registration, final ExecutionYear executionYear) {
        super();
        if (registration == null) {
            throw new NullPointerException("error.registration.cannot.be.null");
        }
        this.registrationDomainReference = new DomainReference<Registration>(registration);
        this.executionYearDomainReference = new DomainReference<ExecutionYear>(executionYear);
    }

    final public Registration getRegistration() {
        return registrationDomainReference.getObject();
    }

    final public ExecutionYear getExecutionYear() {
        return executionYearDomainReference.getObject();
    }

    final public StudentCurricularPlan getStudentCurricularPlan() {
	return getRegistration().getStudentCurricularPlan(getExecutionYear());
    }

    public boolean isEmpty() {
	return getCurriculumEntries().isEmpty();
    }

    abstract public Collection<ICurriculumEntry> getCurriculumEntries();
    
    abstract protected EnrolmentSet getApprovedEnrolments();

    public boolean hasAnyExternalApprovedEnrolment() {
	return false;
    }
    
    final public BigDecimal getSumEctsCredits() {
	BigDecimal ectsCredits = BigDecimal.ZERO;
	for (final ICurriculumEntry entry : getCurriculumEntries()) {
	    ectsCredits = ectsCredits.add(entry.getEctsCreditsForCurriculum());
	}
	return ectsCredits;
    }

    final public Integer getCurricularYear() {
	final int degreeCurricularYears = getTotalCurricularYears();
	final BigDecimal ectsCreditsCurricularYear = getSumEctsCredits().add(BigDecimal.valueOf(24)).divide(BigDecimal.valueOf(60), SCALE * SCALE + 1).add(BigDecimal.valueOf(1));
	return Math.min(ectsCreditsCurricularYear.intValue(), degreeCurricularYears);
    }

    public Integer getTotalCurricularYears() {
	return getStudentCurricularPlan().getDegreeType().getYears();
    }
    
    private AverageType averageType = AverageType.WEIGHTED;

    public void setAverageType(AverageType averageType) {
	this.averageType = averageType;
    }
	
    final private class AverageCalculator {
	private BigDecimal sumPiCi = BigDecimal.ZERO;
	private BigDecimal sumPi = BigDecimal.ZERO;

	public AverageCalculator() {
	}

	public void add(final Collection<ICurriculumEntry> entries) {
	    for (final ICurriculumEntry entry : entries) {
		add(entry);
	    }
	}

	public void add(final ICurriculumEntry entry) {
	    if (entry.getWeigthTimesGrade() != null) {
		if (averageType == AverageType.WEIGHTED) {
		    sumPi = sumPi.add(entry.getWeigthForCurriculum());
		    sumPiCi = sumPiCi.add(entry.getWeigthTimesGrade());
		} else {
		    sumPi = sumPi.add(BigDecimal.ONE);
		    sumPiCi = sumPiCi.add(entry.getGrade().getNumericValue());
		}
	    }
	}

	public BigDecimal result() {
	    return sumPi.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : sumPiCi.divide(sumPi, SCALE * SCALE + 1, ROUNDING_MODE);
	}

    }

    final public BigDecimal getAverage() {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries());
	return averageCalculator.result().setScale(SCALE, ROUNDING_MODE);
    }

    final public BigDecimal getSumPiCi() {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries());
	return averageCalculator.sumPiCi;
    }
    
    final public BigDecimal getSumPi() {
	final AverageCalculator averageCalculator = new AverageCalculator();
	averageCalculator.add(getCurriculumEntries());
	return averageCalculator.sumPi;
    }

    final public BigDecimal getRemainingCredits() {
	return BigDecimal.valueOf(getStudentCurricularPlan().getGivenCredits());
    }

}
