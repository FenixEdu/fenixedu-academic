/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GratuitySituation;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoGratuitySituationWithAll extends InfoGratuitySituation {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation#copyFromDomain(Dominio.GratuitySituation)
     */
    public void copyFromDomain(GratuitySituation gratuitySituation) {
        super.copyFromDomain(gratuitySituation);
        if (gratuitySituation != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(gratuitySituation
                    .getStudentCurricularPlan()));
            setInfoGratuityValues(InfoGratuityValues.newInfoFromDomain(gratuitySituation
                    .getGratuityValues()));
        }
    }

    public static InfoGratuitySituation newInfoFromDomain(GratuitySituation gratuitySituation) {
        InfoGratuitySituationWithAll infoGratuitySituation = new InfoGratuitySituationWithAll();
        if (gratuitySituation != null) {
            infoGratuitySituation = new InfoGratuitySituationWithAll();
            infoGratuitySituation.copyFromDomain(gratuitySituation);
        }
        return infoGratuitySituation;
    }
}