package DataBeans;

import Dominio.ITutor;

/**
 * @author Fernanda Quitério
 * Created on 9/Jul/2004
 * 
 */
public class InfoTutorWithInfoTeacher extends InfoTutor {
    public void copyFromDomain(ITutor tutor) {
        super.copyFromDomain(tutor);
        if(tutor != null) {
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(tutor.getTeacher()));
        }
    }
    
    public static InfoTutor newInfoFromDomain(ITutor tutor) {
        InfoTutorWithInfoTeacher infoTutorWithInfoTeacher = null;
        if(tutor != null) {
            infoTutorWithInfoTeacher = new InfoTutorWithInfoTeacher();
            infoTutorWithInfoTeacher.copyFromDomain(tutor);
        }
        return infoTutorWithInfoTeacher;
    }
    
}
