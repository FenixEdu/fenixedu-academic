package net.sourceforge.fenixedu.domain.degreeStructure;


public abstract class DegreeModule extends DegreeModule_Base {
    
    public void delete() {

        // TODO relate degree structure to curriculum structure
//        if(!getCurriculumModules().isEmpty()) {
//            throw new DomainException("can't delete Degree Module");
//        }
        
        setNewDegreeCurricularPlan(null);
        for(;!getDegreeModuleContexts().isEmpty(); getDegreeModuleContexts().get(0).delete());
    }
    
}
