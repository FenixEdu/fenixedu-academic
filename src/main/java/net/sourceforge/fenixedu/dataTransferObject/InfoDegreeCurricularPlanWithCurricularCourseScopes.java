/*
 * Created on Oct 13, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoDegreeCurricularPlanWithCurricularCourseScopes extends DataTranferObject {
    InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    List scopes;

    /**
     * @return Returns the infoDegreeCurricularPlan.
     */
    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    /**
     * @param infoDegreeCurricularPlan
     *            The infoDegreeCurricularPlan to set.
     */
    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    /**
     * @return Returns the scopes.
     */
    public List getScopes() {
        return scopes;
    }

    /**
     * @param scopes
     *            The scopes to set.
     */
    public void setScopes(List scopes) {
        this.scopes = scopes;
    }
}