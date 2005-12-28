package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;


public abstract class DegreeModule extends DegreeModule_Base {
       
    public void delete() {
        removeNewDegreeCurricularPlan();    
        for (;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
    }
    
    public IContext addContext(ICourseGroup courseGroup, ICurricularSemester curricularSemester,
            IExecutionPeriod beginExecutionPeriod, IExecutionPeriod endExecutionPeriod) {
        checkContextsFor(courseGroup, curricularSemester);
        return new Context(courseGroup, this, curricularSemester, beginExecutionPeriod, endExecutionPeriod);
    }
    
    public void editContext(IContext context, ICourseGroup courseGroup, ICurricularSemester curricularSemester) {
        if (context.getCourseGroup() != courseGroup || context.getCurricularSemester() != curricularSemester) {
            checkContextsFor(courseGroup, curricularSemester);
            context.edit(courseGroup, this, curricularSemester);
        }
    }
    
    public void deleteContext(IContext context) {        
        if (hasDegreeModuleContexts(context)) {
            context.delete();
        }
        if (!hasAnyDegreeModuleContexts()) {
            delete();
        }
    }
    
    protected abstract void checkContextsFor(final ICourseGroup parentCourseGroup, final ICurricularSemester curricularSemester);
    
    public boolean isRoot() {
        return (getNewDegreeCurricularPlan() != null);
    }
}
