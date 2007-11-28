package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;

public class Curriculum implements Serializable, ICurriculum {
    
    private CurriculumModule curriculumModule;
    
    private ExecutionYear executionYear;

    private Set<ICurriculumEntry> enrolmentRelatedEntries = new HashSet<ICurriculumEntry>();
    
    private Set<ICurriculumEntry> dismissalRelatedEntries = new HashSet<ICurriculumEntry>();
    
    private Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();
    
    private BigDecimal sumPiCi;
    
    private BigDecimal sumPi;
    
    static final protected int SCALE = 2;

    static final protected RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    private AverageType averageType = AverageType.WEIGHTED;

    private BigDecimal average;
    
    private BigDecimal sumEctsCredits;
    
    private Integer curricularYear;
    
    private boolean forceCalculus;

    
    static public Curriculum createEmpty(final ExecutionYear executionYear) {
	return Curriculum.createEmpty(null, executionYear);
    }

    static public Curriculum createEmpty(final CurriculumModule curriculumModule, final ExecutionYear executionYear) {
	return new Curriculum(curriculumModule, executionYear);
    }

    private Curriculum(final CurriculumModule curriculumModule, final ExecutionYear executionYear) {
	this.curriculumModule = curriculumModule;
	this.executionYear = executionYear;
    }

    public Curriculum(final CurriculumModule curriculumModule, final ExecutionYear executionYear, final Collection<ICurriculumEntry> entries, final Collection<ICurriculumEntry> dismissalRelatedEntries, final Collection<ICurriculumEntry> curricularYearEntries) {
	this(curriculumModule, executionYear);
	
	addEntries(this.enrolmentRelatedEntries, entries);
	addEntries(this.dismissalRelatedEntries, dismissalRelatedEntries);
	addEntries(this.curricularYearEntries, curricularYearEntries);
    }

    public void add(final Curriculum curriculum) {
	addEntries(this.enrolmentRelatedEntries, curriculum.getEnrolmentRelatedEntries());
	addEntries(this.dismissalRelatedEntries, curriculum.getDismissalRelatedEntries());
	addEntries(this.curricularYearEntries, curriculum.getCurricularYearEntries());

	forceCalculus = true;
    }

    private void addEntries(final Set<ICurriculumEntry> entries, final Collection<ICurriculumEntry> newEntries) {
	final boolean bolonhaDegree = curriculumModule.getStudentCurricularPlan().isBolonhaDegree();
	
	for (final ICurriculumEntry newEntry : newEntries) {
	    if (bolonhaDegree || shouldAdd(newEntry)) {
		entries.add(newEntry);
	    }
	}
    }
    
    /**
     * Just for pre-Bbolonha verification 
     */
    final private boolean shouldAdd(final ICurriculumEntry newEntry) {
	if (newEntry instanceof IEnrolment) {
	    final IEnrolment newIEnrolment = (IEnrolment) newEntry;
	    
	    for (final ICurriculumEntry entry : curricularYearEntries) {
		if (entry instanceof Dismissal && ((Dismissal) entry).hasSourceIEnrolments(newIEnrolment)) {
		    return false;
		} else if (entry == newIEnrolment) {
		    return false;
		}
	    }
	} else if (newEntry instanceof Dismissal) {
	    final Dismissal newDismissal = (Dismissal) newEntry;
	    
	    for (final ICurriculumEntry entry : curricularYearEntries) {
		if (entry instanceof Dismissal && newDismissal.isSimilar((Dismissal) entry)) {
		    return false;
		}
	    }
	}

	return true;
    }

    public CurriculumModule getCurriculumModule() {
	return curriculumModule;
    }
    
    public ExecutionYear getExecutionYear() {
	return executionYear;
    }
    
    public StudentCurricularPlan getStudentCurricularPlan() {
	return curriculumModule == null ? null : curriculumModule.getStudentCurricularPlan();
    }
    
    public boolean hasAverageEntry() {
	return curriculumModule != null && !getCurriculumEntries().isEmpty();
    }
    
    public boolean isEmpty() {
	return curriculumModule == null || (getCurriculumEntries().isEmpty() && this.curricularYearEntries.isEmpty());
    }
    
    public Collection<ICurriculumEntry> getCurriculumEntries() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();
	
	result.addAll(enrolmentRelatedEntries);
	result.addAll(dismissalRelatedEntries);
	
	return result;
    }
    
    public Set<ICurriculumEntry> getEnrolmentRelatedEntries() {
	return enrolmentRelatedEntries;
    }
    
    public Set<ICurriculumEntry> getDismissalRelatedEntries() {
	return dismissalRelatedEntries;
    }
    
    public Set<ICurriculumEntry> getCurricularYearEntries() {
	return curricularYearEntries;
    }
    
    public BigDecimal getSumPiCi() {
	if (sumPiCi == null || forceCalculus) {
	    doCalculus();
	    forceCalculus = false;
	}

	return sumPiCi;
    }

    public BigDecimal getSumPi() {
	if (sumPi == null || forceCalculus) {
	    doCalculus();
	    forceCalculus = false;
	}

	return sumPi;
    }
    
    public BigDecimal getAverage() {
	if (average == null || forceCalculus) {
	    doCalculus();
	    forceCalculus = false;
	}
	
	return average.setScale(SCALE, ROUNDING_MODE);
    }

    public BigDecimal getSumEctsCredits() {
	if (sumEctsCredits == null || forceCalculus) {
	    doCalculus();
	    forceCalculus = false;
	}
	
	return sumEctsCredits;
    }
    
    public Integer getCurricularYear() {
	if (curricularYear == null || forceCalculus) {
	    doCalculus();
	    forceCalculus = false;
	}
	
	return curricularYear;
    }

    public BigDecimal getCreditsEctsCredits() {
	BigDecimal result = BigDecimal.ZERO;

	for (final ICurriculumEntry entry : this.curricularYearEntries) {
	    if (entry instanceof CreditsDismissal) {
		result = result.add(entry.getEctsCreditsForCurriculum());
	    }
	}
	
	return result;
    }
    
    private void doCalculus() {
	sumPiCi = BigDecimal.ZERO;
	sumPi = BigDecimal.ZERO;
	countAverage(enrolmentRelatedEntries);
	countAverage(dismissalRelatedEntries);
	average = calculateAverage();
	
	sumEctsCredits = BigDecimal.ZERO;
	countCurricularYear(curricularYearEntries);
	curricularYear = calculateCurricularYear();
	
	//System.out.println(toString());
    }
    
    public void setAverageType(AverageType averageType) {
	this.averageType = averageType;
	forceCalculus = true;
    }
	
    private void countAverage(final Set<ICurriculumEntry> entries) {
	for (final ICurriculumEntry entry : entries) {
	    if (entry.getGrade().isNumeric()) {
		final BigDecimal weigth = entry.getWeigthForCurriculum();
		
		if (averageType == AverageType.WEIGHTED) {
		    sumPi = sumPi.add(weigth);
		    sumPiCi = sumPiCi.add(entry.getWeigthTimesGrade());
		} else if (averageType == AverageType.SIMPLE) {
		    sumPi = sumPi.add(BigDecimal.ONE);
		    sumPiCi = sumPiCi.add(entry.getGrade().getNumericValue());
		} else {
		    throw new DomainException("Curriculum.average.type.not.supported");
		}
	    }
	}
    }
    
    private BigDecimal calculateAverage() {
	return sumPi.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : sumPiCi.divide(sumPi, SCALE * SCALE + 1, ROUNDING_MODE);
    }
    
    private void countCurricularYear(final Set<ICurriculumEntry> entries) {
	for (final ICurriculumEntry entry : entries) {
	    sumEctsCredits = sumEctsCredits.add(entry.getEctsCreditsForCurriculum());
	}
    }
    
    private Integer calculateCurricularYear() {
	final int degreeCurricularYears = getStudentCurricularPlan().getDegreeType().getYears();
	final BigDecimal ectsCreditsCurricularYear = sumEctsCredits.add(BigDecimal.valueOf(24)).divide(BigDecimal.valueOf(60), SCALE * SCALE + 1).add(BigDecimal.valueOf(1));
	return Math.min(ectsCreditsCurricularYear.intValue(), degreeCurricularYears);
    }
    
    @Override
    public String toString() {
	final StringBuilder result = new StringBuilder();
	
	result.append("\n[CURRICULUM]");
	result.append("\n[CURRICULUM_MODULE][ID] " + curriculumModule.getIdInternal() + "\t[NAME]" + curriculumModule.getName().getContent());
	result.append("\n[SUM ENTRIES] " + (enrolmentRelatedEntries.size() + dismissalRelatedEntries.size()));
	result.append("\n[SUM PiCi] " + sumPiCi.toString());
	result.append("\n[SUM Pi] " + sumPi.toString());
	result.append("\n[AVERAGE] " + average);
	result.append("\n[SUM ECTS CREDITS] " + sumEctsCredits.toString());
	result.append("\n[CURRICULAR YEAR] " + curricularYear);
	
	result.append("\n[ENTRIES]");
	for (final ICurriculumEntry entry : enrolmentRelatedEntries) {
	    result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[GRADE] " + entry.getGrade().getNumericValue() + "\t[WEIGHT] " + entry.getWeigthForCurriculum() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] " + entry.getClass().getSimpleName());
	}
	
	result.append("\n[DISMISSAL RELATED ENTRIES]");
	for (final ICurriculumEntry entry : dismissalRelatedEntries) {
	    result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[GRADE] " + entry.getGrade().getNumericValue() + "\t[WEIGHT] " + entry.getWeigthForCurriculum() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] " + entry.getClass().getSimpleName());
	}
	
	result.append("\n[CURRICULAR YEAR ENTRIES]");
	for (final ICurriculumEntry entry : curricularYearEntries) {
	    result.append("\n[ENTRY] [NAME]" + entry.getName().getContent() + "\t[ECTS] " + entry.getEctsCreditsForCurriculum() + "\t[CLASS_NAME] " + entry.getClass().getSimpleName());
	}
	
	return result.toString();
    }

}
