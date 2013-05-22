/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest extends FenixService {

    protected InfoDistributedTest run(Integer executionCourseId, Integer distributedTestId)
            throws InvalidArgumentsServiceException {
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        return InfoDistributedTest.newInfoFromDomain(distributedTest);
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTest serviceInstance = new ReadDistributedTest();

    @Service
    public static InfoDistributedTest runReadDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}