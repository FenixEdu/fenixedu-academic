/*
 * Created on 29/Fev/2004
 */
package DataBeans.credits;

import DataBeans.InfoExecutionPeriod;

/**
 * @author jpvl
 */
public class InfoOtherTypeCreditLine extends InfoCreditLine
{
    private InfoExecutionPeriod infoExecutionPeriod;
    private String reason;
    
    
    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionPeriod The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason The reason to set.
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }

}
