package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionDoneCurricularCourse extends InfoRestrictionByCurricularCourse {

    public InfoRestrictionDoneCurricularCourse() {
    }

    public void copyFromDomain(RestrictionByCurricularCourse restriction) {
	super.copyFromDomain(restriction);
	super.setRestrictionKindResourceKey("label.manager.restrictionDoneCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(RestrictionByCurricularCourse restriction) {

	InfoRestrictionDoneCurricularCourse infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionDoneCurricularCourse();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

}