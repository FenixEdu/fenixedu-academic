/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICurricularCourse;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoCurricularCourseWithInfoDegree extends InfoCurricularCourse {

    public void copyFromDomain(ICurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                    .newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()));
        }
    }

    public static InfoCurricularCourse newInfoFromDomain(ICurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegree infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegree();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }

        return infoCurricularCourse;
    }
}