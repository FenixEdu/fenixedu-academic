/*
 * Created on 29/Fev/2004
 */
package Dominio.credits;

import Dominio.IExecutionPeriod;

/**
 * @author jpvl
 */
public interface IOtherTypeCreditLine extends ICreditLine
{
    /**
     * @return Returns the executionPeriod.
     */
    public abstract IExecutionPeriod getExecutionPeriod();
    /**
     * @param executionPeriod The executionPeriod to set.
     */
    public abstract void setExecutionPeriod(IExecutionPeriod executionPeriod);
    /**
     * @return Returns the reason.
     */
    public abstract String getReason();
    /**
     * @param reason The reason to set.
     */
    public abstract void setReason(String reason);
}