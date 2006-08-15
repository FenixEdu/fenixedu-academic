/*
 * Created on 14/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear extends
        InfoCurricularCourseScopeWithSemesterAndYear {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope#copyFromDomain(Dominio.CurricularCourseScope)
     */
    public void copyFromDomain(CurricularCourseScope curricularCourseScope) {
        super.copyFromDomain(curricularCourseScope);
        if (curricularCourseScope != null) {
            setInfoCurricularCourse(InfoCurricularCourse
                    .newInfoFromDomain(curricularCourseScope.getCurricularCourse()));
        }
    }

    public static InfoCurricularCourseScope newInfoFromDomain(
            CurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear();
            infoCurricularCourseScope.copyFromDomain(curricularCourseScope);
        }
        return infoCurricularCourseScope;
    }
}