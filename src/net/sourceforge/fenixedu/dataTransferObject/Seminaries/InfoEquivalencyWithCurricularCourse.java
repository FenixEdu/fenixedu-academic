/*
 * Created on 1/Ago/2003, 21:13:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 1/Ago/2003, 21:13:05
 *  
 */
public class InfoEquivalencyWithCurricularCourse extends InfoEquivalency {

    public void copyFromDomain(ICourseEquivalency courseEquivalency) {
        super.copyFromDomain(courseEquivalency);
        if (courseEquivalency != null) {
            setCurricularCourse(InfoCurricularCourseWithInfoDegreeCurricularPlan.newInfoFromDomain(courseEquivalency.getCurricularCourse()));
        }
    }

    public static InfoEquivalencyWithCurricularCourse newInfoFromDomain(ICourseEquivalency courseEquivalency) {
        InfoEquivalencyWithCurricularCourse infoEquivalency = null;
        if (courseEquivalency != null) {
            infoEquivalency = new InfoEquivalencyWithCurricularCourse();
            infoEquivalency.copyFromDomain(courseEquivalency);
        }
        return infoEquivalency;
    }

}