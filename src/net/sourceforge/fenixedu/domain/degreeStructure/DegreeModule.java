package net.sourceforge.fenixedu.domain.degreeStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

abstract public class DegreeModule extends DegreeModule_Base {

    static final public Comparator<DegreeModule> COMPARATOR_BY_NAME = new ComparatorByName();

    static private class ComparatorByName implements Comparator<DegreeModule> {
	public int compare(DegreeModule d1, DegreeModule d2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(new BeanComparator("name", Collator.getInstance()));
	    comparatorChain.addComparator(DomainObject.COMPARATOR_BY_ID);

	    return comparatorChain.compare(d1, d2);
	}
    }

    public static class ComparatorByMinEcts implements Comparator<DegreeModule> {

	private ExecutionPeriod executionPeriod;

	public ComparatorByMinEcts(final ExecutionPeriod executionPeriod) {
	    this.executionPeriod = executionPeriod;

	}

	public int compare(DegreeModule leftDegreeModule, DegreeModule rightDegreeModule) {
	    int comparationResult = leftDegreeModule.getMinEctsCredits(this.executionPeriod).compareTo(
		    rightDegreeModule.getMinEctsCredits(this.executionPeriod));
	    return (comparationResult == 0) ? leftDegreeModule.getIdInternal().compareTo(rightDegreeModule.getIdInternal())
		    : comparationResult;
	}

    }

    public DegreeModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    /**
     * We need a method to return a full name of a course group - from it's
     * parent course group to the degree curricular plan's root course group.
     * 
     * Given this is impossible, for there are many routes from the root course
     * group to one particular course group, we choose (for now...) to get one
     * possible full name, always visiting the first element of every list of
     * contexts on our way to the root course group.
     * 
     * @return A string with one possible full name of this course group
     */
    protected void getOneFullName(final StringBuilder result, final ExecutionPeriod executionPeriod) {
	final String selfName = getNameI18N(executionPeriod).getContent(LanguageUtils.getUserLanguage());

	if (isRoot()) {
	    result.append(selfName);
	} else {
	    List<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionPeriod(executionPeriod);
	    if (parentContextsByExecutionPeriod.isEmpty()) {
		// if not existing, just return all (as previous implementation
		// of method
		parentContextsByExecutionPeriod = getParentContexts();
	    }

	    final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.get(0).getParentCourseGroup();

	    parentCourseGroup.getOneFullName(result, executionPeriod);
	    result.append(FULL_NAME_SEPARATOR);
	    result.append(selfName);
	}
    }

    private static final String FULL_NAME_SEPARATOR = " > ";

    public String getOneFullName(final ExecutionPeriod executionPeriod) {
	final StringBuilder result = new StringBuilder();
	getOneFullName(result, executionPeriod);
	return result.toString();
    }

    public String getOneFullName() {
	return getOneFullName(null);
    }

    public MultiLanguageString getNameI18N(final ExecutionPeriod executionPeriod) {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();

	String name = getName(executionPeriod);
	if (name != null && name.length() > 0) {
	    multiLanguageString.setContent(Language.pt, name);
	}

	String nameEn = getNameEn(executionPeriod);
	if (nameEn != null && nameEn.length() > 0) {
	    multiLanguageString.setContent(Language.en, nameEn);
	}

	return multiLanguageString;
    }

    public MultiLanguageString getNameI18N() {
	return getNameI18N(null);
    }

    protected String getName(final ExecutionPeriod executionPeriod) {
	return getName();
    }

    protected String getNameEn(final ExecutionPeriod executionPeriod) {
	return getNameEn();
    }

    public void delete() {
	if (getCanBeDeleted()) {
	    for (; !getParentContexts().isEmpty(); getParentContexts().get(0).delete())
		;
	    for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete())
		;
	    for (; !getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0)
		    .delete())
		;
	    for (; !getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(
		    0).delete())
		;
	} else {
	    throw new DomainException("courseGroup.notEmptyCurriculumModules");
	}
    }

    protected Boolean getCanBeDeleted() {
	return !hasAnyCurriculumModules();
    }

    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	if (isRoot()) {
	    throw new DomainException("degreeModule.cannot.add.context.to.root");
	}
	if (!parentCourseGroup.allowChildWith(beginExecutionPeriod)) {
	    throw new DomainException("degreeModule.cannot.add.context.with.begin.execution.period", parentCourseGroup.getName(),
		    beginExecutionPeriod.getName(), beginExecutionPeriod.getExecutionYear().getYear());
	}

	checkContextsFor(parentCourseGroup, curricularPeriod, null);
	checkOwnRestrictions(parentCourseGroup, curricularPeriod, endExecutionPeriod);

	return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public void editContext(Context context, CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	checkContextsFor(parentCourseGroup, curricularPeriod, context);
	checkOwnRestrictions(parentCourseGroup, curricularPeriod, endExecutionPeriod);
	context.edit(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public void deleteContext(Context context) {
	if (hasParentContexts(context)) {
	    context.delete();
	}
	if (!hasAnyParentContexts()) {
	    delete();
	}
    }

    public Set<CourseGroup> getAllParentCourseGroups() {
	Set<CourseGroup> result = new HashSet<CourseGroup>();
	collectParentCourseGroups(result, this);
	return result;
    }

    private void collectParentCourseGroups(Set<CourseGroup> result, DegreeModule module) {
	for (Context parent : module.getParentContexts()) {
	    if (!parent.getParentCourseGroup().isRoot()) {
		result.add(parent.getParentCourseGroup());
		collectParentCourseGroups(result, parent.getParentCourseGroup());
	    }
	}
    }

    public List<CurricularRule> getParticipatingCurricularRules() {
	List<CurricularRule> result = new ArrayList<CurricularRule>();
	result.addAll(getParticipatingPrecedenceCurricularRules());
	result.addAll(getParticipatingExclusivenessCurricularRules());
	addOwnPartipatingCurricularRules(result);
	return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionYear executionYear) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (executionYear == null || curricularRule.isValid(executionYear)) {
		result.add(curricularRule);
	    }
	}

	return result;
    }

    public List<CurricularRule> getCurricularRules(final ExecutionPeriod executionPeriod) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (executionPeriod == null || curricularRule.isValid(executionPeriod)) {
		result.add(curricularRule);
	    }
	}

	return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionYear executionYear) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (curricularRule.isVisible() && (executionYear == null || curricularRule.isValid(executionYear))) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionPeriod executionPeriod) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (curricularRule.isVisible() && (executionPeriod == null || curricularRule.isValid(executionPeriod))) {
		result.add(curricularRule);
	    }
	}

	return result;
    }

    public List<Context> getParentContextsByExecutionYear(ExecutionYear executionYear) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getParentContextsSet()) {
	    if (executionYear == null || context.isValid(executionYear)) {
		result.add(context);
	    }
	}
	return result;
    }

    public List<Context> getParentContextsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getParentContextsSet()) {
	    if (executionPeriod == null || context.isValid(executionPeriod)) {
		result.add(context);
	    }
	}
	return result;
    }

    public List<Context> getParentContextsBy(final ExecutionPeriod executionPeriod, final CourseGroup parentCourseGroup) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getParentContextsSet()) {
	    if (context.isValid(executionPeriod) && context.getParentCourseGroup() == parentCourseGroup) {
		result.add(context);
	    }
	}

	return result;
    }

    public boolean hasAnyParentContexts(final ExecutionPeriod executionPeriod) {
	for (final Context context : getParentContextsSet()) {
	    if (executionPeriod == null || context.isValid(executionPeriod)) {
		return true;
	    }
	}

	return false;
    }

    public boolean isBolonhaDegree() {
	return getParentDegreeCurricularPlan().isBolonhaDegree();
    }

    public boolean isOptional() {
	return false;
    }

    public boolean isCycleCourseGroup() {
	return false;
    }

    public boolean isCurricularCourse() {
	return false;
    }

    public boolean isCourseGroup() {
	return false;
    }

    public Degree getDegree() {
	return getParentDegreeCurricularPlan().getDegree();
    }

    public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    public boolean hasOnlyOneParentCourseGroup() {
	return hasOnlyOneParentCourseGroup(null);
    }

    public boolean hasOnlyOneParentCourseGroup(final ExecutionPeriod executionPeriod) {
	DegreeModule degreeModule = null;
	for (final Context context : getParentContextsByExecutionPeriod(executionPeriod)) {
	    if (degreeModule == null) {
		degreeModule = context.getParentCourseGroup();

	    } else if (degreeModule != context.getParentCourseGroup()) {
		return false;
	    }
	}
	return true;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
	    final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionPeriod)) {
	    if (curricularRule.getCurricularRuleType() == ruleType) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public boolean hasAnyCurricularRules(final CurricularRuleType ruleType, final ExecutionPeriod executionPeriod) {
	for (final ICurricularRule curricularRule : getCurricularRules(executionPeriod)) {
	    if (curricularRule.getCurricularRuleType() == ruleType) {
		return true;
	    }
	}

	return false;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType, final ExecutionYear executionYear) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionYear)) {
	    if (curricularRule.getCurricularRuleType() == ruleType) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
	    final CourseGroup parentCourseGroup, final ExecutionYear executionYear) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionYear)) {
	    if (curricularRule.getCurricularRuleType() == ruleType
		    && (!curricularRule.hasContextCourseGroup() || curricularRule.getContextCourseGroup() == parentCourseGroup)) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
	    final CourseGroup parentCourseGroup, final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionPeriod)) {
	    if (curricularRule.getCurricularRuleType() == ruleType
		    && (!curricularRule.hasContextCourseGroup() || curricularRule.getContextCourseGroup() == parentCourseGroup)) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
	    final CourseGroup parentCourseGroup, final ExecutionYear executionYear) {
	final List<ICurricularRule> curricularRules = new ArrayList<ICurricularRule>(getCurricularRules(ruleType,
		parentCourseGroup, (ExecutionYear) null));
	Collections.sort(curricularRules, ICurricularRule.COMPARATOR_BY_BEGIN);

	if (curricularRules.isEmpty()) {
	    return null;

	} else if (executionYear == null) {
	    final ListIterator<ICurricularRule> iter = curricularRules.listIterator(curricularRules.size());
	    while (iter.hasPrevious()) {
		final ICurricularRule curricularRule = iter.previous();
		if (curricularRule.isActive()) {
		    return curricularRule;
		}
	    }
	    return null;

	} else {
	    ICurricularRule result = null;
	    for (final ICurricularRule curricularRule : curricularRules) {
		if (curricularRule.isValid(executionYear)) {
		    if (result != null) {
			// TODO: remove this throw when curricular rule ensures
			// that it can be only one active for execution period
			throw new DomainException("error.degree.module.has.more.than.one.credits.limit.for.executionYear",
				getName());
		    }
		    result = curricularRule;
		}
	    }

	    return result;
	}
    }

    public ICurricularRule getMostRecentActiveCurricularRule(final CurricularRuleType ruleType,
	    final CourseGroup parentCourseGroup, final ExecutionPeriod executionPeriod) {

	final List<ICurricularRule> curricularRules = new ArrayList<ICurricularRule>(getCurricularRules(ruleType,
		parentCourseGroup, executionPeriod));

	if (curricularRules.isEmpty()) {
	    return null;
	}

	ICurricularRule result = null;
	for (final ICurricularRule curricularRule : curricularRules) {
	    if (curricularRule.isValid(executionPeriod)) {
		if (result != null) {
		    // TODO: remove this throw when curricular rule ensures
		    // that it can be only one active for execution period
		    throw new DomainException("error.degree.module.has.more.than.one.credits.limit.for.executionPeriod",
			    getName());
		}
		result = curricularRule;
	    }
	}

	return result;
    }

    public Double getMaxEctsCredits() {
	return getMaxEctsCredits(ExecutionPeriod.readActualExecutionPeriod());
    }

    public Double getMinEctsCredits() {
	return getMinEctsCredits(ExecutionPeriod.readActualExecutionPeriod());
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return this.equals(degreeModule);
    }

    public ExecutionPeriod getMinimumExecutionPeriod() {
	if (isRoot()) {
	    return isBolonhaDegree() ? getBeginBolonhaExecutionPeriod() : getFirstExecutionPeriodOfFirstExecutionDegree();
	}
	final SortedSet<ExecutionPeriod> executionPeriods = new TreeSet<ExecutionPeriod>();
	for (final Context context : getParentContextsSet()) {
	    executionPeriods.add(context.getBeginExecutionPeriod());
	}
	return executionPeriods.first();
    }

    private ExecutionPeriod getBeginBolonhaExecutionPeriod() {
	final String year = PropertiesManager.getProperty("start.year.for.bolonha.degrees");
	final Integer semester = Integer.valueOf(PropertiesManager.getProperty("start.semester.for.bolonha.degrees"));
	return ExecutionPeriod.readBySemesterAndExecutionYear(semester, year);
    }

    private ExecutionPeriod getFirstExecutionPeriodOfFirstExecutionDegree() {
	final ExecutionDegree executionDegree = getParentDegreeCurricularPlan().getFirstExecutionDegree();
	return executionDegree != null ? executionDegree.getExecutionYear().getFirstExecutionPeriod()
		: getBeginBolonhaExecutionPeriod();
    }

    public DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final ExecutionPeriod executionPeriod) {
	final List<DegreeModulesSelectionLimit> result = (List<DegreeModulesSelectionLimit>) getCurricularRules(
		CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionPeriod);
	return result.isEmpty() ? null : (DegreeModulesSelectionLimit) result.get(0);
    }

    public CreditsLimit getCreditsLimitRule(final ExecutionPeriod executionPeriod) {
	final List<? extends ICurricularRule> result = getCurricularRules(CurricularRuleType.CREDITS_LIMIT, executionPeriod);

	return result.isEmpty() ? null : (CreditsLimit) result.get(0);
    }

    public List<Exclusiveness> getExclusivenessRules(final ExecutionPeriod executionPeriod) {
	return (List<Exclusiveness>) getCurricularRules(CurricularRuleType.EXCLUSIVENESS, executionPeriod);
    }

    public Set<EquivalencePlanEntry> getNewDegreeModuleEquivalencePlanEntries(final EquivalencePlan equivalencePlan) {
	final Set<EquivalencePlanEntry> equivalencePlanEntries = new TreeSet<EquivalencePlanEntry>(
		EquivalencePlanEntry.COMPARATOR);
	for (final EquivalencePlanEntry equivalencePlanEntry : getNewEquivalencePlanEntriesSet()) {
	    if (equivalencePlanEntry.getEquivalencePlan() == equivalencePlan) {
		equivalencePlanEntries.add(equivalencePlanEntry);
	    }
	}
	return equivalencePlanEntries;
    }

    abstract public DegreeCurricularPlan getParentDegreeCurricularPlan();

    abstract public void print(StringBuilder stringBuffer, String tabs, Context previousContext);

    abstract public boolean isLeaf();

    abstract public boolean isRoot();

    abstract public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod);

    abstract public Double getMinEctsCredits(final ExecutionPeriod executionPeriod);

    abstract protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod,
	    final Context context);

    abstract protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result);

    abstract protected void checkOwnRestrictions(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod,
	    final ExecutionPeriod executionPeriod);

    abstract public void getAllDegreeModules(final Collection<DegreeModule> degreeModules);

    abstract public Set<CurricularCourse> getAllCurricularCourses(final ExecutionPeriod executionPeriod);

    abstract public Set<CurricularCourse> getAllCurricularCourses();

    public Collection<CycleCourseGroup> getParentCycleCourseGroups() {
	Collection<CycleCourseGroup> res = new HashSet<CycleCourseGroup>();
	for (CourseGroup courseGroup : getParentCourseGroups()) {
	    res.addAll(courseGroup.getParentCycleCourseGroups());
	}
	return res;
    }

    public Set<CourseGroup> getParentCourseGroups() {
	Set<CourseGroup> res = new HashSet<CourseGroup>();
	for (Context context : getParentContextsSet()) {
	    res.add(context.getParentCourseGroup());
	}
	return res;
    }

}
