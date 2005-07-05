package net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public enum DegreeCurricularPlanState {

    ACTIVE,

    NOT_ACTIVE,

    CONCLUDED,

    //// For use in migration process only
    PAST;

    public String getName() {
        return name();
    }

}
