/*
 * Created on 2:32:07 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:32:07 PM, Mar 11, 2005
 */
public class InfoExternalAdressInfo
{
    private String street;
    private String postalCode;
    private String town;
    
    
    /**
     * @return Returns the postalCode.
     */
    public String getPostalCode()
    {
        return this.postalCode;
    }
    /**
     * @param postalCode The postalCode to set.
     */
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }
    /**
     * @return Returns the street.
     */
    public String getStreet()
    {
        return this.street;
    }
    /**
     * @param street The street to set.
     */
    public void setStreet(String street)
    {
        this.street = street;
    }
    /**
     * @return Returns the town.
     */
    public String getTown()
    {
        return this.town;
    }
    /**
     * @param town The town to set.
     */
    public void setTown(String town)
    {
        this.town = town;
    }
}
