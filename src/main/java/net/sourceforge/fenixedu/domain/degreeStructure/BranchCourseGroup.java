package net.sourceforge.fenixedu.domain.degreeStructure;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.predicates.RolePredicates;

public class BranchCourseGroup extends BranchCourseGroup_Base {

    protected BranchCourseGroup() {
        super();
    }

    public BranchCourseGroup(final String name, final String nameEn, final BranchType branchType) {
        check(this, RolePredicates.MANAGER_PREDICATE);
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
