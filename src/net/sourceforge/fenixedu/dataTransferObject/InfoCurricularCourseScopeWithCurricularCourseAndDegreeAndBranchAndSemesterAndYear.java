package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ICurricularCourseScope;

public class InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear extends
        InfoCurricularCourseScopeWithBranchAndSemesterAndYear {

    public void copyFromDomain(ICurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
            setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curricularCourseScope
                    .getCurricularCourse()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            ICurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndBranchAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}