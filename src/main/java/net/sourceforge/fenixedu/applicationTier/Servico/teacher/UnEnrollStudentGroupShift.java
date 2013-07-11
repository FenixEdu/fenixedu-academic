/*
 * Created on 12/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa and rmalo
 * 
 */

public class UnEnrollStudentGroupShift {

    protected Boolean run(String executionCourseCode, String studentGroupCode, String groupPropertiesCode)
            throws FenixServiceException {

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!(studentGroup.getShift() != null && groupProperties.getShiftType() == null) || studentGroup.getShift() == null) {
            throw new InvalidChangeServiceException();
        }

        Shift shift = null;
        studentGroup.setShift(shift);

        return new Boolean(true);
    }

    // Service Invokers migrated from Berserk

    private static final UnEnrollStudentGroupShift serviceInstance = new UnEnrollStudentGroupShift();

    @Service
    public static Boolean runUnEnrollStudentGroupShift(String executionCourseCode, String studentGroupCode,
            String groupPropertiesCode) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode, groupPropertiesCode);
    }

}