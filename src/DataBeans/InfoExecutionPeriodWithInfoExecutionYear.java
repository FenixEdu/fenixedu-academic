/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;

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
    public static InfoExecutionPeriod newInfoFromDomain(IExecutionPeriod period) {
        InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = null;

        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriodWithInfoExecutionYear();
            infoExecutionPeriod.copyFromDomain(period);
        }
        return infoExecutionPeriod;
    }

    public static IExecutionPeriod newDomainFromInfo(InfoExecutionPeriod infoExecutionPeriod) {
        IExecutionPeriod executionPeriod = null;
        if (infoExecutionPeriod != null) {
            executionPeriod = new ExecutionPeriod();
            infoExecutionPeriod.copyToDomain(infoExecutionPeriod, executionPeriod);

            InfoExecutionYear executionYear = infoExecutionPeriod.getInfoExecutionYear();
            IExecutionYear executionYear2 = InfoExecutionYear.newDomainFromInfo(executionYear);
            executionPeriod.setExecutionYear(executionYear2);
        }
        return executionPeriod;
    }

}