package net.sourceforge.fenixedu.domain.degreeStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.beanutils.BeanComparator;


public abstract class DegreeModule extends DegreeModule_Base {

    static final public Comparator<DegreeModule> COMPARATOR_BY_NAME = new DegreeModuleComparatorByName();
    static private class DegreeModuleComparatorByName implements Comparator<DegreeModule> {
        public int compare(DegreeModule d1, DegreeModule d2) {
            return new BeanComparator("name", Collator.getInstance()).compare(d1, d2);
        }
    }
    
    public DegreeModule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    /**
     * We need a method to return a full name of a course group - from it's parent course group to
     * the degree curricular plan's root course group.
     * 
     * Given this is impossible, for there are many routes from the root course group to one
     * particular course group, we choose (for now...) to get one possible full name, 
     * always visiting the first element of every list of contexts on our way to the root course group.  
     * 
     * @return A string with one possible full name of this course group
     */
    public String getOneFullName() {
	boolean pt = !(LanguageUtils.getUserLanguage() == Language.en);
	
        if (isRoot()) {
            return ((pt) ? getName() : getNameEn());
        } else {
            final CourseGroup parentCourseGroup = getParentContexts().get(0).getParentCourseGroup();
            return parentCourseGroup.getOneFullName() + " > " + (((pt) ? getName() : getNameEn()));
        }
    }
    
    public void delete() {
	if(getCanBeDeleted()) {
	    for (;!getParentContexts().isEmpty(); getParentContexts().get(0).delete());
	    for (;!getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
	    for (;!getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0).delete());
	    for (;!getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(0).delete());
	} else {
	    throw new DomainException("courseGroup.notEmptyCurriculumModules");
	}
    }
    
    protected Boolean getCanBeDeleted() {
	return !hasAnyCurriculumModules();
    }
    
    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        
        if (this.isRoot()) {
            throw new DomainException("degreeModule.cannot.add.context.to.root");
        }
        
        checkContextsFor(parentCourseGroup, curricularPeriod, null);
        checkOwnRestrictions(parentCourseGroup, curricularPeriod);
        return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    public void editContext(Context context, CourseGroup parentCourseGroup,
            CurricularPeriod curricularPeriod, ExecutionPeriod beginExecutionPeriod,
            ExecutionPeriod endExecutionPeriod) {
        
        checkContextsFor(parentCourseGroup, curricularPeriod, context);
        checkOwnRestrictions(parentCourseGroup, curricularPeriod);
        context.edit(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod,
                endExecutionPeriod);
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
    
    public boolean hasAnyParentContexts(final ExecutionPeriod executionPeriod) {
	for (final Context context : getParentContextsSet()) {
	    if (executionPeriod == null || context.isValid(executionPeriod)) {
		return true;
	    }
	}
	
	return false;
    }
    
    public boolean isOptional() {
	return false;
    }

    public Degree getDegree() {
	return getParentDegreeCurricularPlan().getDegree();
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
    
    public List<ICurricularRule> getCurricularRules(final CurricularRuleType ruleType, final ExecutionPeriod executionPeriod) {
	final List<ICurricularRule> result = new ArrayList<ICurricularRule>();
	for (final ICurricularRule curricularRule : getCurricularRules(executionPeriod)) {
	    if (curricularRule.getCurricularRuleType() == ruleType) {
		result.add(curricularRule);
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
    
    abstract public DegreeCurricularPlan getParentDegreeCurricularPlan();
    abstract public void print(StringBuilder stringBuffer, String tabs, Context previousContext);
    abstract public boolean isLeaf();
    abstract public boolean isRoot();

    abstract public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod);
    abstract public Double getMinEctsCredits(final ExecutionPeriod executionPeriod);
    abstract protected Double countAllMaxEctsCredits(final ExecutionPeriod executionPeriod);
    abstract protected Double countAllMinEctsCredits(final ExecutionPeriod executionPeriod);
    
    abstract protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod, final Context context);
    abstract protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result);
    abstract protected void checkOwnRestrictions(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod);
}
