package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;


public abstract class CurriculumModule extends CurriculumModule_Base {
    
    public CurriculumModule() {
        super();
        this.setOjbConcreteClass(this.getClass().getName());
    }
    
    public abstract boolean isLeaf();
    
    public void delete() {
    	removeDegreeModule();
    	removeCurriculumGroup();
    	super.deleteDomainObject();
    }
    
    public abstract StringBuilder print(String tabs);
    
    public CurriculumGroup getRootCurriculumGroup() {
    	if(this.getCurriculumGroup() != null) {
    		return this.getCurriculumGroup().getRootCurriculumGroup();
    	} else {
    		return (CurriculumGroup) this;
    	}
    }
    
    public StudentCurricularPlan getRootStudentCurricularPlan() {
    	return getRootCurriculumGroup().getRootStudentCurricularPlan();
    }
    
}
