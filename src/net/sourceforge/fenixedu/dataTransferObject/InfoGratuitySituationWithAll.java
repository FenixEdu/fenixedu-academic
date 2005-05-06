/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGratuitySituation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoGratuitySituationWithAll extends InfoGratuitySituation {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation#copyFromDomain(Dominio.IGratuitySituation)
     */
    public void copyFromDomain(IGratuitySituation gratuitySituation) {
        super.copyFromDomain(gratuitySituation);
        if (gratuitySituation != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree.newInfoFromDomain(gratuitySituation
                    .getStudentCurricularPlan()));
            setInfoGratuityValues(InfoGratuityValues.newInfoFromDomain(gratuitySituation
                    .getGratuityValues()));
        }
    }

    public static InfoGratuitySituation newInfoFromDomain(IGratuitySituation gratuitySituation) {
        InfoGratuitySituationWithAll infoGratuitySituation = new InfoGratuitySituationWithAll();
        if (gratuitySituation != null) {
            infoGratuitySituation = new InfoGratuitySituationWithAll();
            infoGratuitySituation.copyFromDomain(gratuitySituation);
        }
        return infoGratuitySituation;
    }
}