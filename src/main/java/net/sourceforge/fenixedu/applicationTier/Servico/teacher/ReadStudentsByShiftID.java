package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class ReadStudentsByShiftID extends FenixService {

    protected List run(final Integer executionCourseID, final Integer shiftID) {
        final List infoStudents = new LinkedList();
        final Shift shift = rootDomainObject.readShiftByOID(shiftID);
        final List<Registration> students = shift.getStudents();
        for (final Registration registration : students) {
            infoStudents.add(InfoStudent.newInfoFromDomain(registration));
        }

        return infoStudents;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentsByShiftID serviceInstance = new ReadStudentsByShiftID();

    @Service
    public static List runReadStudentsByShiftID(Integer executionCourseID, Integer shiftID) throws NotAuthorizedException {
        try {
            ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
            return serviceInstance.run(executionCourseID, shiftID);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseID);
                return serviceInstance.run(executionCourseID, shiftID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}