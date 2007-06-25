package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class RootCurriculumGroup extends RootCurriculumGroup_Base {

    public RootCurriculumGroup() {
	super();
    }

    public RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan,
	    RootCourseGroup rootCourseGroup, ExecutionPeriod executionPeriod, CycleType cycleType) {
	this();

	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}

	setParentStudentCurricularPlan(studentCurricularPlan);
	init(rootCourseGroup, executionPeriod, cycleType);
    }

    private void init(RootCourseGroup courseGroup, ExecutionPeriod executionPeriod, CycleType cycleType) {
	checkParameters(courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod, cycleType);
    }

    public void setRootCourseGroup(final RootCourseGroup rootCourseGroup) {
	setDegreeModule(rootCourseGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (!(degreeModule instanceof RootCourseGroup)) {
	    throw new DomainException(
		    "error.curriculumGroup.RootCurriculumGroup.degreeModuleMustBeRootCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	throw new DomainException("error.curriculumGroup.RootCurriculumGroupCannotHaveParent");
    }

    @Override
    public boolean isRoot() {
	return true;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
	return getParentStudentCurricularPlan();
    }

    private void addChildCurriculumGroups(RootCourseGroup rootCourseGroup,
	    ExecutionPeriod executionPeriod, CycleType cycle) {

	if (rootCourseGroup.hasCycleGroups()) {
	    if (cycle == null) {
		cycle = rootCourseGroup.getDegree().getDegreeType().getFirstCycleType();
	    }

	    if (cycle != null) {
		new CycleCurriculumGroup(this, rootCourseGroup.getCycleCourseGroup(cycle),
			executionPeriod);
	    }

	} else {
	    super.addChildCurriculumGroups(rootCourseGroup, executionPeriod);
	}

    }

}
