/*
 * Created on 3:33:02 PM,Mar 11, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package DataBeans.externalServices;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 3:33:02 PM, Mar 11, 2005
 */
public class InfoExternalDegreeBranchInfo
{
    private String name;
    private String code;
    /**
     * @return Returns the code.
     */
    public String getCode()
    {
        return this.code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code)
    {
        this.code = code;
    }
    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
}
