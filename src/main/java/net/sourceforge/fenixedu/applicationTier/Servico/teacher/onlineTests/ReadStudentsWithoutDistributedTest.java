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
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest {

    protected List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseId);
        final List<Attends> attendList = executionCourse.getAttends();
        final DistributedTest distributedTest = RootDomainObject.getInstance().readDistributedTestByOID(distributedTestId);
        final Set<Registration> students = distributedTest.findStudents();
        for (Attends attend : attendList) {
            if (!students.contains(attend.getRegistration())) {
                infoStudentList.add(InfoStudent.newInfoFromDomain(attend.getRegistration()));
            }
        }
        return infoStudentList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsWithoutDistributedTest serviceInstance = new ReadStudentsWithoutDistributedTest();

    @Service
    public static List runReadStudentsWithoutDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}