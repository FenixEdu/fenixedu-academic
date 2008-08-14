package net.sourceforge.fenixedu.dataTransferObject.precedences;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoPrecedenceWithRestrictions extends InfoPrecedence {

    protected List infoRestrictions;

    public List getInfoRestrictions() {
	return infoRestrictions;
    }

    public void setInfoRestrictions(List infoRestrictions) {
	this.infoRestrictions = infoRestrictions;
    }

    public void copyFromDomain(Precedence precedence) {
	super.copyFromDomain(precedence);
	this.setInfoRestrictions(getInfoRestrictionsList(precedence.getRestrictions()));
    }

    public static InfoPrecedence newInfoFromDomain(Precedence precedence) {
	InfoPrecedenceWithRestrictions infoPrecedenceWithRestrictions = null;

	if (precedence != null) {
	    infoPrecedenceWithRestrictions = new InfoPrecedenceWithRestrictions();
	    infoPrecedenceWithRestrictions.copyFromDomain(precedence);
	}

	return infoPrecedenceWithRestrictions;
    }

    private List getInfoRestrictionsList(List<Restriction> restrictions) {
	List<InfoRestriction> infoRestrictions = new ArrayList();

	for (Restriction restriction : restrictions) {
	    if (restriction instanceof RestrictionByNumberOfDoneCurricularCourses) {
		InfoRestrictionByNumberOfCurricularCourses infoRestriction = InfoRestrictionByNumberOfDoneCurricularCourses
			.newInfoFromDomain((RestrictionByNumberOfCurricularCourses) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionNotEnrolledInCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotEnrolledInCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionDoneCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionNotDoneCurricularCourse) {
		InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotDoneCurricularCourse
			.newInfoFromDomain((RestrictionByCurricularCourse) restriction);
		infoRestrictions.add(infoRestriction);
	    } else if (restriction instanceof RestrictionPeriodToApply) {
		InfoRestrictionPeriodToApply infoRestriction = InfoRestrictionPeriodToApply
			.newInfoFromDomain((RestrictionPeriodToApply) restriction);
		infoRestrictions.add(infoRestriction);
	    }
	}

	return infoRestrictions;
    }

}
