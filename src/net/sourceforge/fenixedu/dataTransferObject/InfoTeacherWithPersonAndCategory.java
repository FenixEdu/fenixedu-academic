/*
 * Created on 30/Jun/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoTeacherWithPersonAndCategory extends InfoTeacherWithPerson {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoTeacher#copyFromDomain(Dominio.ITeacher)
     */
    public void copyFromDomain(ITeacher teacher) {
        super.copyFromDomain(teacher);
        if (teacher != null) {
            setInfoCategory(InfoCategory.newInfoFromDomain(teacher.getCategory()));
        }
    }

    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithPersonAndCategory infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithPersonAndCategory();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }
}