/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;

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

    public InfoCareer() {
	super();
    }

    public Integer getBeginYear() {
	return beginYear;
    }

    public void setBeginYear(Integer beginYear) {
	this.beginYear = beginYear;
    }

    public Integer getEndYear() {
	return endYear;
    }

    public void setEndYear(Integer endYear) {
	this.endYear = endYear;
    }

    public InfoTeacher getInfoTeacher() {
	return infoTeacher;
    }

    public void setInfoTeacher(InfoTeacher infoTeacher) {
	this.infoTeacher = infoTeacher;
    }

    public CareerType getCareerType() {
	return careerType;
    }

    public void setCareerType(CareerType careerType) {
	this.careerType = careerType;
    }

    public Date getLastModificationDate() {
	return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
	this.lastModificationDate = lastModificationDate;
    }

    public void copyFromDomain(Career career) {
	super.copyFromDomain(career);
	if (career != null) {
	    setBeginYear(career.getBeginYear());
	    setEndYear(career.getEndYear());
	    setLastModificationDate(career.getLastModificationDate());
	}
    }

    public static InfoCareer newInfoFromDomain(Career career) {
	InfoCareer infoCareer = null;
	if (career != null) {
	    if (career instanceof TeachingCareer) {
		infoCareer = InfoTeachingCareerWithInfoCategory.newInfoFromDomain((TeachingCareer) career);
	    } else if (career instanceof ProfessionalCareer) {
		infoCareer = InfoProfessionalCareer.newInfoFromDomain((ProfessionalCareer) career);
	    }
	}
	return infoCareer;
    }
}