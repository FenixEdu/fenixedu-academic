package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse extends
	InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse {

    public InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse() {
    }

    public void copyFromDomain(RestrictionByCurricularCourse restriction) {
	super.copyFromDomain(restriction);
	super.setRestrictionKindResourceKey("label.manager.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(RestrictionByCurricularCourse restriction) {

	InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

}