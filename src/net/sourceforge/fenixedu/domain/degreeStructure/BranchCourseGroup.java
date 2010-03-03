package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ExecutionSemester;

public class BranchCourseGroup extends BranchCourseGroup_Base {

    protected BranchCourseGroup() {
	super();
    }

    public BranchCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
	    final BranchType branchType, final ExecutionSemester begin, final ExecutionSemester end) {

	check(branchType, "error.degreeStructure.BranchCourseGroup.branch.type.cannot.be.null");

	init(parentCourseGroup, name, nameEn, begin, end);
	setBranchType(branchType);
    }

    @Override
    public boolean isBranchCourseGroup() {
	return true;
    }
    
    public boolean isMajor() {
	return getBranchType() == BranchType.MAJOR;
    }
    
    public boolean isMinor() {
	return getBranchType() == BranchType.MINOR;
    }
}
