package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfCurricularCourses;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionByNumberOfDoneCurricularCourses extends InfoRestrictionByNumberOfCurricularCourses {

    public InfoRestrictionByNumberOfDoneCurricularCourses() {
    }

    public void copyFromDomain(RestrictionByNumberOfCurricularCourses restriction) {
	super.copyFromDomain(restriction);
	super.setRestrictionKindResourceKey("label.manager.restrictionByNumberOfDoneCurricularCourses");
    }

    public static InfoRestrictionByNumberOfCurricularCourses newInfoFromDomain(RestrictionByNumberOfCurricularCourses restriction) {

	InfoRestrictionByNumberOfDoneCurricularCourses infoRestriction = null;

	if (restriction != null) {
	    infoRestriction = new InfoRestrictionByNumberOfDoneCurricularCourses();
	    infoRestriction.copyFromDomain(restriction);
	}

	return infoRestriction;
    }

}