/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.ITeacher;

/**
 * @author João Mota
 *  
 */
public class InfoTeacherWithPerson extends InfoTeacher {

    public void copyFromDomain(ITeacher teacher) {
        super.copyFromDomain(teacher);
        if (teacher != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(teacher.getPerson()));
        }
    }

    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithPerson infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithPerson();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }

}