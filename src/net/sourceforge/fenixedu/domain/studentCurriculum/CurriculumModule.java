package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public abstract class CurriculumModule extends CurriculumModule_Base {

    static final public Comparator<CurriculumModule> COMPARATOR_BY_NAME_AND_ID = new Comparator<CurriculumModule>() {
	public int compare(CurriculumModule o1, CurriculumModule o2) {
	    int result = o1.getName().compareTo(o2.getName());
	    return (result == 0) ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : result;
	}
    };

    public CurriculumModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDateDateTime(new DateTime());
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public void deleteRecursive() {
	delete();
    }

    public void delete() {
	removeDegreeModule();
	removeCurriculumGroup();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public RootCurriculumGroup getRootCurriculumGroup() {
	return hasCurriculumGroup() ? getCurriculumGroup().getRootCurriculumGroup() : (RootCurriculumGroup) this;
    }

    public boolean isCycleCurriculumGroup() {
	return false;
    }

    public boolean isNoCourseGroupCurriculumGroup() {
	return false;
    }

    public boolean isEnrolment() {
	return false;
    }

    public boolean isDismissal() {
	return false;
    }

    public boolean isCreditsDismissal() {
	return false;
    }

    abstract public boolean isLeaf();

    abstract public boolean isRoot();

    abstract public StringBuilder print(String tabs);

    abstract public List<Enrolment> getEnrolments();

    public abstract StudentCurricularPlan getStudentCurricularPlan();

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     * 
     * @return
     */
    final public boolean isBoxStructure() {
	return getStudentCurricularPlan().isBoxStructure();
    }

    final public boolean isBolonhaDegree() {
	return getStudentCurricularPlan().isBolonhaDegree();
    }

    public DegreeCurricularPlan getDegreeCurricularPlanOfStudent() {
	return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public DegreeCurricularPlan getDegreeCurricularPlanOfDegreeModule() {
	return getDegreeModule().getParentDegreeCurricularPlan();
    }

    public MultiLanguageString getName() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();

	if (this.getDegreeModule().getName() != null && this.getDegreeModule().getName().length() > 0) {
	    multiLanguageString.setContent(Language.pt, this.getDegreeModule().getName());
	}
	if (this.getDegreeModule().getNameEn() != null && this.getDegreeModule().getNameEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, this.getDegreeModule().getNameEn());
	}
	return multiLanguageString;
    }

    public boolean isApproved(final CurricularCourse curricularCourse) {
	return isApproved(curricularCourse, null);
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return this.getDegreeModule().equals(degreeModule);
    }

    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
	return this.equals(curriculumModule);
    }

    public boolean parentCurriculumGroupIsNoCourseGroupCurriculumGroup() {
	return hasCurriculumGroup() && getCurriculumGroup().isNoCourseGroupCurriculumGroup();
    }

    public Set<ICurricularRule> getCurricularRules(ExecutionPeriod executionPeriod) {
	final Set<ICurricularRule> result = hasCurriculumGroup() ? getCurriculumGroup().getCurricularRules(executionPeriod)
		: new HashSet<ICurricularRule>();
	result.addAll(getDegreeModule().getCurricularRules(executionPeriod));

	return result;
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
	return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, getCurriculumGroup().getDegreeModule(),
		executionYear);
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
	    final ExecutionPeriod executionPeriod) {
	return getDegreeModule().getMostRecentActiveCurricularRule(ruleType, getCurriculumGroup().getDegreeModule(),
		executionPeriod);
    }

    public String getFullPath() {
	if (isRoot()) {
	    return getName().getContent();
	} else {
	    return getCurriculumGroup().getFullPath() + " > " + getName().getContent();
	}
    }

    public boolean isFor(final DegreeCurricularPlan degreeCurricularPlan) {
	return getDegreeModule().getParentDegreeCurricularPlan() == degreeCurricularPlan;
    }

    public boolean isConcluded() {
	return isConcluded(getApprovedCurriculumLinesLastExecutionYear());
    }

    public ExecutionYear getApprovedCurriculumLinesLastExecutionYear() {
	final SortedSet<ExecutionYear> executionYears = new TreeSet<ExecutionYear>(ExecutionYear.COMPARATOR_BY_YEAR);

	for (final CurriculumLine curriculumLine : getApprovedCurriculumLines()) {
	    if (curriculumLine.hasExecutionPeriod()) {
		executionYears.add(curriculumLine.getExecutionPeriod().getExecutionYear());
	    }
	}

	return executionYears.isEmpty() ? ExecutionYear.readCurrentExecutionYear() : executionYears.last();
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
	final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();
	addApprovedCurriculumLines(result);
	return result;
    }

    final public CurriculumLine getLastApprovement() {
	final SortedSet<CurriculumLine> curriculumLines = new TreeSet<CurriculumLine>(
		CurriculumLine.COMPARATOR_BY_APPROVEMENT_DATE_AND_ID);
	curriculumLines.addAll(getApprovedCurriculumLines());

	if (curriculumLines.isEmpty()) {
	    throw new DomainException("Registration.has.no.approved.curriculum.lines");
	}

	return curriculumLines.last();
    }

    final public YearMonthDay getLastApprovementDate() {
	return getLastApprovement().getApprovementDate();
    }

    final public ExecutionYear getLastApprovementExecutionYear() {
	return getLastApprovement().getExecutionYear();
    }

    public Curriculum getCurriculum() {
	return getCurriculum(null);
    }

    public BigDecimal calculateAverage() {
	return getCurriculum().getAverage();
    }

    public Integer calculateRoundedAverage() {
	return getCurriculum().getRoundedAverage();
    }

    public Double getCreditsConcluded() {
	return getCreditsConcluded(getApprovedCurriculumLinesLastExecutionYear());
    }

    abstract public Double getEctsCredits();

    abstract public Double getAprovedEctsCredits();

    abstract public Double getEnroledEctsCredits(final ExecutionPeriod executionPeriod);

    abstract public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod);

    abstract public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public boolean hasAnyEnrolments();

    abstract public void addApprovedCurriculumLines(final Collection<CurriculumLine> result);

    abstract public boolean hasAnyApprovedCurriculumLines();

    abstract public boolean hasEnrolmentWithEnroledState(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod);

    abstract public ExecutionYear getIEnrolmentsLastExecutionYear();

    abstract public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod);

    abstract public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionPeriod executionPeriod);

    abstract public Enrolment getApprovedEnrolment(final CurricularCourse curricularCourse);

    abstract public CurriculumLine getApprovedCurriculumLine(final CurricularCourse curricularCourse);

    abstract public Dismissal getDismissal(final CurricularCourse curricularCourse);

    abstract public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear);

    abstract public void collectDismissals(final List<Dismissal> result);

    abstract public void getAllDegreeModules(Collection<DegreeModule> degreeModules);

    abstract public Set<CurriculumLine> getAllCurriculumLines();

    abstract protected boolean isConcluded(ExecutionYear executionYear);

    abstract public boolean hasConcluded(DegreeModule degreeModule, ExecutionYear executionYear);

    public abstract YearMonthDay calculateConclusionDate();

    abstract public Curriculum getCurriculum(final ExecutionYear executionYear);

    abstract public Double getCreditsConcluded(ExecutionYear executionYear);

    abstract public boolean isPropaedeutic();

}
