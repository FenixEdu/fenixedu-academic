/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.util.CareerType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoTeachingCareer extends InfoCareer {
    private InfoCategory infoCategory;

    private String courseOrPosition;

    /**
     *  
     */
    public InfoTeachingCareer() {
        setCareerType(CareerType.TEACHING);
    }

    /**
     * @return Returns the courseOrPosition.
     */
    public String getCourseOrPosition() {
        return courseOrPosition;
    }

    /**
     * @param courseOrPosition
     *            The courseOrPosition to set.
     */
    public void setCourseOrPosition(String courseOrPosition) {
        this.courseOrPosition = courseOrPosition;
    }

    /**
     * @return Returns the infoCategory.
     */
    public InfoCategory getInfoCategory() {
        return infoCategory;
    }

    /**
     * @param infoCategory
     *            The infoCategory to set.
     */
    public void setInfoCategory(InfoCategory infoCategory) {
        this.infoCategory = infoCategory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.teacher.InfoCareer#copyFromDomain(Dominio.teacher.ICareer)
     */
    public void copyFromDomain(ITeachingCareer teachingCareer) {
        super.copyFromDomain(teachingCareer);
        if (teachingCareer != null) {
            setCourseOrPosition(teachingCareer.getCourseOrPosition());
        }
    }

    public static InfoTeachingCareer newInfoFromDomain(ITeachingCareer teachingCareer) {
        InfoTeachingCareer infoTeachingCareer = null;
        if (teachingCareer != null) {
            infoTeachingCareer = new InfoTeachingCareer();
            infoTeachingCareer.copyFromDomain(teachingCareer);
        }
        return infoTeachingCareer;
    }
}