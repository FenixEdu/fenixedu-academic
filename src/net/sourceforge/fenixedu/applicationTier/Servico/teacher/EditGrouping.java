/*
 * Created on 17/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 */
public class EditGrouping extends Service {

    public List run(Integer executionCourseID, InfoGrouping infoGroupProperties) throws FenixServiceException {
	final Grouping grouping = rootDomainObject.readGroupingByOID(infoGroupProperties.getIdInternal());
	if (grouping == null) {
	    throw new InvalidArgumentsServiceException();
	}

	grouping.edit(infoGroupProperties.getName(), infoGroupProperties.getEnrolmentBeginDay().getTime(), infoGroupProperties
		.getEnrolmentEndDay().getTime(), infoGroupProperties.getEnrolmentPolicy(), infoGroupProperties
		.getGroupMaximumNumber(), infoGroupProperties.getIdealCapacity(), infoGroupProperties.getMaximumCapacity(),
		infoGroupProperties.getMinimumCapacity(), infoGroupProperties.getProjectDescription(), infoGroupProperties
			.getShiftType());

	return findEditionErrors(grouping);
    }

    private List findEditionErrors(final Grouping grouping) {

	List<Integer> errors = new ArrayList<Integer>();

	Integer groupMaximumError = testGroupMaximumNumber(grouping);
	if (groupMaximumError != 0) {
	    errors.add((groupMaximumError));
	}
	errors.addAll(testMaximumAndMininumCapacity(grouping));

	return errors;
    }

    private List<Integer> testMaximumAndMininumCapacity(final Grouping grouping) {
	Integer[] errors = { 0, 0 };
	List<Integer> result = new ArrayList();

	if (grouping.getMaximumCapacity() == null && grouping.getMinimumCapacity() == null) {
	    return result;
	}
	for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
	    if (grouping.getMaximumCapacity() != null && studentGroup.getAttendsCount() > grouping.getMaximumCapacity()) {
		errors[0] = -2;
	    }
	    if (grouping.getMinimumCapacity() != null && studentGroup.getAttendsCount() < grouping.getMinimumCapacity()) {
		errors[1] = -3;
	    }
	    if (errors[0] != 0 && errors[1] != 0) {
		break;
	    }
	}
	if (errors[0] != 0) {
	    result.add(errors[0]);
	}
	if (errors[1] != 0) {
	    result.add(errors[1]);
	}
	return result;
    }

    private Integer testGroupMaximumNumber(final Grouping grouping) {
	if (grouping.getGroupMaximumNumber() != null) {
	    for (final StudentGroup studentGroup : grouping.getStudentGroups()) {
		if (studentGroup.getShift() != null) {
		    if (studentGroup.getShift().getAssociatedStudentGroupsCount() > grouping.getGroupMaximumNumber()) {
			return -1;
		    }
		} else if (grouping.getStudentGroupsCount() > grouping.getGroupMaximumNumber()) {
		    return -1;
		}
	    }
	}
	return 0;
    }
}