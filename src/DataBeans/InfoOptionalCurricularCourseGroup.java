/*
 * Created on 26/Nov/2003
 *  
 */
package DataBeans;

import Dominio.ICurricularCourseGroup;

/**
 * @author João Mota
 */

public class InfoOptionalCurricularCourseGroup extends InfoCurricularCourseGroup {

    public InfoOptionalCurricularCourseGroup() {
    }

    public Integer getMinimumNumberOfOptionalCourses() {
        return super.getMinimumValue();
    }

    public Integer getMaximumNumberOfOptionalCourses() {
        return super.getMaximumValue();
    }

    public void setMaximumNumberOfOptionalCourses(Integer maximumNumberOfOptionalCourses) {
        super.setMaximumValue(maximumNumberOfOptionalCourses);
    }

    public void setMinimumNumberOfOptionalCourses(Integer minimumNumberOfOptionalCourses) {
        super.setMinimumValue(minimumNumberOfOptionalCourses);
    }

    public Integer getMaximumCredits() {
        return null;
    }

    public void setMaximumCredits(Integer maximumCredits) {
    }

    public Integer getMinimumCredits() {
        return null;
    }

    public void setMinimumCredits(Integer minimumCredits) {
    }

    public void copyFromDomain(ICurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {

            setMaximumNumberOfOptionalCourses(curricularCourseGroup.getMaximumNumberOfOptionalCourses());
            setMinimumNumberOfOptionalCourses(curricularCourseGroup.getMinimumNumberOfOptionalCourses());
        }
    }

    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        if (curricularCourseGroup != null) {
            infoCurricularCourseGroup = new InfoOptionalCurricularCourseGroup();
            infoCurricularCourseGroup.copyFromDomain(curricularCourseGroup);
        }
        return infoCurricularCourseGroup;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoCurricularCourseGroup#getType()
     */
    public String getType() {

        return "label.curricularCourseGroup.optional";
    }
}