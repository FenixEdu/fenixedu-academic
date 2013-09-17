/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest {

    protected List run(String executionCourseId, String distributedTestId) throws FenixServiceException {
        final List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        final Collection<Attends> attendList = executionCourse.getAttends();
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
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

    @Atomic
    public static List runReadStudentsWithoutDistributedTest(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}