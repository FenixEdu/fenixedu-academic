/*
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.GroupsAndShiftsManagementLog;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class EditStudentGroupShift extends FenixService {

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode)
	    throws FenixServiceException {

	Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesCode);

	if (grouping == null) {
	    throw new ExistingServiceException();
	}

	Shift shift = rootDomainObject.readShiftByOID(newShiftCode);

	// grouping.checkShiftCapacity(shift);

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

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
}