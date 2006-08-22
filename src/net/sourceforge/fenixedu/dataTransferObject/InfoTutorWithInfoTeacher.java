package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Tutor;

/**
 * @author Fernanda Quitério Created on 9/Jul/2004
 *  
 */
public class InfoTutorWithInfoTeacher extends InfoTutor {
    public void copyFromDomain(Tutor tutor) {
        super.copyFromDomain(tutor);
        if (tutor != null) {
            setInfoTeacher(InfoTeacher.newInfoFromDomain(tutor.getTeacher()));
        }
    }

    public static InfoTutor newInfoFromDomain(Tutor tutor) {
        InfoTutorWithInfoTeacher infoTutorWithInfoTeacher = null;
        if (tutor != null) {
            infoTutorWithInfoTeacher = new InfoTutorWithInfoTeacher();
            infoTutorWithInfoTeacher.copyFromDomain(tutor);
        }
        return infoTutorWithInfoTeacher;
    }

}