/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package DataBeans.teacher.professorship;

import DataBeans.InfoObject;
import DataBeans.InfoProfessorShip;
import DataBeans.InfoShift;

/**
 * @author jpvl
 */
public class InfoShiftProfessorship extends InfoObject
{
    private InfoProfessorShip infoProfessorship;
    private InfoShift infoShift;
    private Float percentage;

    /**
	 * @return Returns the infoProfessorship.
	 */
    public InfoProfessorShip getInfoProfessorship()
    {
        return this.infoProfessorship;
    }

    /**
	 * @return Returns the infoShift.
	 */
    public InfoShift getInfoShift()
    {
        return this.infoShift;
    }

    /**
	 * @return Returns the percentage.
	 */
    public Float getPercentage()
    {
        return this.percentage;
    }

    /**
	 * @param infoProfessorship
	 *                   The infoProfessorship to set.
	 */
    public void setInfoProfessorship(InfoProfessorShip infoProfessorship)
    {
        this.infoProfessorship = infoProfessorship;
    }

    /**
	 * @param infoShift
	 *                   The infoShift to set.
	 */
    public void setInfoShift(InfoShift infoShift)
    {
        this.infoShift = infoShift;
    }

    /**
	 * @param percentage
	 *                   The percentage to set.
	 */
    public void setPercentage(Float percentage)
    {
        this.percentage = percentage;
    }

}
