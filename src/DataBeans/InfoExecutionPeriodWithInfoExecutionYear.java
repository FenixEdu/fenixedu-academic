/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IExecutionPeriod;

/**
 * @author João Mota
 *  
 */
public class InfoExecutionPeriodWithInfoExecutionYear extends
        InfoExecutionPeriod {

    public void copyFromDomain(IExecutionPeriod executionPeriod) {
        super.copyFromDomain(executionPeriod);
        if (executionPeriod != null) {
            setInfoExecutionYear(InfoExecutionYear
                    .newInfoFromDomain(executionPeriod.getExecutionYear()));
        }
    }

    /**
     * @param period
     * @return
     */
    public static InfoExecutionPeriod newInfoFromDomain(IExecutionPeriod period) {
        InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = null;

        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriodWithInfoExecutionYear();
            infoExecutionPeriod.copyFromDomain(period);
        }
        return infoExecutionPeriod;
    }

}