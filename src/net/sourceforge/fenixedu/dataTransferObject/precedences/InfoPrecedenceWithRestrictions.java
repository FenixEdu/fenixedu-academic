package net.sourceforge.fenixedu.dataTransferObject.precedences;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByNumberOfCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionPeriodToApply;
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

    public InfoPrecedenceWithRestrictions() {
    }

    public List getInfoRestrictions() {
        return infoRestrictions;
    }

    public void setInfoRestrictions(List infoRestrictions) {
        this.infoRestrictions = infoRestrictions;
    }

    public void copyFromDomain(IPrecedence precedence) {
        super.copyFromDomain(precedence);
        this.setInfoRestrictions(getInfoRestrictionsList(precedence.getRestrictions()));
    }

    public static InfoPrecedence newInfoFromDomain(IPrecedence precedence) {

        InfoPrecedenceWithRestrictions infoPrecedenceWithRestrictions = null;

        if (precedence != null) {
            infoPrecedenceWithRestrictions = new InfoPrecedenceWithRestrictions();
            infoPrecedenceWithRestrictions.copyFromDomain(precedence);
        }

        return infoPrecedenceWithRestrictions;
    }

    private List getInfoRestrictionsList(List restrictions) {

        List infoRestrictions = new ArrayList();

        int size = restrictions.size();

        for (int i = 0; i < size; i++) {
            IRestriction restriction = (IRestriction) restrictions.get(i);

            if (restriction instanceof RestrictionByNumberOfDoneCurricularCourses) {
                InfoRestrictionByNumberOfCurricularCourses infoRestriction = InfoRestrictionByNumberOfDoneCurricularCourses
                        .newInfoFromDomain((IRestrictionByNumberOfCurricularCourses) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionNotEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionDoneCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionNotDoneCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotDoneCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof RestrictionPeriodToApply) {
                InfoRestrictionPeriodToApply infoRestriction = InfoRestrictionPeriodToApply
                        .newInfoFromDomain((IRestrictionPeriodToApply) restriction);
                infoRestrictions.add(infoRestriction);
            }
        }

        return infoRestrictions;
    }

}