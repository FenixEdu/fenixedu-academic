package DataBeans.precedences;

import Dominio.precedences.IRestrictionByCurricularCourse;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionNotDoneCurricularCourse extends InfoRestrictionByCurricularCourse {

    public InfoRestrictionNotDoneCurricularCourse() {
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        super.setRestrictionKindResourceKey("label.manager.restrictionNotDoneCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(
            IRestrictionByCurricularCourse restriction) {

        InfoRestrictionNotDoneCurricularCourse infoRestriction = null;

        if (restriction != null) {
            infoRestriction = new InfoRestrictionNotDoneCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }

        return infoRestriction;
    }

}