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

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan copyFromDomain(
            IDegreeCurricularPlan plan) {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                .copyFromDomain(plan);
        if (infoDegreeCurricularPlan != null) {
            infoDegreeCurricularPlan.setInfoDegree(InfoDegree.copyFromDomain(plan
                    .getDegree()));
        }
        return infoDegreeCurricularPlan;
    }
}