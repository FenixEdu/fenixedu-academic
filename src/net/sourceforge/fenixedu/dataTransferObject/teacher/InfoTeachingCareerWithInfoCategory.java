/*
 * Created on 30/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * @author Tânia Pousão
 * 
 */
public class InfoTeachingCareerWithInfoCategory extends InfoTeachingCareer {

    private MultiLanguageString categoryName;

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer
     * #copyFromDomain(Dominio.teacher.TeachingCareer)
     */
    @Override
    public void copyFromDomain(TeachingCareer teachingCareer) {
	super.copyFromDomain(teachingCareer);
	if (teachingCareer != null) {
	    setCategoryName(teachingCareer.getCategoryName());
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

    public MultiLanguageString getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(MultiLanguageString categoryName) {
	this.categoryName = categoryName;
    }
}