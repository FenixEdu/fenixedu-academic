/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.DegreeCurricularPlan;
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
    
    
    public void copyToDomain(InfoDegreeCurricularPlan infoDegreeCurricularPlan, IDegreeCurricularPlan degreeCurricularPlan) {
        super.copyToDomain(infoDegreeCurricularPlan, degreeCurricularPlan);
        degreeCurricularPlan.setDegree(InfoDegree.newDomainFromInfo(infoDegreeCurricularPlan.getInfoDegree()));
        
    }
    
    public static IDegreeCurricularPlan newDomainFromInfo(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        IDegreeCurricularPlan degreeCurricularPlan = null;
        if(infoDegreeCurricularPlan != null) {
            degreeCurricularPlan = new DegreeCurricularPlan();
            InfoDegreeCurricularPlanWithDegree infoDegreeCurricularPlanWithDegree = new InfoDegreeCurricularPlanWithDegree();
            infoDegreeCurricularPlanWithDegree.copyToDomain(infoDegreeCurricularPlan, degreeCurricularPlan);
        }        
        return degreeCurricularPlan;
    }
}