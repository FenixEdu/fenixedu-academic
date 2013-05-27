package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

public class ReadExecutionCourseByID {

    protected InfoExecutionCourse run(Integer idInternal) throws FenixServiceException {
        ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(idInternal);
        if (executionCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionCourseByID serviceInstance = new ReadExecutionCourseByID();

    @Service
    public static InfoExecutionCourse runReadExecutionCourseManagerByID(Integer idInternal) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(idInternal);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(idInternal);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}