/*
 * Created on 14/Jul/2004
 *
 */
package DataBeans;

import Dominio.ICurricularCourseScope;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear extends
        InfoCurricularCourseScopeWithSemesterAndYear {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoCurricularCourseScope#copyFromDomain(Dominio.ICurricularCourseScope)
     */
    public void copyFromDomain(ICurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
            setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourseScope.getCurricularCourse()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            ICurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}