/*
 * Created on 11/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author joaosa & rmalo
 * 
 */

public class EnrollStudentGroupShift {

    protected Boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode)
            throws FenixServiceException {

        Grouping grouping = AbstractDomainObject.fromExternalId(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        Shift shift = AbstractDomainObject.fromExternalId(newShiftCode);

        if (shift == null) {
            throw new InvalidSituationServiceException();
        }

        StudentGroup studentGroup = AbstractDomainObject.fromExternalId(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (grouping.getShiftType() == null || studentGroup.getShift() != null || !shift.containsType(grouping.getShiftType())) {
            throw new InvalidChangeServiceException();
        }

        studentGroup.setShift(shift);

        return new Boolean(true);
    }

    // Service Invokers migrated from Berserk

    private static final EnrollStudentGroupShift serviceInstance = new EnrollStudentGroupShift();

    @Service
    public static Boolean runEnrollStudentGroupShift(Integer executionCourseCode, Integer studentGroupCode,
            Integer groupPropertiesCode, Integer newShiftCode) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode, groupPropertiesCode, newShiftCode);
    }

}