/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithDistributedTest {

    protected List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {
        final List<InfoStudent> result = new ArrayList<InfoStudent>();

        final DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        if (distributedTest == null) {
            throw new FenixServiceException();
        }

        final Set<Registration> students = distributedTest.findStudents();
        for (Registration registration : students) {
            result.add(InfoStudent.newInfoFromDomain(registration));
        }

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsWithDistributedTest serviceInstance = new ReadStudentsWithDistributedTest();

    @Service
    public static List runReadStudentsWithDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}