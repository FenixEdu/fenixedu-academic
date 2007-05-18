package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CycleCurriculumGroup extends CycleCurriculumGroup_Base {

    public CycleCurriculumGroup() {
	super();
    }

    @Override
    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
	if (!(curriculumGroup instanceof RootCurriculumGroup)) {
	    throw new DomainException("error.curriculumGroup.CycleParentCanOnlyBeRootCurriculumGroup");
	}
	super.setCurriculumGroup(curriculumGroup);
    }

}
