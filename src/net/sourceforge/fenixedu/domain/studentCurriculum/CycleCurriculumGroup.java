package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleCurriculumGroup extends CycleCurriculumGroup_Base {

    protected CycleCurriculumGroup() {
	super();
    }

    public CycleCurriculumGroup(RootCurriculumGroup rootCurriculumGroup,
	    CycleCourseGroup cycleCourseGroup, ExecutionPeriod executionPeriod) {
	this();
	init(rootCurriculumGroup, cycleCourseGroup, executionPeriod);
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	if (!(curriculumGroup instanceof RootCurriculumGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentCanOnlyBeRootCurriculumGroup");
	}
	super.setCurriculumGroup(curriculumGroup);
    }

    @Override
    public void setDegreeModule(DegreeModule degreeModule) {
	if (!(degreeModule instanceof CycleCourseGroup)) {
	    throw new DomainException(
		    "error.curriculumGroup.CycleParentDegreeModuleCanOnlyBeCycleCourseGroup");
	}
	super.setDegreeModule(degreeModule);
    }

}
