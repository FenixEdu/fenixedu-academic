/*
 * Created on 5:00:05 PM,Mar 10, 2005
 *
 * Author: Goncalo Luiz (goncalo@ist.utl.pt)
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.externalServices;



/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 *
 * Created at 5:00:05 PM, Mar 10, 2005
 */
public class InfoExternalGroupPropertiesInfo
{
    private InfoExternalStudentGroup group;
    private String name;
    private Integer maxCapacity;
    private Integer minCapacity;
    private Integer recomendedCapacity;
    
    /**
     * @return Returns the group.
     */
    public InfoExternalStudentGroup getGroup()
    {
        return this.group;
    }
    /**
     * @param groups The group to set.
     */
    public void setGroup(InfoExternalStudentGroup group)
    {
        this.group = group;
    }
    /**
     * @return Returns the maxCapacity.
     */
    public Integer getMaxCapacity()
    {
        return this.maxCapacity;
    }
    /**
     * @param maxCapacity The maxCapacity to set.
     */
    public void setMaxCapacity(Integer maxCapacity)
    {
        this.maxCapacity = maxCapacity;
    }
    /**
     * @return Returns the minCapacity.
     */
    public Integer getMinCapacity()
    {
        return this.minCapacity;
    }
    /**
     * @param minCapacity The minCapacity to set.
     */
    public void setMinCapacity(Integer minCapacity)
    {
        this.minCapacity = minCapacity;
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
    /**
     * @return Returns the recomendedCapacity.
     */
    public Integer getRecomendedCapacity()
    {
        return this.recomendedCapacity;
    }
    /**
     * @param recomendedCapacity The recomendedCapacity to set.
     */
    public void setRecomendedCapacity(Integer recomendedCapacity)
    {
        this.recomendedCapacity = recomendedCapacity;
    }
}
