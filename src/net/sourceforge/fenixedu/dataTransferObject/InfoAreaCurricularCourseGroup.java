/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;


/**
 * @author João Mota
 */

public class InfoAreaCurricularCourseGroup extends InfoCurricularCourseGroup {

    public InfoAreaCurricularCourseGroup() {
    }

    public Integer getMaximumCredits() {
        return super.getMaximumValue();
    }

    public void setMaximumCredits(Integer maximumCredits) {
        super.setMaximumValue(maximumCredits);
    }

    public Integer getMinimumCredits() {
        return super.getMinimumValue();
    }

    public void setMinimumCredits(Integer minimumCredits) {
        super.setMinimumValue(minimumCredits);
    }

    /**
     * @return
     */
    public net.sourceforge.fenixedu.tools.enrollment.AreaType getAreaType() {
        return areaType;
    }

    /**
     * @param areaType
     */
    public void setAreaType(net.sourceforge.fenixedu.tools.enrollment.AreaType areaType) {
        this.areaType = areaType;
    }

    public void copyFromDomain(ICurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {
            setAreaType(curricularCourseGroup.getAreaType());
            setMaximumCredits(curricularCourseGroup.getMaximumCredits());
            setMinimumCredits(curricularCourseGroup.getMinimumCredits());
        }
    }

    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        if (curricularCourseGroup != null) {
            infoCurricularCourseGroup = new InfoAreaCurricularCourseGroup();
            infoCurricularCourseGroup.copyFromDomain(curricularCourseGroup);
        }
        return infoCurricularCourseGroup;

    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup#getType()
     */
    public String getType() {
        return "label.curricularCourseGroup.area";
    }

}