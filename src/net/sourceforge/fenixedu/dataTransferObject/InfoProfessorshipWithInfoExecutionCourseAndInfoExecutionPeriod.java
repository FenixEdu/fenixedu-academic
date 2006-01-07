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
public class InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod extends InfoProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship#copyFromDomain(Dominio.Professorship)
     */
    public void copyFromDomain(Professorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(professorship.getExecutionCourse()));
        }
    }

    public static InfoProfessorship newInfoFromDomain(Professorship professorship) {
        InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }
}