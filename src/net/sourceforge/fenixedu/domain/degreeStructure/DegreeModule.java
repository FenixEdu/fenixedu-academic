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

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.EquivalencePlan;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.util.i18n.Language;
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

	private ExecutionSemester executionSemester;

	public ComparatorByMinEcts(final ExecutionSemester executionSemester) {
	    this.executionSemester = executionSemester;

	}

	public int compare(DegreeModule leftDegreeModule, DegreeModule rightDegreeModule) {
	    int comparationResult = leftDegreeModule.getMinEctsCredits(this.executionSemester).compareTo(
		    rightDegreeModule.getMinEctsCredits(this.executionSemester));
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
    protected void getOneFullName(final StringBuilder result, final ExecutionSemester executionSemester) {
	final String selfName = getNameI18N(executionSemester).getContent(Language.getUserLanguage());

	if (isRoot()) {
	    result.append(selfName);
	} else {
	    List<Context> parentContextsByExecutionPeriod = getParentContextsByExecutionPeriod(executionSemester);
	    if (parentContextsByExecutionPeriod.isEmpty()) {
		// if not existing, just return all (as previous implementation
		// of method
		parentContextsByExecutionPeriod = getParentContexts();
	    }

	    final CourseGroup parentCourseGroup = parentContextsByExecutionPeriod.get(0).getParentCourseGroup();

	    parentCourseGroup.getOneFullName(result, executionSemester);
	    result.append(FULL_NAME_SEPARATOR);
	    result.append(selfName);
	}
    }

    private static final String FULL_NAME_SEPARATOR = " > ";

    public String getOneFullName(final ExecutionSemester executionSemester) {
	final StringBuilder result = new StringBuilder();
	getOneFullName(result, executionSemester);
	return result.toString();
    }

    public String getOneFullName() {
	return getOneFullName(null);
    }

    public MultiLanguageString getNameI18N(final ExecutionSemester executionSemester) {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();

	String name = getName(executionSemester);
	if (name != null && name.length() > 0) {
	    multiLanguageString.setContent(Language.pt, name);
	}

	String nameEn = getNameEn(executionSemester);
	if (nameEn != null && nameEn.length() > 0) {
	    multiLanguageString.setContent(Language.en, nameEn);
	}

	return multiLanguageString;
    }

    public MultiLanguageString getNameI18N() {
	return getNameI18N(null);
    }

    protected String getName(final ExecutionSemester executionSemester) {
	return getName();
    }

    protected String getNameEn(final ExecutionSemester executionSemester) {
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
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	result.addAll(getParticipatingPrecedenceCurricularRules());
	result.addAll(getParticipatingExclusivenessCurricularRules());
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

    public List<CurricularRule> getCurricularRules(final ExecutionSemester executionSemester) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (executionSemester == null || curricularRule.isValid(executionSemester)) {
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

    public List<CurricularRule> getVisibleCurricularRules(final ExecutionSemester executionSemester) {
	final List<CurricularRule> result = new ArrayList<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRules()) {
	    if (curricularRule.isVisible() && (executionSemester == null || curricularRule.isValid(executionSemester))) {
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

    public List<Context> getParentContextsByExecutionPeriod(final ExecutionSemester executionSemester) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getParentContextsSet()) {
	    if (executionSemester == null || context.isValid(executionSemester)) {
		result.add(context);
	    }
	}
	return result;
    }

    public List<Context> getParentContextsBy(final ExecutionSemester executionSemester, final CourseGroup parentCourseGroup) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getParentContextsSet()) {
	    if (context.isValid(executionSemester) && context.getParentCourseGroup() == parentCourseGroup) {
		result.add(context);
	    }
	}

	return result;
    }

    public boolean hasAnyParentContexts(final ExecutionSemester executionSemester) {
	for (final Context context : getParentContextsSet()) {
	    if (executionSemester == null || context.isValid(executionSemester)) {
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

    public boolean hasOnlyOneParentCourseGroup(final ExecutionSemester executionSemester) {
	DegreeModule degreeModule = null;
	for (final Context context : getParentContextsByExecutionPeriod(executionSemester)) {
	    if (degreeModule == null) {
		degreeModule = context.getParentCourseGroup();

	    } else if (degreeModule != context.getParentCourseGroup()) {
		return false;
	    }
	}
	return true;
    }

    public List<? extends ICurricularRule> getCurricularRules(final CurricularRuleType ruleType,
	    final ExecutionSemester executionSemester) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionSemester)) {
	    if (curricularRule.getCurricularRuleType() == ruleType) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    public boolean hasAnyCurricularRules(final CurricularRuleType ruleType, final ExecutionSemester executionSemester) {
	for (final ICurricularRule curricularRule : getCurricularRules(executionSemester)) {
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
	    final CourseGroup parentCourseGroup, final ExecutionSemester executionSemester) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionSemester)) {
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
	    final CourseGroup parentCourseGroup, final ExecutionSemester executionSemester) {

	final List<ICurricularRule> curricularRules = new ArrayList<ICurricularRule>(getCurricularRules(ruleType,
		parentCourseGroup, executionSemester));

	if (curricularRules.isEmpty()) {
	    return null;
	}

	ICurricularRule result = null;
	for (final ICurricularRule curricularRule : curricularRules) {
	    if (curricularRule.isValid(executionSemester)) {
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
	return getMaxEctsCredits(ExecutionSemester.readActualExecutionSemester());
    }

    public Double getMinEctsCredits() {
	return getMinEctsCredits(ExecutionSemester.readActualExecutionSemester());
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return this.equals(degreeModule);
    }

    public ExecutionSemester getMinimumExecutionPeriod() {
	if (isRoot()) {
	    return isBolonhaDegree() ? getBeginBolonhaExecutionPeriod() : getFirstExecutionPeriodOfFirstExecutionDegree();
	}
	final SortedSet<ExecutionSemester> executionSemesters = new TreeSet<ExecutionSemester>();
	for (final Context context : getParentContextsSet()) {
	    executionSemesters.add(context.getBeginExecutionPeriod());
	}
	return executionSemesters.first();
    }

    public ExecutionSemester getBeginBolonhaExecutionPeriod() {
	return ExecutionSemester.readFirstBolonhaExecutionPeriod();
    }

    private ExecutionSemester getFirstExecutionPeriodOfFirstExecutionDegree() {
	final ExecutionDegree executionDegree = getParentDegreeCurricularPlan().getFirstExecutionDegree();
	return executionDegree != null ? executionDegree.getExecutionYear().getFirstExecutionPeriod()
		: getBeginBolonhaExecutionPeriod();
    }

    public DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final ExecutionSemester executionSemester) {
	final List<DegreeModulesSelectionLimit> result = (List<DegreeModulesSelectionLimit>) getCurricularRules(
		CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionSemester);
	return result.isEmpty() ? null : (DegreeModulesSelectionLimit) result.get(0);
    }

    public CreditsLimit getCreditsLimitRule(final ExecutionSemester executionSemester) {
	final List<? extends ICurricularRule> result = getCurricularRules(CurricularRuleType.CREDITS_LIMIT, executionSemester);

	return result.isEmpty() ? null : (CreditsLimit) result.get(0);
    }

    public List<Exclusiveness> getExclusivenessRules(final ExecutionSemester executionSemester) {
	return (List<Exclusiveness>) getCurricularRules(CurricularRuleType.EXCLUSIVENESS, executionSemester);
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

    abstract public DegreeCurricularPlan getParentDegreeCurricularPlan();

    abstract public void print(StringBuilder stringBuffer, String tabs, Context previousContext);

    abstract public boolean isLeaf();

    abstract public boolean isRoot();

    abstract public Double getMaxEctsCredits(final ExecutionSemester executionSemester);

    abstract public Double getMinEctsCredits(final ExecutionSemester executionSemester);

    abstract public void getAllDegreeModules(final Collection<DegreeModule> degreeModules);

    abstract public Set<CurricularCourse> getAllCurricularCourses(final ExecutionSemester executionSemester);

    abstract public Set<CurricularCourse> getAllCurricularCourses();

    abstract public void doForAllCurricularCourses(final CurricularCourseFunctor curricularCourseFunctor);

}
