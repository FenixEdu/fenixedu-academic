/*
 * Created on 30/Jun/2004
 *
 */
package DataBeans;

import DataBeans.teacher.InfoCategory;
import Dominio.ITeacher;

/**
 * @author Tânia Pousão
 *
 */
public class InfoTeacherWithPersonAndCategory extends InfoTeacherWithPerson {

    /* (non-Javadoc)
     * @see DataBeans.InfoTeacher#copyFromDomain(Dominio.ITeacher)
     */
    public void copyFromDomain(ITeacher teacher) {
        super.copyFromDomain(teacher);
        if(teacher != null) {
            setInfoCategory(InfoCategory.newInfoFromDomain(teacher.getCategory()));
        }
    }
    
    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithPersonAndCategory infoTeacher = null;
        if(teacher != null) {
            infoTeacher = new InfoTeacherWithPersonAndCategory();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }
}
