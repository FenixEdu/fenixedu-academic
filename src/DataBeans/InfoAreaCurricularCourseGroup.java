/*
 * Created on 26/Nov/2003
 *  
 */
package DataBeans;

import Dominio.ICurricularCourseGroup;

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

    public void copyFromDomain(ICurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {            
            setMaximumCredits(curricularCourseGroup.getMaximumCredits());
            setMinimumCredits(curricularCourseGroup.getMinimumCredits());
        }
    }
    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        if(curricularCourseGroup != null) {
            infoCurricularCourseGroup = new InfoAreaCurricularCourseGroup();
            infoCurricularCourseGroup.copyFromDomain(curricularCourseGroup);
        }
        return infoCurricularCourseGroup;
       
    }
    
}