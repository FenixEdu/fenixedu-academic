package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;


public abstract class DegreeModule extends DegreeModule_Base {
       
    public void delete() {
        removeNewDegreeCurricularPlan();    
        for (;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
        for (;!getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
    }
    
    public abstract Context addContext(CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod);
    
    protected abstract void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod);
    
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
    
    public boolean isRoot() {
        return (getNewDegreeCurricularPlan() != null);
    }
    
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        if (isRoot()) {
            return getNewDegreeCurricularPlan();
        }
        return getDegreeModuleContexts().get(0).getCourseGroup().getParentDegreeCurricularPlan();
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

    public abstract Double getEctsCredits();

    public abstract void print(StringBuilder stringBuffer, String tabs, Context previousContext);

	public abstract Boolean getCanBeDeleted();

	public abstract boolean isLeaf();

}
