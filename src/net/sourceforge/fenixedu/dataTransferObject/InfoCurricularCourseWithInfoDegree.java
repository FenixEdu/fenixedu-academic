/*
 * Created on 23/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourse;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoCurricularCourseWithInfoDegree extends InfoCurricularCourse {

    public void copyFromDomain(CurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(curricularCourse.getDegreeCurricularPlan()));
        }
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegree infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegree();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }

        return infoCurricularCourse;
    }
}