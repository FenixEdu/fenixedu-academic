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

    /**
     * @param period
     * @return
     */
    public static InfoExecutionPeriod copyFromDomain(IExecutionPeriod period) {
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod
                .copyFromDomain(period);
        if (infoExecutionPeriod != null) {
            infoExecutionPeriod.setInfoExecutionYear(InfoExecutionYear
                    .copyFromDomain(period.getExecutionYear()));
        }
        return infoExecutionPeriod;
    }

}