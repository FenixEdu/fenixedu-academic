package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionNotEnrolledInCurricularCourse extends InfoRestrictionNotDoneCurricularCourse {

    public InfoRestrictionNotEnrolledInCurricularCourse() {
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        super.setRestrictionKindResourceKey("label.manager.restrictionNotEnrolledInCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(
            IRestrictionByCurricularCourse restriction) {

        InfoRestrictionNotEnrolledInCurricularCourse infoRestriction = null;

        if (restriction != null) {
            infoRestriction = new InfoRestrictionNotEnrolledInCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }

        return infoRestriction;
    }

}