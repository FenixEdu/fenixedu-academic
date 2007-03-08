package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
    
    static public CourseGroup createRoot(final DegreeCurricularPlan degreeCurricularPlan, final String name, final String nameEn, final DegreeType courseGroupType) {
	return new CourseGroup(degreeCurricularPlan, name, nameEn, courseGroupType);
    }

    protected CourseGroup() {
	super();
    }

    protected CourseGroup(final String name, final String nameEn, final DegreeType courseGroupType) {
	this();
	init(name, nameEn, courseGroupType);
    }
    
    protected void init(final String name, final String nameEn, final DegreeType courseGroupType) {
	super.setName(StringFormatter.prettyPrint(name));
	super.setNameEn(StringFormatter.prettyPrint(nameEn));
	super.setCourseGroupType(courseGroupType);
    }
    
    public CourseGroup(final DegreeCurricularPlan degreeCurricularPlan, final String name, final String nameEn, final DegreeType courseGroupType) {
	this(name, nameEn, courseGroupType);
	if (degreeCurricularPlan == null) {
	    throw new DomainException("error.degreeStructure.CourseGroup.degreeCurricularPlan.cannot.be.null");
	}
	setParentDegreeCurricularPlan(degreeCurricularPlan);
    }
    
    public CourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
	    final DegreeType courseGroupType, final ExecutionPeriod begin, final ExecutionPeriod end) {
	
	init(name, nameEn, courseGroupType);
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
	return super.getCanBeDeleted() && !hasAnyChildContexts();
    }

    public void delete() {
	if (getCanBeDeleted()) {
	    removeParentDegreeCurricularPlan();
	    super.delete();
	    for (; !getParticipatingContextCurricularRules().isEmpty(); getParticipatingContextCurricularRules()
		    .get(0).delete())
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
	return (super.getParentDegreeCurricularPlan() != null);
    }

    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
	if (isRoot()) {
	    return super.getParentDegreeCurricularPlan();
	}
	return getParentContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan();
    }

    public List<Context> getChildContexts(Class<? extends DegreeModule> clazz) {
	return getChildContexts(clazz, (ExecutionYear) null);
    }

    public List<Context> getChildContexts(ExecutionYear executionYear) {
	return getChildContexts(null, executionYear);
    }

    public List<Context> getChildContexts(Class<? extends DegreeModule> clazz,
	    ExecutionYear executionYear) {
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getChildContexts()) {
	    if ((clazz == null || clazz.isAssignableFrom(context.getChildDegreeModule().getClass()))
		    && ((executionYear == null || context.isValid(executionYear)))) {
		result.add(context);
	    }
	}

	return result;
    }
    
    public List<Context> getChildContexts(ExecutionPeriod executionPeriod) {
	return getChildContexts(null, executionPeriod);
    }

    public List<Context> getChildContexts(Class<? extends DegreeModule> clazz,
	    ExecutionPeriod executionPeriod) {
	List<Context> result = new ArrayList<Context>();
	for (Context context : this.getChildContexts()) {
	    if ((clazz == null || clazz.isAssignableFrom(context.getChildDegreeModule().getClass()))
		    && ((executionPeriod == null || context.isValid(executionPeriod)))) {
		result.add(context);
	    }
	}

	return result;
    }

    public List<Context> getSortedChildContextsWithCurricularCourses() {
	List<Context> result = this.getChildContexts(CurricularCourse.class);
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCurricularCoursesByExecutionYear(
	    ExecutionYear executionYear) {
	List<Context> result = this.getChildContexts(CurricularCourse.class, executionYear);
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCourseGroups() {
	List<Context> result = new ArrayList<Context>(this.getChildContexts(CourseGroup.class));
	Collections.sort(result);
	return result;
    }

    public List<Context> getSortedChildContextsWithCourseGroupsByExecutionYear(
	    ExecutionYear executionYear) {
	List<Context> result = this.getChildContexts(CourseGroup.class, executionYear);
	Collections.sort(result);
	return result;
    }

    protected void checkContextsFor(final CourseGroup parentCourseGroup,
	    final CurricularPeriod curricularPeriod, final Context ignoreContext) {

	for (final Context context : this.getParentContexts()) {
	    if (context != ignoreContext && context.getParentCourseGroup() == parentCourseGroup) {
		throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
	    }
	}
    }

    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
	result.addAll(getParticipatingContextCurricularRules());
    }

    protected void checkOwnRestrictions(final CourseGroup parentCourseGroup,
	    final CurricularPeriod curricularPeriod) {
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
    
    @Override
    @Checked("CourseGroupPredicates.curricularPlanMemberWritePredicate")
    public void setCourseGroupType(DegreeType courseGroupType) {
        super.setCourseGroupType(courseGroupType);
    }

    public void checkDuplicateChildNames(final String name, final String nameEn) throws DomainException {
	String normalizedName = StringFormatter.normalize(name);
	String normalizedNameEn = StringFormatter.normalize(nameEn);
	if (!verifyNames(normalizedName, normalizedNameEn)) {
	    throw new DomainException("error.existingCourseGroupWithSameName");
	}
    }

    public void checkDuplicateBrotherNames(final String name, final String nameEn)
	    throws DomainException {
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

    private boolean verifyNames(String normalizedName, String normalizedNameEn,
	    DegreeModule excludedModule) {
	for (Context context : getChildContexts()) {
	    DegreeModule degreeModule = context.getChildDegreeModule();
	    if (degreeModule != excludedModule) {
		if (degreeModule.getName() != null
			&& StringFormatter.normalize(degreeModule.getName()).equals(normalizedName)) {
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

    public void collectChildDegreeModules(Class<? extends DegreeModule> clazz,
	    final Set<DegreeModule> result, ExecutionYear executionYear) {
	for (final Context context : this.getChildContexts(executionYear)) {
	    if (clazz.isAssignableFrom(context.getChildDegreeModule().getClass())) {
		result.add(context.getChildDegreeModule());
	    }
	    if (!context.getChildDegreeModule().isLeaf()) {
		((CourseGroup) context.getChildDegreeModule()).collectChildDegreeModules(clazz, result,
			executionYear);
	    }
	}
    }

    public void collectChildDegreeModulesIncludingFullPath(Class<? extends DegreeModule> clazz,
	    List<List<DegreeModule>> result, List<DegreeModule> previousDegreeModulesPath,
	    ExecutionYear executionYear) {
	final List<DegreeModule> currentDegreeModulesPath = previousDegreeModulesPath;
	for (final Context context : this.getChildContexts(executionYear)) {
	    List<DegreeModule> newDegreeModulesPath = null;
	    if (clazz.isAssignableFrom(context.getChildDegreeModule().getClass())) {
		newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath,
			currentDegreeModulesPath, context.getChildDegreeModule());
		result.add(newDegreeModulesPath);
	    }
	    if (!context.getChildDegreeModule().isLeaf()) {
		newDegreeModulesPath = initNewDegreeModulesPath(newDegreeModulesPath,
			currentDegreeModulesPath, context.getChildDegreeModule());
		((CourseGroup) context.getChildDegreeModule())
			.collectChildDegreeModulesIncludingFullPath(clazz, result, newDegreeModulesPath,
				executionYear);
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
	    if (curricularRule.getCurricularRuleType().equals(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT)) {
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
		CurricularCourse childCurricularCourse = (CurricularCourse) context
			.getChildDegreeModule();
		if (childCurricularCourse.isEquivalent(curricularCourse)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public Collection<Context> getContextsWithCurricularCourseByCurricularPeriod(
	    final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	
	final Collection<Context> result = new HashSet<Context>();
	
	for (final Context context : this.getChildContextsSet()) {
	    
	    if (context.getChildDegreeModule().isLeaf() 
		    && context.hasCurricularPeriod()
		    && context.getCurricularPeriod().equals(curricularPeriod)
		    && context.isValid(executionPeriod)) {
		
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
    
    @Override
    public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod) {
	final CreditsLimit creditsLimit = getCreditsLimitRule(executionPeriod);
	if (creditsLimit != null) {
	    return creditsLimit.getMaximumCredits();
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
	
	final Set<Double> ectsCredits = new TreeSet<Double>(new ReverseComparator());
	for (final DegreeModule degreeModule : modulesByExecutionPeriod) {
	    ectsCredits.add(degreeModule.getMaxEctsCredits(executionPeriod));
	}
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
	final CreditsLimit creditsLimit = getCreditsLimitRule(executionPeriod);
	if (creditsLimit != null) {
	    return creditsLimit.getMinimumCredits();
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
	
	final Set<Double> ectsCredits = new TreeSet<Double>();
	for (final DegreeModule degreeModule : modulesByExecutionPeriod) {
	    ectsCredits.add(degreeModule.getMinEctsCredits(executionPeriod));
	}
	return sumEctsCredits(ectsCredits, minimumLimit.intValue());
    }
    
    private Double sumEctsCredits(final Set<Double> ectsCredits, int limit) {
	double result = 0d;
	final Iterator<Double> ectsCreditsIter = ectsCredits.iterator();
	for (; ectsCreditsIter.hasNext() && limit > 0; limit--) {
	    result += ectsCreditsIter.next().doubleValue();
	}
	return Double.valueOf(result);
    }
    
    private CreditsLimit getCreditsLimitRule(final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = getCurricularRules(CurricularRuleType.CREDITS_LIMIT, executionPeriod);
	return result.isEmpty() ? null : (CreditsLimit) result.get(0); // must have only one 
    }

    private DegreeModulesSelectionLimit getDegreeModulesSelectionLimitRule(final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = getCurricularRules(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT, executionPeriod);
	return result.isEmpty() ? null : (DegreeModulesSelectionLimit) result.get(0); // must have only one 
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
    
    private boolean hasCourseGroupType(final DegreeType courseGroupType) {
	return getCourseGroupType() == courseGroupType;
    }
    
    public boolean isFirstCycle() {
	return hasCourseGroupType(DegreeType.BOLONHA_DEGREE);
    }
    
    public boolean isSecondCycle() {
	return hasCourseGroupType(DegreeType.BOLONHA_MASTER_DEGREE);
    }
    
    public CourseGroup getFirstCycleCourseGroup() {
	return getCourseGroup(DegreeType.BOLONHA_DEGREE);
    }
    
    public CourseGroup getSecondCycleCourseGroup() {
	return getCourseGroup(DegreeType.BOLONHA_MASTER_DEGREE);
    }
    
    private CourseGroup getCourseGroup(final DegreeType courseGroupType) {
	if (hasCourseGroupType(courseGroupType)) {
	    return this;
	}
	for (final Context context : getChildContexts(CourseGroup.class)) {
	    final CourseGroup courseGroup = (CourseGroup) context.getChildDegreeModule();
	    final CourseGroup search = courseGroup.getCourseGroup(courseGroupType);
	    if (search != null) {
		return search;
	    }
	}
	return null;
    }
}
