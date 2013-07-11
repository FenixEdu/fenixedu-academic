package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;

public class BranchCourseGroup extends BranchCourseGroup_Base {

    protected BranchCourseGroup() {
        super();
    }

    @Checked("RolePredicates.MANAGER_PREDICATE")
    public BranchCourseGroup(final String name, final String nameEn, final BranchType branchType) {
        super.init(name, nameEn);
        String[] args = {};
        if (branchType == null) {
            throw new DomainException("error.degreeStructure.BranchCourseGroup.branch.type.cannot.be.null", args);
        }
        setBranchType(branchType);
    }

    public BranchCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
            final BranchType branchType, final ExecutionSemester begin, final ExecutionSemester end) {

        String[] args = {};
        if (branchType == null) {
            throw new DomainException("error.degreeStructure.BranchCourseGroup.branch.type.cannot.be.null", args);
        }

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
    @Deprecated
    public boolean hasBranchType() {
        return getBranchType() != null;
    }

}
