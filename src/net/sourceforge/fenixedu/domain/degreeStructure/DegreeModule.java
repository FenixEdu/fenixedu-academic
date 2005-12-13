package net.sourceforge.fenixedu.domain.degreeStructure;


public abstract class DegreeModule extends DegreeModule_Base {
    
    public void delete() {       
        setNewDegreeCurricularPlan(null);
        for(;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
    }
    
}
