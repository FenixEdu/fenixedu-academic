/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourse;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularCourseWithInfoDegreeCurricularPlan extends InfoCurricularCourse {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse#copyFromDomain(Dominio.CurricularCourse)
     */
    public void copyFromDomain(CurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan.newInfoFromDomain(curricularCourse
                    .getDegreeCurricularPlan()));
        }
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegreeCurricularPlan infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegreeCurricularPlan();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }

        return infoCurricularCourse;
    }
}