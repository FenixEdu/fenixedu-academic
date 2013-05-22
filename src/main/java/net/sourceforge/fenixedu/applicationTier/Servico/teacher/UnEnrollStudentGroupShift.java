/*
 * Created on 12/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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

/**
 * @author joaosa and rmalo
 * 
 */

public class UnEnrollStudentGroupShift extends FenixService {

    protected Boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode)
            throws FenixServiceException {

        Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

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
    public static Boolean runUnEnrollStudentGroupShift(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode) throws FenixServiceException  , NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode, groupPropertiesCode);
    }

}