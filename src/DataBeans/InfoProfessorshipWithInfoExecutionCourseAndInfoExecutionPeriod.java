/*
 * Created on 6/Jul/2004
 *
 */
package DataBeans;

import Dominio.IProfessorship;

/**
 * @author Tânia Pousão
 *
 */
public class InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod extends InfoProfessorship {

    /* (non-Javadoc)
     * @see DataBeans.InfoProfessorship#copyFromDomain(Dominio.IProfessorship)
     */
    public void copyFromDomain(IProfessorship professorship) {
        super.copyFromDomain(professorship);
        if(professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(professorship.getExecutionCourse()));
        }
    }
    
    public static InfoProfessorship newInfoFromDomain (IProfessorship professorship) {
        InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod infoProfessorship = null;
        if(professorship != null) {
            infoProfessorship = new InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    } 
}
