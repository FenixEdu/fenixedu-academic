package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionNotDoneCurricularCourse extends InfoRestrictionByCurricularCourse {

    public InfoRestrictionNotDoneCurricularCourse() {
    }

    public void copyFromDomain(RestrictionByCurricularCourse restriction) {
	super.copyFromDomain(restriction);
	super.setRestrictionKindResourceKey("label.manager.restrictionNotDoneCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(RestrictionByCurricularCourse restriction) {

	InfoRestrictionNotDoneCurricularCourse infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionNotDoneCurricularCourse();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

}