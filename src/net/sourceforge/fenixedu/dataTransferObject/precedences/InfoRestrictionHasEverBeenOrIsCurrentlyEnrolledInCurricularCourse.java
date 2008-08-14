package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse extends
	InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse {

    public InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse() {
    }

    public void copyFromDomain(RestrictionByCurricularCourse restriction) {
	super.copyFromDomain(restriction);
	super.setRestrictionKindResourceKey("label.manager.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(RestrictionByCurricularCourse restriction) {

	InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

}