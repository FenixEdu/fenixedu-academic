/*
 * Created on 12:42:44 PM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 12:42:44 PM, Mar 10, 2005
 */
public class InfoShiftExternalInfo
{
    private String type;
    private String startingTime;

    /**
     * @return Returns the startingTime.
     */
    public String getStartingTime()
    {
        return this.startingTime;
    }
    /**
     * @param startingTime The startingTime to set.
     */
    public void setStartingTime(String startingTime)
    {
        this.startingTime = startingTime;
    }
    /**
     * @return Returns the type.
     */
    public String getType()
    {
        return this.type;
    }
    /**
     * @param type The type to set.
     */
    public void setType(String type)
    {
        this.type = type;
    }
}
