package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public abstract class DegreeModule extends DegreeModule_Base {
    
    public Boolean getCanBeDeleted() {
        return !hasAnyDegreeModuleContexts(); 
    }
    
    public void delete() {
        if (getCanBeDeleted()) {
            removeNewDegreeCurricularPlan();    
        } else {
            throw new DomainException("error.notEmptyCurricularCourseContexts");    
        }
    }
    
    public void deleteContext(IContext context) {        
        if (hasDegreeModuleContexts(context)) {
            context.delete();
        }
        if (!hasAnyDegreeModuleContexts()) {
            this.delete();
        }
    }    
}
