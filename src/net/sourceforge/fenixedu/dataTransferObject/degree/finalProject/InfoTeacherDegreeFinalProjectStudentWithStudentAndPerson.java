/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.degree.finalProject;

import net.sourceforge.fenixedu.dataTransferObject.InfoStudentWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.degree.finalProject.ITeacherDegreeFinalProjectStudent;

/**
 * @author jpvl
 */
public class InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson extends InfoTeacherDegreeFinalProjectStudent {

    public void copyFromDomain(final ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) {
        super.copyFromDomain(teacherDegreeFinalProjectStudent);
        if (teacherDegreeFinalProjectStudent != null) {
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(teacherDegreeFinalProjectStudent.getStudent()));
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(teacherDegreeFinalProjectStudent.getTeacher()));
        }
    }

    public static InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson newInfoFromDomain(
            final ITeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent) {
        if (teacherDegreeFinalProjectStudent != null) {
            InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson infoTeacherDegreeFinalProjectStudent = 
                    new InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson();
            infoTeacherDegreeFinalProjectStudent.copyFromDomain(teacherDegreeFinalProjectStudent);
            return infoTeacherDegreeFinalProjectStudent;
        }
        return null;
    }
}