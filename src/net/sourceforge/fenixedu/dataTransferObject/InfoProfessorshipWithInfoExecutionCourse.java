/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Professorship;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoProfessorshipWithInfoExecutionCourse extends InfoProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship#copyFromDomain(Dominio.Professorship)
     */
    public void copyFromDomain(Professorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(professorship
                    .getExecutionCourse()));
        }
    }

    public static InfoProfessorship newInfoFromDomain(Professorship professorship) {
        InfoProfessorshipWithInfoExecutionCourse infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorshipWithInfoExecutionCourse();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }
}