/*
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author asnr and scpo
 * 
 */

public class EditStudentGroupShift {

    protected Boolean run(String executionCourseCode, String studentGroupCode, String groupPropertiesCode, String newShiftCode)
            throws FenixServiceException {

        Grouping grouping = AbstractDomainObject.fromExternalId(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        Shift shift = AbstractDomainObject.fromExternalId(newShiftCode);

        // grouping.checkShiftCapacity(shift);

        StudentGroup studentGroup = AbstractDomainObject.fromExternalId(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        String oldShiftName = studentGroup.getShift().getNome();
        studentGroup.editShift(shift);
        List<ExecutionCourse> ecs = grouping.getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, "resources.MessagingResources",
                    "log.executionCourse.groupAndShifts.grouping.shift.edited", studentGroup.getGroupNumber().toString(),
                    oldShiftName, shift.getNome(), grouping.getName(), ec.getNome(), ec.getDegreePresentationString());
        }

        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final EditStudentGroupShift serviceInstance = new EditStudentGroupShift();

    @Service
    public static Boolean runEditStudentGroupShift(String executionCourseCode, String studentGroupCode,
            String groupPropertiesCode, String newShiftCode) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseCode);
        return serviceInstance.run(executionCourseCode, studentGroupCode, groupPropertiesCode, newShiftCode);
    }

}