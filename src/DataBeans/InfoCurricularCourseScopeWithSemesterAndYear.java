/*
 * Created on 29/Jun/2004
 *
 */
package DataBeans;

import Dominio.ICurricularCourseScope;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularCourseScopeWithSemesterAndYear extends InfoCurricularCourseScope {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoCurricularCourseScope#copyFromDomain(Dominio.ICurricularCourseScope)
     */
    public void copyFromDomain(ICurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
            setInfoCurricularSemester(InfoCurricularSemesterWithInfoCurricularYear
                    .newInfoFromDomain(curricularCourseScope.getCurricularSemester()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            ICurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithBranchAndSemesterAndYear infoCCScope = null;
        if (curricularCourseScope != null) {
            infoCCScope = new InfoCurricularCourseScopeWithBranchAndSemesterAndYear();
            infoCCScope.copyFromDomain(curricularCourseScope);
        }
        return infoCCScope;
    }
}