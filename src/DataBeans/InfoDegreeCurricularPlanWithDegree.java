/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IDegreeCurricularPlan;

/**
 * @author João Mota
 *  
 */
public class InfoDegreeCurricularPlanWithDegree extends
        InfoDegreeCurricularPlan {

    public void copyFromDomain(IDegreeCurricularPlan plan) {
        super.copyFromDomain(plan);
        if (plan != null) {
            setInfoDegree(InfoDegree.newInfoFromDomain(plan.getDegree()));
        }
    }

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan newInfoFromDomain(
            IDegreeCurricularPlan plan) {
        InfoDegreeCurricularPlanWithDegree infoDegreeCurricularPlan = null;
        if (plan != null) {
            infoDegreeCurricularPlan = new InfoDegreeCurricularPlanWithDegree();
            infoDegreeCurricularPlan.copyFromDomain(plan);
        }
        return infoDegreeCurricularPlan;
    }
}