/*
 * Created on 8/Jul/2004
 *
 */
package DataBeans;

import Dominio.IGratuitySituation;


/**
 * @author Tânia Pousão
 *
 */
public class InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree extends InfoGratuitySituation {
    
    /* (non-Javadoc)
     * @see DataBeans.InfoGratuitySituation#copyFromDomain(Dominio.IGratuitySituation)
     */
    public void copyFromDomain(IGratuitySituation gratuitySituation) {
        super.copyFromDomain(gratuitySituation);
        if(gratuitySituation != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudent.newInfoFromDomain(gratuitySituation.getStudentCurricularPlan()));
            setInfoGratuityValues(InfoGratuityValuesWithInfoExecutionDegree.newInfoFromDomain(gratuitySituation.getGratuityValues()));
        }
    }
    
    public static InfoGratuitySituation newInfoFromDomain(IGratuitySituation gratuitySituation) {
        InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree infoGratuitySituation = null;
        if(gratuitySituation != null) {
            infoGratuitySituation = new InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree();
            infoGratuitySituation.copyFromDomain(gratuitySituation);
        }
        return infoGratuitySituation;
    }
}
