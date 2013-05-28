/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.CreditsServiceWithTeacherIdArgumentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsByExecutionPeriod extends ReadDetailedTeacherProfessorshipsAbstractService {

    protected List run(Integer teacherOID, Integer executionPeriodOID) throws FenixServiceException {

        final ExecutionSemester executionSemester;
        if (executionPeriodOID == null) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            executionSemester = AbstractDomainObject.fromExternalId(executionPeriodOID);
        }

        final Teacher teacher = AbstractDomainObject.fromExternalId(teacherOID);
        final List<Professorship> responsibleFors = new ArrayList<Professorship>();
        for (Professorship professorship : teacher.responsibleFors()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                responsibleFors.add(professorship);
            }
        }
        return getDetailedProfessorships(teacher.getProfessorships(executionSemester), responsibleFors);
    }

    // Service Invokers migrated from Berserk

    private static final ReadDetailedTeacherProfessorshipsByExecutionPeriod serviceInstance =
            new ReadDetailedTeacherProfessorshipsByExecutionPeriod();

    @Service
    public static List runReadDetailedTeacherProfessorshipsByExecutionPeriod(Integer teacherOID, Integer executionPeriodOID)
            throws FenixServiceException, NotAuthorizedException {
        CreditsServiceWithTeacherIdArgumentAuthorization.instance.execute(teacherOID);
        return serviceInstance.run(teacherOID, executionPeriodOID);
    }

}