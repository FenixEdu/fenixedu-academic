/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.SummaryManagementToDepartmentAdmOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.SummaryManagementToTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Summary;
import pt.ist.fenixframework.Atomic;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 *         21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 * 
 */
public class DeleteSummary {

    protected Boolean run(ExecutionCourse executionCourse, Summary summary, Professorship professorship)
            throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), executionCourse, summary, professorship);

        if (summary == null) {
            throw new InvalidArgumentsServiceException();
        }

        summary.delete();
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteSummary serviceInstance = new DeleteSummary();

    @Atomic
    public static Boolean runDeleteSummary(ExecutionCourse executionCourse, Summary summary, Professorship professorship)
            throws FenixServiceException, NotAuthorizedException {
        try {
            SummaryManagementToTeacherAuthorizationFilter.instance.execute(summary, professorship);
            return serviceInstance.run(executionCourse, summary, professorship);
        } catch (NotAuthorizedException ex1) {
            try {
                SummaryManagementToDepartmentAdmOfficeAuthorizationFilter.instance.execute(summary, professorship);
                return serviceInstance.run(executionCourse, summary, professorship);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}