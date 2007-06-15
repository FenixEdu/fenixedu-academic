package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CourseGroupEquivalencePlanEntry extends CourseGroupEquivalencePlanEntry_Base {

    protected CourseGroupEquivalencePlanEntry() {
	super();
    }

    public CourseGroupEquivalencePlanEntry(final EquivalencePlan equivalencePlan,
	    final CourseGroup oldCourseGroup, final CourseGroup newCourseGroup) {

	init(equivalencePlan, oldCourseGroup, newCourseGroup);

    }

    private void init(EquivalencePlan equivalencePlan, CourseGroup oldCourseGroup,
	    CourseGroup newCourseGroup) {

	super.init(equivalencePlan);

	checkParameters(oldCourseGroup, newCourseGroup);

	super.setOldCourseGroup(oldCourseGroup);
	super.setNewCourseGroup(newCourseGroup);

    }

    private void checkParameters(CourseGroup oldCourseGroup, CourseGroup newCourseGroup) {
	if (oldCourseGroup == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry.oldCourseGroup.cannot.be.null");
	}

	if (newCourseGroup == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.enclosing_type.newCourseGroup.cannot.be.null");
	}
    }

    @Override
    public boolean isCourseGroupEntry() {
        return true;
    }
}
