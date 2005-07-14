/*
 * Created on 26/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.lang.reflect.Proxy;

import net.sourceforge.fenixedu.domain.IAreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;

import org.apache.ojb.broker.core.proxy.ProxyHelper;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public abstract class InfoCurricularCourseGroupWithInfoBranch extends InfoCurricularCourseGroup {

    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {

        if (curricularCourseGroup != null) {

            if (curricularCourseGroup instanceof Proxy) {
                curricularCourseGroup = (ICurricularCourseGroup) ProxyHelper
                        .getRealObject(curricularCourseGroup);
            }

            if (curricularCourseGroup instanceof IAreaCurricularCourseGroup) {
                return InfoAreaCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);
            }
            return InfoOptionalCurricularCourseGroupWithBranch.newInfoFromDomain(curricularCourseGroup);

        }
        return null;
    }

}
