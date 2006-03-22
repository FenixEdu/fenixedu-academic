package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;


public abstract class DegreeModule extends DegreeModule_Base {

    public DegreeModule() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
        for (;!getParentContexts().isEmpty(); getParentContexts().get(0).delete());
        for (;!getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
        for (;!getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0).delete());
        for (;!getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(0).delete());
    }
    
    public Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        
        checkContextsFor(parentCourseGroup, curricularPeriod);
        checkOwnRestrictions(parentCourseGroup, curricularPeriod);
        return new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }
    
    public void editContext(Context context, CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod) {
        if (context.getParentCourseGroup() != parentCourseGroup || context.getCurricularPeriod() != curricularPeriod) {
            checkContextsFor(parentCourseGroup, curricularPeriod);
            checkOwnRestrictions(parentCourseGroup, curricularPeriod);
            context.edit(parentCourseGroup, this, curricularPeriod);
        }
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

    public List<CurricularRule> getCurricularRules(ExecutionYear executionYear) {
        List<CurricularRule> result = new ArrayList<CurricularRule>();
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (executionYear == null || curricularRule.isValid(executionYear)) {
                result.add(curricularRule);
            }
        }
        
        return result;
    }
    
    public abstract Double getEctsCredits();
    public abstract void print(StringBuilder stringBuffer, String tabs, Context previousContext);
	public abstract Boolean getCanBeDeleted();
	public abstract boolean isLeaf();
    public abstract boolean isRoot();
    public abstract DegreeCurricularPlan getParentDegreeCurricularPlan();
    protected abstract void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod);
    protected abstract void addOwnPartipatingCurricularRules(final List<CurricularRule> result);
    protected abstract void checkOwnRestrictions(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod);
}
