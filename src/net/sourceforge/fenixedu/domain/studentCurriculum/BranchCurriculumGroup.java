package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;

public class BranchCurriculumGroup extends BranchCurriculumGroup_Base {
    
    public  BranchCurriculumGroup() {
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
    
}
