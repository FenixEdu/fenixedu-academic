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
    private Double credits;
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

}
