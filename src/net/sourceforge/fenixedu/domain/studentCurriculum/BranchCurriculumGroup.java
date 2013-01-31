package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class BranchCurriculumGroup extends BranchCurriculumGroup_Base {

	protected BranchCurriculumGroup() {
		super();
	}

	public BranchCurriculumGroup(CurriculumGroup parentNode, BranchCourseGroup branch) {
		this();
		init(parentNode, branch);
	}

	public BranchCurriculumGroup(CurriculumGroup parentNode, BranchCourseGroup branch, ExecutionSemester executionSemester) {
		super();
		init(parentNode, branch, executionSemester);
	}

	@Override
	protected void checkInitConstraints(CurriculumGroup parent, CourseGroup courseGroup) {
		super.checkInitConstraints(parent, courseGroup);

		final BranchCourseGroup branchCourseGroup = (BranchCourseGroup) courseGroup;

		final CycleCurriculumGroup cycle = parent.getParentCycleCurriculumGroup();
		if (cycle != null && cycle.hasBranchCurriculumGroup(branchCourseGroup.getBranchType())) {
			throw new DomainException("error.BranchCurriculumGroup.parent.cycle.cannot.have.another.branch.with.same.type");
		}
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

	public BranchType getBranchType() {
		return this.getDegreeModule().getBranchType();
	}

	@Override
	public Set<BranchCurriculumGroup> getBranchCurriculumGroups() {
		return Collections.singleton(this);
	}

	@Override
	public Set<BranchCurriculumGroup> getBranchCurriculumGroups(BranchType branchType) {
		return getBranchType() == branchType ? Collections.<BranchCurriculumGroup> singleton(this) : Collections
				.<BranchCurriculumGroup> emptySet();
	}

	@Override
	public BranchCurriculumGroup getParentBranchCurriculumGroup() {
		return this;
	}

	@Override
	public boolean hasBranchCurriculumGroup(final BranchType type) {
		return getBranchType() == type;
	}

	public boolean isMajor() {
		return getDegreeModule().isMajor();
	}

	public boolean isMinor() {
		return getDegreeModule().isMinor();
	}

}
