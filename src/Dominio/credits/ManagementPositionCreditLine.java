/*
 * Created on 7/Mar/2004
 */
package Dominio.credits;

/**
 * @author jpvl
 */
public class ManagementPositionCreditLine extends DatePeriodBaseCreditLine implements IManagementPositionCreditLine
{
    private String position;
    
    
    /**
     * @return Returns the position.
     */
    public String getPosition()
    {
        return position;
    }

    /**
     * @param position The position to set.
     */
    public void setPosition(String position)
    {
        this.position = position;
    }

}
