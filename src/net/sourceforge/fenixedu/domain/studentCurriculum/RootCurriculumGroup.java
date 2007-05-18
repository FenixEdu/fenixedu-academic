package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
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

    public RootCurriculumGroup(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    ExecutionPeriod executionPeriod) {
	this();

	if (studentCurricularPlan == null) {
	    throw new DomainException(
		    "error.studentCurriculum.curriculumGroup.studentCurricularPlan.cannot.be.null");
	}

	setParentStudentCurricularPlan(studentCurricularPlan);
	init(courseGroup, executionPeriod);
    }
    
    private void init(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
	checkParameters(courseGroup, executionPeriod);
	setDegreeModule(courseGroup);
	addChildCurriculumGroups(courseGroup, executionPeriod);
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

    @Override
    protected void addChildCurriculumGroups(CourseGroup courseGroup, ExecutionPeriod executionPeriod) {
        // TODO Auto-generated method stub
        super.addChildCurriculumGroups(courseGroup, executionPeriod);
    }
    
}
