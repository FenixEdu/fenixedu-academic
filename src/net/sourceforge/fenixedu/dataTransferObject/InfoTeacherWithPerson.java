/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Teacher;

/**
 * @author João Mota
 * 
 */
public class InfoTeacherWithPerson extends InfoTeacher {

    public void copyFromDomain(Teacher teacher) {
        super.copyFromDomain(teacher);
        if (teacher != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(teacher.getPerson()));
        }
    }

    public static InfoTeacher newInfoFromDomain(Teacher teacher) {
        InfoTeacherWithPerson infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithPerson();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }

}
