package DataBeans.precedences;

import Dominio.precedences.IRestrictionByNumberOfCurricularCourses;

/**
 * @author David Santos on Jul 27, 2004
 */

public class InfoRestrictionByNumberOfDoneCurricularCourses extends
        InfoRestrictionByNumberOfCurricularCourses {

    public InfoRestrictionByNumberOfDoneCurricularCourses() {
    }

    public void copyFromDomain(IRestrictionByNumberOfCurricularCourses restriction) {
        super.copyFromDomain(restriction);
        super.setRestrictionKindResourceKey("label.manager.restrictionByNumberOfDoneCurricularCourses");
    }

    public static InfoRestrictionByNumberOfCurricularCourses newInfoFromDomain(
            IRestrictionByNumberOfCurricularCourses restriction) {

        InfoRestrictionByNumberOfDoneCurricularCourses infoRestriction = null;

        if (restriction != null) {
            infoRestriction = new InfoRestrictionByNumberOfDoneCurricularCourses();
            infoRestriction.copyFromDomain(restriction);
        }

        return infoRestriction;
    }

}