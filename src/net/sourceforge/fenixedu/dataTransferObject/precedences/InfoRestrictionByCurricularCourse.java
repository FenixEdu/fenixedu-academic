package net.sourceforge.fenixedu.dataTransferObject.precedences;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.precedences.IRestrictionByCurricularCourse;

/**
 * @author David Santos in Jun 9, 2004
 */

public class InfoRestrictionByCurricularCourse extends InfoRestriction {

    protected InfoCurricularCourse precedentInfoCurricularCourse;

    public InfoRestrictionByCurricularCourse() {
    }

    public InfoCurricularCourse getPrecedentInfoCurricularCourse() {
        return precedentInfoCurricularCourse;
    }

    public void setPrecedentInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.precedentInfoCurricularCourse = infoCurricularCourse;
    }

    public void copyFromDomain(IRestrictionByCurricularCourse restriction) {
        super.copyFromDomain(restriction);
        this.setPrecedentInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(restriction
                .getPrecedentCurricularCourse()));
    }

    public static InfoRestrictionByCurricularCourse newInfoFromDomain(
            IRestrictionByCurricularCourse restriction) {

        InfoRestrictionByCurricularCourse infoRestriction = null;

        if (restriction != null) {
            infoRestriction = new InfoRestrictionByCurricularCourse();
            infoRestriction.copyFromDomain(restriction);
        }

        return infoRestriction;
    }

    public String getArg() {
        return precedentInfoCurricularCourse.getName();
    }
}