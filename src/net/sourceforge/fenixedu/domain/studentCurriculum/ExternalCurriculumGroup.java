package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;


/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ExternalCurriculumGroup extends ExternalCurriculumGroup_Base {

    protected ExternalCurriculumGroup() {
	super();
    }
    
    public ExternalCurriculumGroup(final RootCurriculumGroup rootCurriculumGroup,
	    final CycleCourseGroup cycleCourseGroup, final ExecutionPeriod executionPeriod) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup, executionPeriod);
    }
    
    @Override
    protected void checkInitConstraints(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup) {
	super.checkInitConstraints(studentCurricularPlan, courseGroup);
	if (studentCurricularPlan.getDegreeCurricularPlan() == courseGroup.getParentDegreeCurricularPlan()) {
	    throw new DomainException("error.studentCurriculum.CurriculumGroup.courseGroup.must.have.different.degreeCurricularPlan");
	}
    }

}
