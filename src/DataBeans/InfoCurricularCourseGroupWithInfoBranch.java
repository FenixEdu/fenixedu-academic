/*
 * Created on 26/Nov/2003
 *  
 */
package DataBeans;

import Dominio.AreaCurricularCourseGroup;
import Dominio.ICurricularCourseGroup;


/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public abstract class InfoCurricularCourseGroupWithInfoBranch extends InfoCurricularCourseGroup {

    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {

        if (curricularCourseGroup != null) {
            if (curricularCourseGroup instanceof AreaCurricularCourseGroup) {
                return InfoAreaCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);
            }
            return InfoOptionalCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);

        }
        return null;
    }

   

    
}