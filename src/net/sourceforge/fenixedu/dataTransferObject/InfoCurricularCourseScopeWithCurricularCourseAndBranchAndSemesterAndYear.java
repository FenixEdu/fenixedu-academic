package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

/**
 * @author Fernanda Quitério Created on 23/Jul/2004
 *  
 */
public class InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear extends
        InfoCurricularCourseScopeWithBranchAndSemesterAndYear {

    public void copyFromDomain(CurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourseScope
                    .getCurricularCourse()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            CurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndBranchAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}