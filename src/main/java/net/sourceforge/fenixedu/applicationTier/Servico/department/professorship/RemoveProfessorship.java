/*
 * Created on Dec 14, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.CreditsServiceWithTeacherIdInSecondArgumentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.DeleteProfessorship;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class RemoveProfessorship extends DeleteProfessorship {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ServidorAplicacao.Servico.teacher.DeleteTeacher#canDeleteResponsibleFor()
     */
    @Override
    protected boolean canDeleteResponsibleFor() {
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final RemoveProfessorship serviceInstance = new RemoveProfessorship();

    @Service
    public static Boolean runRemoveProfessorshipByDepartment(Integer infoExecutionCourseCode, Integer teacherCode)
            throws FenixServiceException {
        CreditsServiceWithTeacherIdInSecondArgumentAuthorization.instance.execute(teacherCode);
        return serviceInstance.run(infoExecutionCourseCode, teacherCode);
    }

}