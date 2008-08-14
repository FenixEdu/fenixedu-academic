/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Curriculum;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoCurriculumWithInfoCurricularCourse extends InfoCurriculum {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum#copyFromDomain
     * (Dominio.Curriculum)
     */
    public void copyFromDomain(Curriculum curriculum) {
	super.copyFromDomain(curriculum);
	if (curriculum != null) {
	    setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curriculum.getCurricularCourse()));
	}
    }

    public static InfoCurriculum newInfoFromDomain(Curriculum curriculum) {
	InfoCurriculumWithInfoCurricularCourse infoCurriculum = null;
	if (curriculum != null) {
	    infoCurriculum = new InfoCurriculumWithInfoCurricularCourse();
	    infoCurriculum.copyFromDomain(curriculum);
	}
	return infoCurriculum;
    }
}