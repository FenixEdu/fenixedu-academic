/*
 * Created on 29/Fev/2004
 */
package Dominio.credits;

import Dominio.IExecutionPeriod;
import Dominio.credits.event.CreditsEvent;

/**
 * @author jpvl
 */
public class OtherTypeCreditLine extends CreditLine implements IOtherTypeCreditLine
{
    private IExecutionPeriod executionPeriod;
    private String reason;
    private Double credits;
    private Integer keyExecutionPeriod;

    public OtherTypeCreditLine()
    {
        super();
    }
    
    /**
     * @param id
     */
    public OtherTypeCreditLine(Integer idInternal)
    {
        super(idInternal);
    }

    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod()
    {
        return executionPeriod;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod)
    {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @return Returns the reason.
     */
    public String getReason()
    {
        return reason;
    }

    /**
     * @param reason
     *            The reason to set.
     */
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    /**
     * @return Returns the credits.
     */
    public Double getCredits()
    {
        return credits;
    }

    /**
     * @param credits The credits to set.
     */
    public void setCredits(Double credits)
    {
        this.credits = credits;
    }

    /**
     * @return Returns the keyExecutionPeriod.
     */
    public Integer getKeyExecutionPeriod()
    {
        return keyExecutionPeriod;
    }

    /**
     * @param keyExecutionPeriod The keyExecutionPeriod to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod)
    {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /* (non-Javadoc)
     * @see Dominio.credits.CreditLine#getCreditEventGenerated()
     */
    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.OTHER_CREDIT;
    }

    /* (non-Javadoc)
     * @see Dominio.credits.event.ICreditsEventOriginator#belongsToExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod) {
        return this.getExecutionPeriod().equals(executionPeriod);
    }

}
