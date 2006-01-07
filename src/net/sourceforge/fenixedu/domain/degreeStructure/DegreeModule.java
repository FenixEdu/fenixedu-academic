package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;


public abstract class DegreeModule extends DegreeModule_Base {
       
    public void delete() {
        removeNewDegreeCurricularPlan();    
        for (;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
    }
    
    public Context addContext(CourseGroup courseGroup, CurricularSemester curricularSemester,
            ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
        checkContextsFor(courseGroup, curricularSemester);
        return new Context(courseGroup, this, curricularSemester, beginExecutionPeriod, endExecutionPeriod);
    }
    
    public void editContext(Context context, CourseGroup courseGroup, CurricularSemester curricularSemester) {
        if (context.getCourseGroup() != courseGroup || context.getCurricularSemester() != curricularSemester) {
            checkContextsFor(courseGroup, curricularSemester);
            context.edit(courseGroup, this, curricularSemester);
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
    
    protected abstract void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularSemester curricularSemester);
    
    public boolean isRoot() {
        return (getNewDegreeCurricularPlan() != null);
    }
    
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
        return searchParentDegreeCurricularPlan(getFirstParent());
    }
    
    private DegreeCurricularPlan searchParentDegreeCurricularPlan(DegreeModule degreeModule) {        
        if (degreeModule.isRoot()) {
            return degreeModule.getNewDegreeCurricularPlan();
        } else {
            return degreeModule.getParentDegreeCurricularPlan();
        }
    }    
    
    private DegreeModule getFirstParent() {
        return (hasAnyDegreeModuleContexts()) ? getDegreeModuleContexts().get(0).getCourseGroup() : null;
    }

    public abstract Double computeEctsCredits();

    public abstract void print(StringBuffer stringBuffer, String tabs, Context previousContext);

	public abstract Boolean getCanBeDeleted();

	public abstract boolean isLeaf();

}
