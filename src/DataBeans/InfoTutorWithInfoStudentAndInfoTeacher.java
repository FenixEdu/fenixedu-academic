package DataBeans;

import Dominio.ITutor;

/**
 * @author Fernanda Quitério
 * Created on 9/Jul/2004
 */
public class InfoTutorWithInfoStudentAndInfoTeacher extends InfoTutorWithInfoTeacher {

    /* (non-Javadoc)
     * @see DataBeans.InfoTeacher#copyFromDomain(Dominio.ITeacher)
     */
    public void copyFromDomain(ITutor tutor) {
        super.copyFromDomain(tutor);
        if(tutor != null) {
            setInfoStudent(InfoStudentWithInfoPerson.newInfoFromDomain(tutor.getStudent()));
           
        }
    }
    
    public static InfoTutor newInfoFromDomain(ITutor tutor) {
        InfoTutorWithInfoStudentAndInfoTeacher infoStudentAndInfoTeacher = null;
        if(tutor != null) {
            infoStudentAndInfoTeacher = new InfoTutorWithInfoStudentAndInfoTeacher();
            infoStudentAndInfoTeacher.copyFromDomain(tutor);
        }
        return infoStudentAndInfoTeacher;
    }
}
