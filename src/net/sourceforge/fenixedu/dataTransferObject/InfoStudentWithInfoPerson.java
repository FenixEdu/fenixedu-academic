/*
 * Created on 17/Jun/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Student;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoStudentWithInfoPerson extends InfoStudent {

    public void copyFromDomain(Student student) {
        super.copyFromDomain(student);
        if (student != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(student.getPerson()));
        }
    }

    public static InfoStudent newInfoFromDomain(Student student) {
        InfoStudentWithInfoPerson infoStudent = null;
        if (student != null) {
            infoStudent = new InfoStudentWithInfoPerson();
            infoStudent.copyFromDomain(student);
        }
        return infoStudent;
    }
}