/*
 * Created on 23/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ScientificCouncilAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class ReadExecutionCourseResponsiblesIds extends FenixService {

    protected List run(Integer executionCourseId) throws FenixServiceException {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        List<Professorship> responsibles = executionCourse.responsibleFors();

        List<Integer> responsibleIDs = new ArrayList<Integer>();
        if (responsibles != null) {
            for (Professorship responsibleFor : responsibles) {
                responsibleIDs.add(responsibleFor.getTeacher().getIdInternal());
            }
        }
        return responsibleIDs;
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionCourseResponsiblesIds serviceInstance = new ReadExecutionCourseResponsiblesIds();

    @Service
    public static List runReadExecutionCourseResponsiblesIds(Integer executionCourseId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}