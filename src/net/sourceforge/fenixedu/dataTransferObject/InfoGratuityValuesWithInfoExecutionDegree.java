/*
 * Created on 8/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.GratuityValues;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoGratuityValuesWithInfoExecutionDegree extends InfoGratuityValues {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoGratuityValues#copyFromDomain(Dominio.GratuityValues)
     */
    public void copyFromDomain(GratuityValues gratuityValues) {
        super.copyFromDomain(gratuityValues);
        if (gratuityValues != null) {
            setInfoExecutionDegree(InfoExecutionDegree
                    .newInfoFromDomain(gratuityValues.getExecutionDegree()));
        }
    }

    public static InfoGratuityValues newInfoFromDomain(GratuityValues gratuityValues) {
        InfoGratuityValuesWithInfoExecutionDegree infoGratuityValues = null;
        if (gratuityValues != null) {
            infoGratuityValues = new InfoGratuityValuesWithInfoExecutionDegree();
            infoGratuityValues.copyFromDomain(gratuityValues);
        }
        return infoGratuityValues;
    }
}