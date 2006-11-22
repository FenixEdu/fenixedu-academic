package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.util.StringFormatter;

public class CourseGroup extends CourseGroup_Base {

    public static List<CourseGroup> readCourseGroups() {
	List<CourseGroup> result = new ArrayList<CourseGroup>();

	for (DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModules()) {
	    if (degreeModule instanceof CourseGroup) {
		result.add((CourseGroup) degreeModule);
	    }
	}

	return result;
    }

    protected CourseGroup() {
	super();
    }

    public CourseGroup(String name, String nameEn) {
	this();
	init(name, nameEn);
    }
    
    protected void init(String name, String nameEn) {
	super.setName(StringFormatter.prettyPrint(name));
	super.setNameEn(StringFormatter.prettyPrint(nameEn));
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
	return !hasAnyChildContexts();
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

    public Double getEctsCredits() {
	Double result = 0.0;

	for (Context context : this.getChildContexts()) {
	    if (context.getChildDegreeModule() != null
		    && context.getChildDegreeModule().getEctsCredits() != null) {
		result += context.getChildDegreeModule().getEctsCredits();
	    }
	}

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

}
