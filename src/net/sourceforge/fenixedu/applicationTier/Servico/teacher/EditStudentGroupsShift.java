package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditStudentGroupsShift extends Service {

	public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode,
			List<Integer> studentGroupsCodes) throws FenixServiceException{

		Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesCode);
		if (grouping == null) {
			throw new ExistingServiceException();
		}

		Shift shift = rootDomainObject.readShiftByOID(shiftCode);
		if (shift == null) {
			throw new InvalidChangeServiceException();
		}

		// grouping.checkShiftCapacity(shift);
		if (grouping.getShiftType() == null || !shift.containsType(grouping.getShiftType())) {
			throw new NonValidChangeServiceException();
		}

		List<StudentGroup> studentGroups = buildStudentGroupsList(studentGroupsCodes);
		for (StudentGroup studentGroup : studentGroups) {
			if (!studentGroup.getGrouping().equals(grouping)) {
				throw new InvalidArgumentsServiceException();
			}
		}

		for (StudentGroup studentGroup : studentGroups) {
			studentGroup.editShift(shift);
		}
		
        return Boolean.TRUE;
	}

	private List<StudentGroup> buildStudentGroupsList(List<Integer> studentGroupsCodes) throws
			InvalidSituationServiceException {

		List<StudentGroup> studentGroups = new ArrayList<StudentGroup>();
		
        for (Integer studentGroupCode : studentGroupsCodes) {
			StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);

			if (studentGroup == null) {
                throw new InvalidSituationServiceException("error.studentGroupNotInList");    
            }

			studentGroups.add(studentGroup);
		}
        
		return studentGroups;
	}
    
}
