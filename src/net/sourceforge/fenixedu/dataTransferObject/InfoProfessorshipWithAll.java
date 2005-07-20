/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IProfessorship;

/**
 * @author João Mota
 *  
 */
public class InfoProfessorshipWithAll extends InfoProfessorship {

    public void copyFromDomain(IProfessorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(professorship.getExecutionCourse()));
            setInfoTeacher(InfoTeacherWithPerson.newInfoFromDomain(professorship.getTeacher()));           
            setResponsibleFor(professorship.getResponsibleFor());
        }
    }

    public static InfoProfessorship newInfoFromDomain(IProfessorship professorship) {
        InfoProfessorshipWithAll infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorshipWithAll();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }

}