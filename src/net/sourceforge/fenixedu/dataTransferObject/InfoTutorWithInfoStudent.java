package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Tutor;

/**
 * @author Fernanda Quitério Created on 9/Jul/2004
 *  
 */
public class InfoTutorWithInfoStudent extends InfoTutor {
    public void copyFromDomain(Tutor tutor) {
        super.copyFromDomain(tutor);
        if (tutor != null) {
            setInfoStudent(InfoStudent.newInfoFromDomain(tutor.getStudent()));
        }
    }

    public static InfoTutor newInfoFromDomain(Tutor tutor) {
        InfoTutorWithInfoStudent infoTutorWithInfoStudent = null;
        if (tutor != null) {
            infoTutorWithInfoStudent = new InfoTutorWithInfoStudent();
            infoTutorWithInfoStudent.copyFromDomain(tutor);
        }
        return infoTutorWithInfoStudent;
    }

}