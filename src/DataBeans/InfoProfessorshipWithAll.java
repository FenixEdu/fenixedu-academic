/*
 * Created on Jun 7, 2004
 *  
 */
package DataBeans;

import Dominio.IProfessorship;

/**
 * @author João Mota
 *  
 */
public class InfoProfessorshipWithAll extends InfoProfessorship {

    public static InfoProfessorship copyFromDomain(IProfessorship professorship) {
        InfoProfessorship infoProfessorship = InfoProfessorship
                .copyFromDomain(professorship);
        if (infoProfessorship != null) {
            infoProfessorship
                    .setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                            .copyFromDomain(professorship.getExecutionCourse()));
            infoProfessorship.setInfoTeacher(InfoTeacherWithPerson
                    .copyFromDomain(professorship.getTeacher()));
        }
        return infoProfessorship;
    }

}