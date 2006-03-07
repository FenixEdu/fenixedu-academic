package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;


public abstract class DegreeModule extends DegreeModule_Base {
       
    public void delete() {
        for (;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
        for (;!getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
        for (;!getParticipatingPrecedenceCurricularRules().isEmpty(); getParticipatingPrecedenceCurricularRules().get(0).delete());
        for (;!getParticipatingExclusivenessCurricularRules().isEmpty(); getParticipatingExclusivenessCurricularRules().get(0).delete());
    }
    
    public abstract Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod);
    
    public void editContext(Context context, CourseGroup courseGroup, CurricularPeriod curricularPeriod) {
        if (context.getCourseGroup() != courseGroup || context.getCurricularPeriod() != curricularPeriod) {
            checkContextsFor(courseGroup, curricularPeriod);
            context.edit(courseGroup, this, curricularPeriod);
        }
    }
    
    public void deleteContext(Context context) {        
        if (hasDegreeModuleContexts(context)) {
            context.delete();
        }
        if (!hasAnyDegreeModuleContexts()) {
            delete();
        }
    }
    
    public Set<CourseGroup> getAllParentCourseGroups() {
        Set<CourseGroup> result = new HashSet<CourseGroup>();
        collectParentCourseGroups(result, this);
        return result;
    }

    private void collectParentCourseGroups(Set<CourseGroup> result, DegreeModule module) {
        for (Context parent : module.getDegreeModuleContexts()) {
            if (!parent.getCourseGroup().isRoot()) {
                result.add(parent.getCourseGroup());
                collectParentCourseGroups(result, parent.getCourseGroup());
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

    public abstract Double getEctsCredits();
    public abstract void print(StringBuilder stringBuffer, String tabs, Context previousContext);
	public abstract Boolean getCanBeDeleted();
	public abstract boolean isLeaf();
    public abstract boolean isRoot();
    public abstract DegreeCurricularPlan getParentDegreeCurricularPlan();
    protected abstract void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod);
    protected abstract void addOwnPartipatingCurricularRules(final List<CurricularRule> result);
}
