/*
 * Created on 16/Fev/2004
 */
package DataBeans.credits;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author jpvl
 */
public abstract class InfoCreditLine extends InfoObject
{
    private InfoTeacher infoTeacher;
    
    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
     * @param infoTeacher The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

}
