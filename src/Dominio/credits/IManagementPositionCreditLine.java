/*
 * Created on 7/Mar/2004
 */
package Dominio.credits;

/**
 * @author jpvl
 */
public interface IManagementPositionCreditLine extends IDatePeriodBasedCreditLine
{
    /**
     * @return Returns the position.
     */
    public abstract String getPosition();
    /**
     * @param position The position to set.
     */
    public abstract void setPosition(String position);
}