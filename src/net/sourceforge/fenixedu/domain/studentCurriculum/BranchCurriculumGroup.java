package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class BranchCurriculumGroup extends BranchCurriculumGroup_Base {
    
    protected  BranchCurriculumGroup() {
        super();
    }
    
    public BranchCurriculumGroup(CurriculumGroup parentNode, BranchCourseGroup branch) {
	this();
	init(parentNode, branch);
    }
    
    @Override
    public boolean isBranchCurriculumGroup() {
	return true;
    }
    
    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (degreeModule != null && !(degreeModule instanceof BranchCourseGroup)) {
	    throw new DomainException("error.curriculumGroup.BranchParentDegreeModuleCanOnlyBeBranchCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }
    
    @Override
    public BranchCourseGroup getDegreeModule() {
	return (BranchCourseGroup) super.getDegreeModule();
    }
    
    @Override
    public BranchCurriculumGroup getBranchCurriculumGroup(BranchType branchType) {
	return this.getDegreeModule().getBranchType() == branchType ? this : null;
    }
    
    @Override
    public BranchCourseGroup getBranchCourseGroup(BranchType branchType) {
	return this.getDegreeModule().getBranchType() == branchType ? this.getDegreeModule() : null;
    }
    
}
