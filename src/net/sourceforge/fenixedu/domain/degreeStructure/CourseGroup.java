package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.collections.comparators.ReverseComparator;

public class CourseGroup extends CourseGroup_Base {

    static public List<CourseGroup> readCourseGroups() {
	final List<CourseGroup> result = new ArrayList<CourseGroup>();
	for (final DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModules()) {
	    if (degreeModule instanceof CourseGroup) {
		result.add((CourseGroup) degreeModule);
	    }
	}
	return result;
    }

    protected CourseGroup() {
	super();
    }

    protected CourseGroup(final String name, final String nameEn) {
	this();
	init(name, nameEn);
    }

    protected void init(final String name, final String nameEn) {
	super.setName(StringFormatter.prettyPrint(name));
	super.setNameEn(StringFormatter.prettyPrint(nameEn));
    }

    public CourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn, final ExecutionPeriod begin,
	    final ExecutionPeriod end) {
	init(parentCourseGroup, name, nameEn, begin, end);
    }

    protected void init(CourseGroup parentCourseGroup, String name, String nameEn, ExecutionPeriod begin, ExecutionPeriod end) {
	init(name, nameEn);
	if (parentCourseGroup == null) {
	    throw new DomainException("error.degreeStructure.CourseGroup.parentCourseGroup.cannot.be.null");
	}
	parentCourseGroup.checkDuplicateChildNames(name, nameEn);
	new Context(parentCourseGroup, this, null, begin, end);
    }

    public boolean isLeaf() {
	return false;
    }

    public void edit(String name, String nameEn, Context context, ExecutionPeriod beginExecutionPeriod,
	    ExecutionPeriod endExecutionPeriod) {
	// override, assure that root's name equals degree curricular plan name
	if (this.isRoot()) {
	    setName(getParentDegreeCurricularPlan().getName());
	    setNameEn(getParentDegreeCurricularPlan().getName());
	} else {
	    setName(StringFormatter.prettyPrint(name));
	    setNameEn(StringFormatter.prettyPrint(nameEn));
	}

	this.checkDuplicateBrotherNames(name, nameEn);

	if (!this.isRoot() && context != null) {
	    context.edit(beginExecutionPeriod, endExecutionPeriod);
	}
    }

    public Boolean getCanBeDeleted() {
	return super.getCanBeDeleted() && !hasAnyChildContexts() && !hasAnyOldCourseGroupChangeRequests()
		&& !hasAnyNewCourseGroupChangeRequests();
    }

    public void delete() {
	if (getCanBeDeleted()) {
	    super.delete();
	    for (; !getParticipatingContextCurricularRules().isEmpty(); getParticipatingContextCurricularRules().get(0).delete())
		;
	    removeRootDomainObject();
	    super.deleteDomainObject();
	} else {
	    throw new DomainException("courseGroup.notEmptyCourseGroupContexts");
	}
    }

    public void print(StringBuilder dcp, String tabs, Context previousContext) {
	String tab = tabs + "\t";
	dcp.append(tab);
	dcp.append("[CG ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");

	for (Context context : this.getSortedChildContextsWithCurricularCourses()) {
	    context.getChildDegreeModule().print(dcp, tab, context);
	}
	for (Context context : this.getSortedChildContextsWithCourseGroups()) {
	    context.getChildDegreeModule().print(dcp, tab, context);
	}
    }

    public boolean isRoot() {
	return false;
    }

    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
	return hasAnyParentContexts() ? getParentContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan() : null;
    }

    public List<Context> getChildContexts(Class<? extends DegreeModule> clazz) {
	return getValidChildContexts(clazz, (ExecutionYear) null);
    }

    public List<Context> getValidChildContexts(final ExecutionYear executionYear) {
	return getValidChildContexts(null, executionYear);
    }

    public List<Context> getValidChildContexts(final ExecutionPeriod executionPeriod) {
	return getValidChildContexts(null, executionPeriod);
    }

    // Valid means that is open to execution year, and if is CurricularCourse
    // the context must have same semester of any ExecutionPeriod of
    // ExecutionYear
    public List<Context> getValidChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionYear executionYear) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : this.getChildContexts()) {
	    if (hasClass(clazz, context.getChildDegreeModule()) && ((executionYear == null || context.isValid(executionYear)))) {
		result.add(context);
	    }
	}
	return result;
    }

    // Valid means that is open to execution period, and if is CurricularCourse
    // the context must have same semester than executionPeriod
    public List<Context> getValidChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionPeriod executionPeriod) {
	final List<Context> result = new ArrayList<Context>();
	for (Context context : this.getChildContexts()) {
	    if (hasClass(clazz, context.getChildDegreeModule())
		    && ((executionPeriod == null || context.isValid(executionPeriod)))) {
		result.add(context);
	    }
	}

	return result;
    }

    public List<Context> getSortedOpenChildContextsWithCurricularCourses(final ExecutionYear executionYear) {
	final List<Context> result = getOpenChildContexts(CurricularCourse.class, executionYear);
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedOpenChildContextsWithCourseGroups(final ExecutionYear executionYear) {
	final List<Context> result = this.getOpenChildContexts(CourseGroup.class, executionYear);
	Collections.sort(result);
	return result;
    }
    
    public List<Context> getSortedOpenChildContextsWithCourseGroups(final ExecutionPeriod executionPeriod) {
	final List<Context> result = this.getOpenChildContexts(CourseGroup.class, executionPeriod);
	Collections.sort(result);
	return result;
    }


    public List<Context> getOpenChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionPeriod executionPeriod) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getChildContexts()) {
	    if (hasClass(clazz, context.getChildDegreeModule()) && ((executionPeriod == null || context.isOpen(executionPeriod)))) {
		result.add(context);
	    }
	}
	return result;
    }

    public List<Context> getOpenChildContexts(final Class<? extends DegreeModule> clazz, final ExecutionYear executionYear) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getChildContexts()) {
	    if (hasClass(clazz, context.getChildDegreeModule()) && ((executionYear == null || context.isOpen(executionYear)))) {
		result.add(context);
	    }
	}
	return result;
    }

    private boolean hasClass(final Class<? extends DegreeModule> clazz, final DegreeModule degreeModule) {
	return clazz == null || clazz.isAssignableFrom(degreeModule.getClass());
    }

    public List<Context> getSortedChildContextsWithCurricularCourses() {
	List<Context> result = this.getChildContexts(CurricularCourse.class);
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCurricularCoursesByExecutionYear(ExecutionYear executionYear) {
	List<Context> result = this.getValidChildContexts(CurricularCourse.class, executionYear);
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCourseGroups() {
	List<Context> result = new ArrayList<Context>(this.getChildContexts(CourseGroup.class));
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCourseGroupsByExecutionYear(ExecutionYear executionYear) {
	List<Context> result = this.getValidChildContexts(CourseGroup.class, executionYear);
	Collections.sort(result);
	return result;
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod,
	    final Context ignoreContext) {

	for (final Context context : this.getParentContexts()) {
	    if (context != ignoreContext && context.getParentCourseGroup() == parentCourseGroup) {
		throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
	    }
	}
    }

    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
	result.addAll(getParticipatingContextCurricularRules());
    }

    protected void checkOwnRestrictions(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod) {
	parentCourseGroup.checkDuplicateChildNames(getName(), getNameEn());
    }

    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setName(String name) {
	super.setName(name);
    }

    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setNameEn(String nameEn) {
	super.setNameEn(nameEn);
    }

    public void checkDuplicateChildNames(final String name, final String nameEn) throws DomainException {
	String normalizedName = StringFormatter.normalize(name);
	String normalizedNameEn = StringFormatter.normalize(nameEn);
	if (!verifyNames(normalizedName, normalizedNameEn)) {
	    throw new DomainException("error.existingCourseGroupWithSameName");
	}
    }

    public void checkDuplicateBrotherNames(final String name, final String nameEn) throws DomainException {
	String normalizedName = StringFormatter.normalize(name);
	String normalizedNameEn = StringFormatter.normalize(nameEn);
	for (Context parentContext : getParentContexts()) {
	    CourseGroup parentCourseGroup = parentContext.getParentCourseGroup();
	    if (!parentCourseGroup.verifyNames(normalizedName, normalizedNameEn, this)) {
		throw new DomainException("error.existingCourseGroupWithSameName");
	    }
	}
    }

    private boolean verifyNames(String normalizedName, String normalizedNameEn) {
	return verifyNames(normalizedName, normalizedNameEn, this);
    }

    private boolean verifyNames(String normalizedName, String normalizedNameEn, DegreeModule excludedModule) {
	for (Context context : getChildContexts()) {
	    DegreeModule degreeModule = context.getChildDegreeModule();
	    if (degreeModule != excludedModule) {
		if (degreeModule.getName() != null && StringFormatter.normalize(degreeModule.getName()).equals(normalizedName)) {
		    return false;
		}
		if (degreeModule.getNameEn() != null
			&& StringFormatter.normalize(degreeModule.getNameEn()).equals(normalizedNameEn)) {
		    return false;
		}
	    }
	}
	return true;
    }

    public void orderChild(Context contextToOrder, int position) {
	List<Context> newSort = null;
	if (contextToOrder.getChildDegreeModule() instanceof CurricularCourse) {
	    newSort = this.getSortedChildContextsWithCurricularCourses();
	} else {
	    newSort = this.getSortedChildContextsWithCourseGroups();
	}

	if (newSort.size() <= 1 || position < 0 || position > newSort.size()) {
	    return;
	}

	newSort.remove(contextToOrder);
	newSort.add(position, contextToOrder);

	for (int newOrder = 0; newOrder < newSort.size(); newOrder++) {
	    Context context = newSort.get(newOrder);

	    if (context == contextToOrder && newOrder != position) {
		throw new DomainException("wrong.order.algorithm");
	    }
	    context.setChildOrder(newOrder);
	}
    }

    public Set<DegreeModule> collectAllChildDegreeModules(final Class<? extends DegreeModule> clazz,
	    final ExecutionYear executionYear) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : this.getValidChildContexts(executionYear)) {
	    final DegreeModule degreeModule = context.getChildDegreeModule();
	    if (clazz.isAssignableFrom(degreeModule.getClass())) {
		result.add(degreeModule);
	    }
	    if (!degreeModule.isLeaf()) {
		final CourseGroup courseGroup = (CourseGroup) degreeModule;
		result.addAll(courseGroup.collectAllChildDegreeModules(clazz, executionYear));
	    }
	}
	return result;
    }

    public Set<DegreeModule> collectAllChildDegreeModules(final Class<? extends DegreeModule> clazz,
	    final ExecutionPeriod executionPeriod) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : getValidChildContexts(executionPeriod)) {
	    final DegreeModule degreeModule = context.getChildDegreeModule();
	    if (clazz.isAssignableFrom(degreeModule.getClass())) {
		result.add(degreeModule);
	    }
	    if (!degreeModule.isLeaf()) {
		final CourseGroup courseGroup = (CourseGroup) degreeModule;
		result.addAll(courseGroup.collectAllChildDegreeModules(clazz, executionPeriod));
	    }
	}
	return result;
    }

    public void collectChildDegreeModulesIncludingFullPath(Class<? extends DegreeModule> clazz, List<List<DegreeModule>> result,
	    List<DegreeModule> previousDegreeModulesPath, ExecutionYear executionYear) {
	final List<DegreeModule> currentDegreeModulesPath = previousDegreeModulesPath;
	for (final Context context : this.getValidChildContexts(executionYear)) {
	    List<DegreeModule> newDegreeModulesPath = null;
	    if (clazz.isAssignableFrom(context.getChildDegreeModule().getClass())) {
		newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath, currentDegreeModulesPath, context
			.getChildDegreeModule());
		result.add(newDegreeModulesPath);
	    }
	    if (!context.getChildDegreeModule().isLeaf()) {
		newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath, currentDegreeModulesPath, context
			.getChildDegreeModule());
		((CourseGroup) context.getChildDegreeModule()).collectChildDegreeModulesIncludingFullPath(clazz, result,
			newDegreeModulesPath, executionYear);
	    }
	}
    }

    private List<DegreeModule> initNewDegreeModulesPath(List<DegreeModule> newDegreeModulesPath,
	    final List<DegreeModule> currentDegreeModulesPath, final DegreeModule degreeModule) {
	if (newDegreeModulesPath == null) {
	    newDegreeModulesPath = new ArrayList<DegreeModule>(currentDegreeModulesPath);
	    newDegreeModulesPath.add(degreeModule);
	}
	return newDegreeModulesPath;
    }

    public Collection<CourseGroup> getNotOptionalChildCourseGroups(final ExecutionPeriod executionPeriod) {

	final Collection<DegreeModule> degreeModules = getDegreeModulesByExecutionPeriod(executionPeriod);
	final Collection<CurricularRule> curricularRules = getCurricularRulesByExecutionPeriod(executionPeriod);
	final DegreeModulesSelectionLimit degreeModulesSelectionLimit = getDegreeModulesSelectionLimitRule(curricularRules);

	if (degreeModulesSelectionLimit != null) {

	    if (degreeModulesSelectionLimit.getMinimumLimit().equals(degreeModulesSelectionLimit.getMaximumLimit())
		    && degreeModulesSelectionLimit.getMaximumLimit().equals(degreeModules.size())) {

		return filterCourseGroups(degreeModules);

	    } else {
		return Collections.EMPTY_LIST;
	    }
	}
	return filterCourseGroups(degreeModules);
    }

    private Collection<CourseGroup> filterCourseGroups(final Collection<DegreeModule> degreeModules) {
	final Collection<CourseGroup> result = new HashSet<CourseGroup>();
	for (final DegreeModule degreeModule : degreeModules) {
	    if (!degreeModule.isLeaf()) {
		result.add((CourseGroup) degreeModule);
	    }
	}
	return result;
    }

    private DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final Collection<CurricularRule> curricularRules) {
	for (final CurricularRule curricularRule : curricularRules) {
	    if (curricularRule.getCurricularRuleType() == CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT) {
		return (DegreeModulesSelectionLimit) curricularRule;
	    }
	}
	return null;
    }

    private Collection<CurricularRule> getCurricularRulesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final Collection<CurricularRule> result = new HashSet<CurricularRule>();
	for (final CurricularRule curricularRule : this.getCurricularRulesSet()) {
	    if (curricularRule.isValid(executionPeriod)) {
		result.add(curricularRule);
	    }
	}
	return result;
    }

    private Collection<DegreeModule> getDegreeModulesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final Collection<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : this.getChildContexts()) {
	    if (context.isValid(executionPeriod)) {
		result.add(context.getChildDegreeModule());
	    }
	}
	return result;
    }

    public boolean validate(CurricularCourse curricularCourse) {
	for (final Context context : this.getChildContextsSet()) {
	    if (context.getChildDegreeModule() instanceof CurricularCourse) {
		CurricularCourse childCurricularCourse = (CurricularCourse) context.getChildDegreeModule();
		if (childCurricularCourse.isEquivalent(curricularCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<Context> getContextsWithCurricularCourseByCurricularPeriod(final CurricularPeriod curricularPeriod,
	    final ExecutionPeriod executionPeriod) {

	final Collection<Context> result = new HashSet<Context>();

	for (final Context context : this.getChildContextsSet()) {

	    if (context.getChildDegreeModule().isLeaf() && context.hasCurricularPeriod()
		    && context.getCurricularPeriod().equals(curricularPeriod) && context.isValid(executionPeriod)) {

		result.add(context);
	    }
	}
	return result;
    }

    public Set<DegreeModule> getOpenChildDegreeModulesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : getChildContexts()) {
	    if (context.isOpen(executionPeriod)) {
		result.add(context.getChildDegreeModule());
	    }
	}
	return result;
    }

    public Set<CourseGroup> getParentCourseGroups() {
	final Set<CourseGroup> result = new HashSet<CourseGroup>();
	for (final Context context : getParentContexts()) {
	    result.add(context.getParentCourseGroup());
	}
	return result;
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod) {
	final List<CreditsLimit> creditsLimitRules = (List<CreditsLimit>) getCurricularRules(CurricularRuleType.CREDITS_LIMIT,
		executionPeriod);
	if (!creditsLimitRules.isEmpty()) {
	    for (final CreditsLimit creditsLimit : creditsLimitRules) {
		if (getParentCourseGroups().contains(creditsLimit.getContextCourseGroup())) {
		    return creditsLimit.getMaximumCredits();
		}
	    }
	    return creditsLimitRules.get(0).getMaximumCredits();
	}

	final DegreeModulesSelectionLimit modulesSelectionLimit = getDegreeModulesSelectionLimitRule(executionPeriod);
	if (modulesSelectionLimit != null) {
	    final Collection<DegreeModule> modulesByExecutionPeriod = getOpenChildDegreeModulesByExecutionPeriod(executionPeriod);
	    if (modulesSelectionLimit.getMaximumLimit().intValue() != modulesByExecutionPeriod.size()) {
		return countMaxEctsCredits(modulesByExecutionPeriod, executionPeriod, modulesSelectionLimit.getMaximumLimit());
	    }
	}
	return countAllMaxEctsCredits(executionPeriod);
    }

    private Double countMaxEctsCredits(final Collection<DegreeModule> modulesByExecutionPeriod,
	    final ExecutionPeriod executionPeriod, final Integer maximumLimit) {

	final List<Double> ectsCredits = new ArrayList<Double>();
	for (final DegreeModule degreeModule : modulesByExecutionPeriod) {
	    ectsCredits.add(degreeModule.getMaxEctsCredits(executionPeriod));
	}
	Collections.sort(ectsCredits, new ReverseComparator());
	return sumEctsCredits(ectsCredits, maximumLimit.intValue());
    }

    @Override
    protected Double countAllMaxEctsCredits(final ExecutionPeriod executionPeriod) {
	double result = 0d;
	for (final DegreeModule degreeModule : getOpenChildDegreeModulesByExecutionPeriod(executionPeriod)) {
	    result += degreeModule.countAllMaxEctsCredits(executionPeriod);
	}
	return result;
    }

    @Override
    protected Double countAllMinEctsCredits(final ExecutionPeriod executionPeriod) {
	double result = 0d;
	for (final DegreeModule degreeModule : getOpenChildDegreeModulesByExecutionPeriod(executionPeriod)) {
	    result += degreeModule.countAllMinEctsCredits(executionPeriod);
	}
	return result;
    }

    @Override
    public Double getMinEctsCredits(final ExecutionPeriod executionPeriod) {
	final List<CreditsLimit> creditsLimitRules = (List<CreditsLimit>) getCurricularRules(CurricularRuleType.CREDITS_LIMIT,
		executionPeriod);
	if (!creditsLimitRules.isEmpty()) {
	    for (final CreditsLimit creditsLimit : creditsLimitRules) {
		if (getParentCourseGroups().contains(creditsLimit.getContextCourseGroup())) {
		    return creditsLimit.getMaximumCredits();
		}
	    }
	    return creditsLimitRules.get(0).getMinimumCredits();
	}

	final DegreeModulesSelectionLimit modulesSelectionLimit = getDegreeModulesSelectionLimitRule(executionPeriod);
	if (modulesSelectionLimit != null) {
	    final Collection<DegreeModule> modulesByExecutionPeriod = getOpenChildDegreeModulesByExecutionPeriod(executionPeriod);
	    if (modulesSelectionLimit.getMinimumLimit().intValue() != modulesByExecutionPeriod.size()) {
		return countMinEctsCredits(modulesByExecutionPeriod, executionPeriod, modulesSelectionLimit.getMinimumLimit());
	    }
	}
	return countAllMinEctsCredits(executionPeriod);
    }

    private Double countMinEctsCredits(final Collection<DegreeModule> modulesByExecutionPeriod,
	    final ExecutionPeriod executionPeriod, final Integer minimumLimit) {

	final List<Double> ectsCredits = new ArrayList<Double>();
	for (final DegreeModule degreeModule : modulesByExecutionPeriod) {
	    ectsCredits.add(degreeModule.getMinEctsCredits(executionPeriod));
	}
	Collections.sort(ectsCredits, new ReverseComparator());
	return sumEctsCredits(ectsCredits, minimumLimit.intValue());
    }

    private Double sumEctsCredits(final List<Double> ectsCredits, int limit) {
	double result = 0d;
	final Iterator<Double> ectsCreditsIter = ectsCredits.iterator();
	for (; ectsCreditsIter.hasNext() && limit > 0; limit--) {
	    result += ectsCreditsIter.next().doubleValue();
	}
	return Double.valueOf(result);
    }

    @Override
    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	if (super.hasDegreeModule(degreeModule)) {
	    return true;
	}
	for (final Context context : getChildContexts()) {
	    if (context.getChildDegreeModule().hasDegreeModule(degreeModule)) {
		return true;
	    }
	}
	return false;
    }

    public Context addCurricularCourse(final CurricularPeriod curricularPeriod, final CurricularCourse curricularCourse,
	    final ExecutionPeriod start, final ExecutionPeriod end) {
	return curricularCourse.addContext(this, curricularPeriod, start, end);
    }

    @Override
    public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
	degreeModules.add(this);
	for (Context context : getChildContexts()) {
	    context.getAllDegreeModules(degreeModules);
	}
    }

    public void getAllCoursesGroupse(final Set<CourseGroup> courseGroups) {
	for (final Context context : getChildContextsSet()) {
	    context.addAllCourseGroups(courseGroups);
	}
    }

    public boolean allowChildWith(final ExecutionPeriod executionPeriod) {
	return getMinimumExecutionPeriod().isBeforeOrEquals(executionPeriod);
    }

    public Set<Context> getChildContextsSortedByDegreeModuleName() {
	final Set<Context> contexts = new TreeSet<Context>(Context.COMPARATOR_BY_DEGREE_MODULE_NAME);
	contexts.addAll(getChildContextsSet());
	return contexts;
    }

    public Set<DegreeModule> getChildDegreeModules() {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : getChildContexts()) {
	    result.add(context.getChildDegreeModule());
	}
	return result;
    }

    public Set<DegreeModule> getChildDegreeModulesValidOn(final ExecutionPeriod executionPeriod) {
	final Set<DegreeModule> result = new HashSet<DegreeModule>();
	for (final Context context : getValidChildContexts(executionPeriod)) {
	    result.add(context.getChildDegreeModule());
	}

	return result;
    }

    public Set<Context> getActiveChildContexts() {
	final Set<Context> result = new HashSet<Context>();

	for (final Context context : getChildContexts()) {
	    if (context.isOpen()) {
		result.add(context);
	    }
	}

	return result;
    }

    public Set<Context> getActiveChildContextsWithMax(final ExecutionPeriod executionPeriod) {
	final Map<DegreeModule, Context> maxContextsByDegreeModule = new HashMap<DegreeModule, Context>();

	for (final Context context : getActiveChildContexts()) {
	    if (maxContextsByDegreeModule.containsKey(context.getChildDegreeModule())) {
		final Context existingContext = maxContextsByDegreeModule.get(context.getChildDegreeModule());
		if (existingContext.getCurricularPeriod().getChildOrder().intValue() != executionPeriod.getSemester().intValue()
			&& context.getCurricularPeriod().getChildOrder().intValue() == executionPeriod.getSemester().intValue()) {
		    maxContextsByDegreeModule.put(context.getChildDegreeModule(), context);
		}

	    } else {
		maxContextsByDegreeModule.put(context.getChildDegreeModule(), context);
	    }
	}

	return new HashSet<Context>(maxContextsByDegreeModule.values());
    }

    public Map<CurricularPeriod, Set<Context>> getActiveChildCurricularContextsWithMaxByCurricularPeriod(
	    final ExecutionPeriod executionPeriod) {
	final Map<CurricularPeriod, Set<Context>> result = new HashMap<CurricularPeriod, Set<Context>>();

	for (final Context context : getActiveChildContextsWithMax(executionPeriod)) {
	    if (context.getChildDegreeModule().isCurricularCourse()) {
		if (!result.containsKey(context.getCurricularPeriod())) {
		    result.put(context.getCurricularPeriod(), new HashSet<Context>());
		}

		result.get(context.getCurricularPeriod()).add(context);
	    }
	}

	return result;
    }

    public Set<CurricularCourse> getChildCurricularCoursesValidOn(final ExecutionPeriod executionPeriod) {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();

	for (final Context context : getValidChildContexts(executionPeriod)) {
	    if (context.getChildDegreeModule().isCurricularCourse()) {
		result.add((CurricularCourse) context.getChildDegreeModule());
	    }
	}

	return result;
    }

    public List<Context> getChildContextsForCurricularCourses(final ExecutionPeriod executionPeriod) {
	final List<Context> result = new ArrayList<Context>();
	for (final Context context : getChildContexts(CurricularCourse.class)) {
	    if (context.isValid(executionPeriod)) {
		result.add(context);
	    }
	}

	return result;
    }

    public Set<Context> getActiveChildContextsWithMaxCurricularPeriodForCurricularCourses(final ExecutionPeriod executionPeriod) {
	final Set<Context> result = new HashSet<Context>();
	for (final Context context : getActiveChildContextsWithMax(executionPeriod)) {
	    if (context.getChildDegreeModule().isCurricularCourse()) {
		result.add(context);
	    }
	}

	return result;
    }

    public boolean hasDegreeModuleOnChilds(final DegreeModule degreeModuleToSearch) {
	for (final Context context : getChildContexts()) {
	    if (context.getChildDegreeModule() == degreeModuleToSearch) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean isCourseGroup() {
	return true;
    }
    
    @Override
    public Set<CurricularCourse> getAllCurricularCourses(final ExecutionPeriod executionPeriod) {
	final Set<CurricularCourse> result = new HashSet<CurricularCourse>();
	for (final Context context : getChildContexts()) {
	    if(executionPeriod == null || context.isOpen(executionPeriod)) {
		result.addAll(context.getChildDegreeModule().getAllCurricularCourses());
	    }
	}
	return result;
    }
    
    @Override
    public Set<CurricularCourse> getAllCurricularCourses() {
	return getAllCurricularCourses(null);
    }
}
