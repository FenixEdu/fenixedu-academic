package DataBeans.precedences;

import Dominio.precedences.IRestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse extends
        InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse {

    public InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse() {
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        super
                .setRestrictionKindResourceKey("label.manager.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(
            IRestrictionByCurricularCourse restriction) {

        InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse infoRestriction = null;

        if (restriction != null) {
            infoRestriction = new InfoRestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }

        return infoRestriction;
    }

}