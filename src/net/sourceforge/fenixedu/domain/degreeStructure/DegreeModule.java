package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public abstract class DegreeModule extends DegreeModule_Base {
    
    public void delete() {
        if (hasAnyDegreeModuleContexts()) {
            throw new DomainException("error.notEmptyCurricularCourseContexts");
        }
        setNewDegreeCurricularPlan(null);
    }
    
    public void deleteContext(IContext context) {
        if (getDegreeModuleContextsCount() > 1) {
            context.delete();
        } else {
            context.delete();
            this.delete();
        }
    }    
}
