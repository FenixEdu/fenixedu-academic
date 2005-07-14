package net.sourceforge.fenixedu.dataTransferObject.precedences;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.IRestriction;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByNumberOfCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByNumberOfDoneCurricularCourses;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionNotDoneCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionNotEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionPeriodToApply;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

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

    private List getInfoRestrictionsList(List<IRestriction> restrictions) {
        List<InfoRestriction> infoRestrictions = new ArrayList();

        for (IRestriction restriction : restrictions) {

            if (restriction instanceof Proxy) {
                restriction = (IRestriction) ProxyHelper.getRealObject(restriction);
            }

            if (restriction instanceof IRestrictionByNumberOfDoneCurricularCourses) {
                InfoRestrictionByNumberOfCurricularCourses infoRestriction = InfoRestrictionByNumberOfDoneCurricularCourses
                        .newInfoFromDomain((IRestrictionByNumberOfCurricularCourses) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionNotEnrolledInCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotEnrolledInCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionDoneCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionDoneCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionNotDoneCurricularCourse) {
                InfoRestrictionByCurricularCourse infoRestriction = InfoRestrictionNotDoneCurricularCourse
                        .newInfoFromDomain((IRestrictionByCurricularCourse) restriction);
                infoRestrictions.add(infoRestriction);
            } else if (restriction instanceof IRestrictionPeriodToApply) {
                InfoRestrictionPeriodToApply infoRestriction = InfoRestrictionPeriodToApply
                        .newInfoFromDomain((IRestrictionPeriodToApply) restriction);
                infoRestrictions.add(infoRestriction);
            }
        }

        return infoRestrictions;
    }

}
