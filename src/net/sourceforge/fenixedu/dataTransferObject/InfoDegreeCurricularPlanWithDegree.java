/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;

/**
 * @author João Mota
 * 
 */
public class InfoDegreeCurricularPlanWithDegree extends InfoDegreeCurricularPlan {

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
    public static InfoDegreeCurricularPlan newInfoFromDomain(IDegreeCurricularPlan plan) {
        InfoDegreeCurricularPlanWithDegree infoDegreeCurricularPlan = null;
        if (plan != null) {
            infoDegreeCurricularPlan = new InfoDegreeCurricularPlanWithDegree();
            infoDegreeCurricularPlan.copyFromDomain(plan);
        }
        return infoDegreeCurricularPlan;
    }

    public void copyToDomain(InfoDegreeCurricularPlan infoDegreeCurricularPlan,
            IDegreeCurricularPlan degreeCurricularPlan) {
        super.copyToDomain(infoDegreeCurricularPlan, degreeCurricularPlan);
        degreeCurricularPlan.setDegree(InfoDegree.newDomainFromInfo(infoDegreeCurricularPlan
                .getInfoDegree()));

    }

}
