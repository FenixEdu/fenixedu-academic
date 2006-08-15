package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

public class InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear extends
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
        InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}