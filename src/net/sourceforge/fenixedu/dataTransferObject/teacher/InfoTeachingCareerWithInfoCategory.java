/*
 * Created on 30/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoTeachingCareerWithInfoCategory extends InfoTeachingCareer {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.teacher.InfoTeachingCareer#copyFromDomain(Dominio.teacher.ITeachingCareer)
     */
    public void copyFromDomain(ITeachingCareer teachingCareer) {
        super.copyFromDomain(teachingCareer);
        if (teachingCareer != null) {
            setInfoCategory(InfoCategory.newInfoFromDomain(teachingCareer.getCategory()));
        }
    }

    public static InfoTeachingCareer newInfoFromDomain(ITeachingCareer teachingCareer) {
        InfoTeachingCareerWithInfoCategory infoTeachingCareer = null;
        if (teachingCareer != null) {
            infoTeachingCareer = new InfoTeachingCareerWithInfoCategory();
            infoTeachingCareer.copyFromDomain(teachingCareer);
        }
        return infoTeachingCareer;
    }
}