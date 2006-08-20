package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Tutor;

/**
 * @author Fernanda Quitério Created on 9/Jul/2004
 */
public class InfoTutorWithInfoStudentAndInfoTeacher extends InfoTutorWithInfoTeacher {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoTeacher#copyFromDomain(Dominio.Teacher)
     */
    public void copyFromDomain(Tutor tutor) {
        super.copyFromDomain(tutor);
        if (tutor != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(tutor.getStudent()));

        }
    }

    public static InfoTutor newInfoFromDomain(Tutor tutor) {
        InfoTutorWithInfoStudentAndInfoTeacher infoStudentAndInfoTeacher = null;
        if (tutor != null) {
            infoStudentAndInfoTeacher = new InfoTutorWithInfoStudentAndInfoTeacher();
            infoStudentAndInfoTeacher.copyFromDomain(tutor);
        }
        return infoStudentAndInfoTeacher;
    }
}