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

    public static InfoTeacher copyFromDomain(ITeacher teacher) {
        InfoTeacher infoTeacher = InfoTeacher.copyFromDomain(teacher);
        if (infoTeacher != null) {
            infoTeacher.setInfoPerson(InfoPerson.copyFromDomain(teacher
                    .getPerson()));
        }
        return infoTeacher;
    }

}