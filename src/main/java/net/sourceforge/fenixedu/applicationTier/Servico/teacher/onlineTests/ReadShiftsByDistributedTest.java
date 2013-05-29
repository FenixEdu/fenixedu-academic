/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest {

    public List<InfoShift> run(String executionCourseId, String distributedTestId) throws FenixServiceException {

        final DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        final Set<Registration> students = distributedTest != null ? distributedTest.findStudents() : new HashSet<Registration>();

        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Set<Shift> shiftList = executionCourse.getAssociatedShifts();

        List<InfoShift> result = new ArrayList<InfoShift>();
        for (Shift shift : shiftList) {
            List<Registration> shiftStudents = shift.getStudents();
            if (!students.containsAll(shiftStudents)) {
                result.add(InfoShift.newInfoFromDomain(shift));
            }
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadShiftsByDistributedTest serviceInstance = new ReadShiftsByDistributedTest();

    @Service
    public static List<InfoShift> runReadShiftsByDistributedTest(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}