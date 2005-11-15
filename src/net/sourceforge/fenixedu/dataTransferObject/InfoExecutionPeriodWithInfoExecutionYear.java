/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * @author João Mota
 * 
 */
public class InfoExecutionPeriodWithInfoExecutionYear extends InfoExecutionPeriod {

    public void copyFromDomain(IExecutionPeriod executionPeriod) {
        super.copyFromDomain(executionPeriod);
        if (executionPeriod != null) {
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(executionPeriod.getExecutionYear()));
        }
    }

    /**
     * @param period
     * @return
     */
    public static InfoExecutionPeriodWithInfoExecutionYear newInfoFromDomain(IExecutionPeriod period) {
        InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = null;

        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriodWithInfoExecutionYear();
            infoExecutionPeriod.copyFromDomain(period);
            infoExecutionPeriod.setQualifiedName(period.getName() + " " + period.getExecutionYear().getYear());
        }
        return infoExecutionPeriod;
    }

}
