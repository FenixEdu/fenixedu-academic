package net.sourceforge.fenixedu.domain.student.curriculum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CreditsDismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class Curriculum implements Serializable {
    
    private CurriculumModule curriculumModule;
    
    private ExecutionYear executionYear;

    private Set<ICurriculumEntry> entries = new HashSet<ICurriculumEntry>();
    
    private Set<ICurriculumEntry> dismissalRelatedEntries = new HashSet<ICurriculumEntry>();
    
    private Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();
    
    private BigDecimal sumPiCi;
    
    private BigDecimal sumPi;
    
    static final protected int SCALE = 2;

    static final protected RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

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
	
	addEntries(this.entries, entries);
	addEntries(this.dismissalRelatedEntries, dismissalRelatedEntries);
	
	this.curricularYearEntries.addAll(curricularYearEntries);
    }

    public void add(final Curriculum curriculum) {
	addEntries(this.entries, curriculum.getEntries());
	addEntries(this.dismissalRelatedEntries, curriculum.getDismissalRelatedEntries());
	this.curricularYearEntries.addAll(curriculum.getCurricularYearEntries());

	forceCalculus = true;
    }

    private void addEntries(final Set<ICurriculumEntry> entries, final Collection<ICurriculumEntry> newEntries) {
	for (final ICurriculumEntry newEntry : newEntries) {
	    if (newEntry.getGrade().isNumeric()) {
//		if (newEntry instanceof Dismissal) {
//		    Dismissal dismissal = (Dismissal) newEntry;
//		    for (IEnrolment iEnrolment : dismissal.getSourceIEnrolments()) {
//			this.entries.remove(iEnrolment);
//		    }
//		}
		entries.add(newEntry);
	    }
	}
    }

    public CurriculumModule getCurriculumModule() {
	return curriculumModule;
    }
    
    public ExecutionYear getExecutionYear() {
	return executionYear;
    }
    
    public StudentCurricularPlan getStudentCurricularPlan() {
	return curriculumModule.getStudentCurricularPlan();
    }
    
    public Set<ICurriculumEntry> getEntries() {
	return entries;
    }
    
    public Set<ICurriculumEntry> getDismissalRelatedEntries() {
	return dismissalRelatedEntries;
    }
    
    public Collection<ICurriculumEntry> getCurriculumEntries() {
	final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();
	
	result.addAll(entries);
	result.addAll(dismissalRelatedEntries);
	
	return result;
    }
    
    public Set<ICurriculumEntry> getCurricularYearEntries() {
	return curricularYearEntries;
    }
    
    public boolean isEmpty() {
	return this.entries.isEmpty() && this.dismissalRelatedEntries.isEmpty() && this.curricularYearEntries.isEmpty();
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

    public BigDecimal getCreditsDismissalsEctsCredits() {
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
	countAverage(entries);
	countAverage(dismissalRelatedEntries);
	average = calculateAverage();
	
	sumEctsCredits = BigDecimal.ZERO;
	countCurricularYear(curricularYearEntries);
	curricularYear = calculateCurricularYear();
	
	System.out.println(toString());
    }
    
    private void countAverage(final Set<ICurriculumEntry> entries) {
	final Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();
	
	for (final ICurriculumEntry entry : entries) {
	    final BigDecimal weigth = entry.getWeigthForCurriculum();
		
	    sumPiCi = sumPiCi.add(weigth.multiply(entry.getGrade().getNumericValue()));
	    sumPi = sumPi.add(weigth);
	}
    }
    
    private BigDecimal calculateAverage() {
	return sumPi == BigDecimal.ZERO ? BigDecimal.ZERO : sumPiCi.divide(sumPi, SCALE * SCALE + 1, ROUNDING_MODE);
    }
    
    private Integer calculateCurricularYear() {
	final int degreeCurricularYears = getStudentCurricularPlan().getDegreeType().getYears();
	final BigDecimal ectsCreditsCurricularYear = sumEctsCredits.add(BigDecimal.valueOf(24)).divide(BigDecimal.valueOf(60), SCALE * SCALE + 1).add(BigDecimal.valueOf(1));
	return Math.min(ectsCreditsCurricularYear.intValue(), degreeCurricularYears);
    }
    
    private void countCurricularYear(final Set<ICurriculumEntry> entries) {
	final Set<ICurriculumEntry> curricularYearEntries = new HashSet<ICurriculumEntry>();
	
	for (final ICurriculumEntry entry : entries) {
	    sumEctsCredits = sumEctsCredits.add(entry.getEctsCreditsForCurriculum());
	}
    }
    
    @Override
    public String toString() {
	final StringBuilder result = new StringBuilder();
	
	result.append("\n[CURRICULUM]");
	result.append("\n[CURRICULUM_MODULE][ID] " + curriculumModule.getIdInternal() + "\t[NAME]" + curriculumModule.getName().getContent());
	result.append("\n[SUM ENTRIES] " + (entries.size() + dismissalRelatedEntries.size()));
	result.append("\n[SUM PiCi] " + sumPiCi.toString());
	result.append("\n[SUM Pi] " + sumPi.toString());
	result.append("\n[AVERAGE] " + average);
	result.append("\n[SUM ECTS CREDITS] " + sumEctsCredits.toString());
	result.append("\n[CURRICULAR YEAR] " + curricularYear);
	
	result.append("\n[ENTRIES]");
	for (final ICurriculumEntry entry : entries) {
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
