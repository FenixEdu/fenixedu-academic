/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

/**
 * @author João Mota
 * 
 */
public class InfoDegreeCurricularPlanWithDegree extends InfoDegreeCurricularPlan {

    public void copyFromDomain(DegreeCurricularPlan plan) {
        super.copyFromDomain(plan);
        if (plan != null) {
            setInfoDegree(InfoDegree.newInfoFromDomain(plan.getDegree()));
        }
    }

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan newInfoFromDomain(DegreeCurricularPlan plan) {
        InfoDegreeCurricularPlanWithDegree infoDegreeCurricularPlan = null;
        if (plan != null) {
            infoDegreeCurricularPlan = new InfoDegreeCurricularPlanWithDegree();
            infoDegreeCurricularPlan.copyFromDomain(plan);
        }
        return infoDegreeCurricularPlan;
    }

}
