package DataBeans.precedences;

import Dominio.precedences.IRestrictionByCurricularCourse;


/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionDoneCurricularCourse extends InfoRestrictionByCurricularCourse {

    public InfoRestrictionDoneCurricularCourse() {
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        super.setRestrictionKindResourceKey("label.manager.restrictionDoneCurricularCourse");
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(IRestrictionByCurricularCourse restriction) {

        InfoRestrictionDoneCurricularCourse infoRestriction = null;
        
        if (restriction != null) {
            infoRestriction = new InfoRestrictionDoneCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }
        
        return infoRestriction;
    }

}