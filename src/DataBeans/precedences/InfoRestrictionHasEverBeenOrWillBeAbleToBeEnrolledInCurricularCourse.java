package DataBeans.precedences;

import Dominio.precedences.IRestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse extends
        InfoRestrictionDoneOrHasEverBeenEnrolledInCurricularCourse {

    public InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse() {
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        super.setRestrictionKindResourceKey("label.manager.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(IRestrictionByCurricularCourse restriction) {

        InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse infoRestriction = null;
        
        if (restriction != null) {
            infoRestriction = new InfoRestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }
        
        return infoRestriction;
    }

}