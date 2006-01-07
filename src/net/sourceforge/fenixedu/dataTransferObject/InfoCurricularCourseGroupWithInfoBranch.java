/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public abstract class InfoCurricularCourseGroupWithInfoBranch extends InfoCurricularCourseGroup {

    public static InfoCurricularCourseGroup newInfoFromDomain(
            CurricularCourseGroup curricularCourseGroup) {

        if (curricularCourseGroup != null) {
            if (curricularCourseGroup instanceof AreaCurricularCourseGroup) {
                return InfoAreaCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);
            }
            return InfoOptionalCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);

        }
        return null;
    }

}
