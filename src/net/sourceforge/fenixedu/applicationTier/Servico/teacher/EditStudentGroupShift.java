/*
 * Created on 21/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public class EditStudentGroupShift extends Service {

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

	studentGroup.editShift(shift);

	return Boolean.TRUE;
    }
}