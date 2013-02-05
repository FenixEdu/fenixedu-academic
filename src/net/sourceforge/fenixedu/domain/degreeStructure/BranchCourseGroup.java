package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class BranchCourseGroup extends BranchCourseGroup_Base {

    protected BranchCourseGroup() {
        super();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public BranchCourseGroup(final String name, final String nameEn, final BranchType branchType) {
        super.init(name, nameEn);
        check(branchType, "error.degreeStructure.BranchCourseGroup.branch.type.cannot.be.null");
        setBranchType(branchType);
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
