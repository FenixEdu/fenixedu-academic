/*
 * Created on 13/Nov/2003
 *  
 */
package DataBeans.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import Util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class InfoCareer extends InfoObject implements ISiteComponent
{
    private Integer beginYear;
    private Integer endYear;
    private CareerType careerType;
    private InfoTeacher infoTeacher;

    /**
	 *  
	 */
    public InfoCareer()
    {
        super();
    }

    /**
	 * @return Returns the beginYear.
	 */
    public Integer getBeginYear()
    {
        return beginYear;
    }

    /**
	 * @param beginYear
	 *            The beginYear to set.
	 */
    public void setBeginYear(Integer beginYear)
    {
        this.beginYear = beginYear;
    }

    /**
	 * @return Returns the endYear.
	 */
    public Integer getEndYear()
    {
        return endYear;
    }

    /**
	 * @param endYear
	 *            The endYear to set.
	 */
    public void setEndYear(Integer endYear)
    {
        this.endYear = endYear;
    }

    /**
	 * @return Returns the infoTeacher.
	 */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
	 * @param infoTeacher
	 *            The infoTeacher to set.
	 */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }
    /**
     * @return Returns the careerType.
     */
    public CareerType getCareerType()
    {
        return careerType;
    }

    /**
     * @param careerType The careerType to set.
     */
    public void setCareerType(CareerType careerType)
    {
        this.careerType = careerType;
    }

}
