/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.ICareer;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public abstract class InfoCareer extends InfoObject implements ISiteComponent {
    private Integer beginYear;

    private Integer endYear;

    private CareerType careerType;

    private InfoTeacher infoTeacher;

    private Date lastModificationDate;

    /**
     *  
     */
    public InfoCareer() {
        super();
    }

    /**
     * @return Returns the beginYear.
     */
    public Integer getBeginYear() {
        return beginYear;
    }

    /**
     * @param beginYear
     *            The beginYear to set.
     */
    public void setBeginYear(Integer beginYear) {
        this.beginYear = beginYear;
    }

    /**
     * @return Returns the endYear.
     */
    public Integer getEndYear() {
        return endYear;
    }

    /**
     * @param endYear
     *            The endYear to set.
     */
    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return Returns the careerType.
     */
    public CareerType getCareerType() {
        return careerType;
    }

    /**
     * @param careerType
     *            The careerType to set.
     */
    public void setCareerType(CareerType careerType) {
        this.careerType = careerType;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(ICareer career) {
        super.copyFromDomain(career);
        if (career != null) {
            setBeginYear(career.getBeginYear());
            setEndYear(career.getEndYear());
            setLastModificationDate(career.getLastModificationDate());
        }
    }

    public static InfoCareer newInfoFromDomain(ICareer career) {
        InfoCareer infoCareer = null;
        if (career != null) {
            if (career instanceof IProfessionalCareer) {
                infoCareer = InfoProfessionalCareer.newInfoFromDomain((IProfessionalCareer) career);
            } else if (career instanceof ITeachingCareer) {
                infoCareer = InfoTeachingCareerWithInfoCategory
                        .newInfoFromDomain((ITeachingCareer) career);
            }
        }
        return infoCareer;
    }
}