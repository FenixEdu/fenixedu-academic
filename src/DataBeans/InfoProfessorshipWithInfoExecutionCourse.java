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
public class InfoProfessorshipWithInfoExecutionCourse extends InfoProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoProfessorship#copyFromDomain(Dominio.IProfessorship)
     */
    public void copyFromDomain(IProfessorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(professorship
                    .getExecutionCourse()));
        }
    }

    public static InfoProfessorship newInfoFromDomain(IProfessorship professorship) {
        InfoProfessorshipWithInfoExecutionCourse infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorshipWithInfoExecutionCourse();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }
}