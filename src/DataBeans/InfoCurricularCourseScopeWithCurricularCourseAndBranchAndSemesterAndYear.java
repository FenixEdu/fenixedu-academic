package DataBeans;

import Dominio.ICurricularCourseScope;

/**
 * @author Fernanda Quitério
 * Created on 23/Jul/2004
 *
 */
public class InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear extends
        InfoCurricularCourseScopeWithBranchAndSemesterAndYear {

    public void copyFromDomain(ICurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if(curricularCourseScope != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourseScope.getCurricularCourse()));
        }
    }
    
    public static InfoCurricularCourseScope newInfoFromDomain(ICurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear infoCurricularCourseScope = null;
        if(curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}
