/*
 * Created on 2:21:40 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 2:21:40 PM, Mar 11, 2005
 */
public class InfoExternalCitizenshipInfo
{
    private String county; 
    private String area;
    /**
     * @return Returns the area.
     */
    public String getArea()
    {
        return this.area;
    }
    /**
     * @param area The area to set.
     */
    public void setArea(String area)
    {
        this.area = area;
    }
    /**
     * @return Returns the county.
     */
    public String getCounty()
    {
        return this.county;
    }
    /**
     * @param county The county to set.
     */
    public void setCounty(String county)
    {
        this.county = county;
    }
}
