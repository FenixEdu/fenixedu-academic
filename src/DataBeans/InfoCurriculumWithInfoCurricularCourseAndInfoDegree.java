/*
 * Created on 8/Jul/2004
 *
 */
package DataBeans;

import Dominio.ICurriculum;

/**
 * @author Tânia Pousão
 *
 */
public class InfoCurriculumWithInfoCurricularCourseAndInfoDegree extends
        InfoCurriculum {

    /* (non-Javadoc)
     * @see DataBeans.InfoCurriculum#copyFromDomain(Dominio.ICurriculum)
     */
    public void copyFromDomain(ICurriculum curriculum) {
        super.copyFromDomain(curriculum);
        if(curriculum != null) {
            setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(curriculum.getCurricularCourse()));
        }
    }
    
    public static InfoCurriculum newInfoFromDomain(ICurriculum curriculum) {
        InfoCurriculumWithInfoCurricularCourseAndInfoDegree infoCurriculum = null;
        if(curriculum != null) {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourseAndInfoDegree();
            infoCurriculum.copyFromDomain(curriculum);
        }
        return infoCurriculum;
    }
}
