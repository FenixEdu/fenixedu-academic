/*
 * Created on 11/Fev/2004
 */
package DataBeans.enrollment.shift;

import DataBeans.InfoShift;

/**
 * @author jpvl
 */
public class ShiftEnrollmentDetails
{
    private InfoShift infoShift;
    
    private Integer vacancies;
    
    /**
     * @return Returns the infoShift.
     */
    public InfoShift getInfoShift()
    {
        return infoShift;
    }

    /**
     * @return Returns the vacancies.
     */
    public Integer getVacancies()
    {
        return vacancies;
    }

    /**
     * @param infoShift The infoShift to set.
     */
    public void setInfoShift(InfoShift infoShift)
    {
        this.infoShift = infoShift;
    }

    /**
     * @param vacancies The vacancies to set.
     */
    public void setVacancies(Integer vacancies)
    {
        this.vacancies = vacancies;
    }

}
