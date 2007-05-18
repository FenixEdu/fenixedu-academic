package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CycleCourseGroup extends CycleCourseGroup_Base {
    
    protected  CycleCourseGroup() {
        super();
    }
    
    public CycleCourseGroup(final CourseGroup parentCourseGroup, final String name, final String nameEn,
	    final CycleType cycleType, final ExecutionPeriod begin, final ExecutionPeriod end) {
	if(cycleType == null) {
	    throw new DomainException(
	    "error.degreeStructure.CycleCourseGroup.cycle.type.cannot.be.null");
	}
	init(parentCourseGroup, name, nameEn, begin, end);
	setCycleType(cycleType);
    }

    
    @Override
    public void delete() {
	getSourceAffinitiesSet().clear();
	getDestinationAffinitiesSet().clear();
        super.delete();
    }
    
    @Override
    public boolean isCycleCourseGroup() {
        return true;
    }
}
