/*
 * Created on 30/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoTeachingCareerWithInfoCategory extends InfoTeachingCareer {

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer
     * #copyFromDomain(Dominio.teacher.TeachingCareer)
     */
    public void copyFromDomain(TeachingCareer teachingCareer) {
	super.copyFromDomain(teachingCareer);
	if (teachingCareer != null) {
	    setInfoCategory(InfoCategory.newInfoFromDomain(teachingCareer.getCategory()));
	    setInfoTeacher(InfoTeacher.newInfoFromDomain(teachingCareer.getTeacher()));
	}
    }

    public static InfoTeachingCareer newInfoFromDomain(TeachingCareer teachingCareer) {
	InfoTeachingCareerWithInfoCategory infoTeachingCareer = null;
	if (teachingCareer != null) {
	    infoTeachingCareer = new InfoTeachingCareerWithInfoCategory();
	    infoTeachingCareer.copyFromDomain(teachingCareer);
	}
	return infoTeachingCareer;
    }
}