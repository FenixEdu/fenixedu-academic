/*
 * Created on 29/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularCourseScopeWithBranchAndSemesterAndYear extends InfoCurricularCourseScope {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope#copyFromDomain(Dominio.CurricularCourseScope)
     */
    public void copyFromDomain(CurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
        	setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourseScope.getCurricularCourse()));
            setInfoBranch(InfoBranch.newInfoFromDomain(curricularCourseScope.getBranch()));
            setInfoCurricularSemester(InfoCurricularSemesterWithInfoCurricularYear
                    .newInfoFromDomain(curricularCourseScope.getCurricularSemester()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            CurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithBranchAndSemesterAndYear infoCCScope = null;
        if (curricularCourseScope != null) {
            infoCCScope = new InfoCurricularCourseScopeWithBranchAndSemesterAndYear();
            infoCCScope.copyFromDomain(curricularCourseScope);
        }
        return infoCCScope;
    }
}