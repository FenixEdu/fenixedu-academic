/*
 * Created on 6/Jul/2004
 *
 */
package DataBeans;

import Dominio.ICurriculum;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCurriculumWithInfoCurricularCourse extends InfoCurriculum {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoCurriculum#copyFromDomain(Dominio.ICurriculum)
     */
    public void copyFromDomain(ICurriculum curriculum) {
        super.copyFromDomain(curriculum);
        if (curriculum != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curriculum
                    .getCurricularCourse()));
        }
    }

    public static InfoCurriculum newInfoFromDomain(ICurriculum curriculum) {
        InfoCurriculumWithInfoCurricularCourse infoCurriculum = null;
        if (curriculum != null) {
            infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
            infoCurriculum.copyFromDomain(curriculum);
        }
        return infoCurriculum;
    }
}