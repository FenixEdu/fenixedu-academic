/*
 * Created on 29/Jul/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author asnr and scpo
 * 
 */

public class VerifyStudentGroupAtributes extends Service {

    private boolean checkGroupStudentEnrolment(Integer studentGroupCode, String username) throws FenixServiceException {
	boolean result = false;

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
	if (studentGroup == null) {
	    throw new FenixServiceException();
	}

	Grouping groupProperties = studentGroup.getGrouping();

	IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	if (!strategy.checkStudentInGrouping(groupProperties, username)) {
	    throw new NotAuthorizedException();
	}

	result = strategy.checkAlreadyEnroled(groupProperties, username);
	if (result)
	    throw new InvalidSituationServiceException();

	result = strategy.checkPossibleToEnrolInExistingGroup(groupProperties, studentGroup);
	if (!result)
	    throw new InvalidArgumentsServiceException();

	return true;
    }

    private boolean checkGroupEnrolment(Integer groupPropertiesCode, Integer shiftCode, String username)
	    throws FenixServiceException {
	boolean result = false;
	Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
	if (groupProperties == null) {
	    throw new InvalidChangeServiceException();
	}

	IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	if (!strategy.checkStudentInGrouping(groupProperties, username)) {
	    throw new NotAuthorizedException();
	}

	Shift shift = rootDomainObject.readShiftByOID(shiftCode);
	result = strategy.checkNumberOfGroups(groupProperties, shift);

	if (!result)
	    throw new InvalidArgumentsServiceException();

	result = strategy.checkAlreadyEnroled(groupProperties, username);

	if (result)
	    throw new InvalidSituationServiceException();

	return true;
    }

    private boolean checkUnEnrollStudentInGroup(Integer studentGroupCode, String username) throws FenixServiceException {

	boolean result = false;

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
	if (studentGroup == null) {
	    throw new FenixServiceException();
	}
	Grouping groupProperties = studentGroup.getGrouping();

	GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	if (!strategy.checkStudentInGrouping(groupProperties, username)) {
	    throw new NotAuthorizedException();
	}

	result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
	if (result)
	    throw new InvalidSituationServiceException();

	result = strategy.checkNumberOfGroupElements(groupProperties, studentGroup);
	if (!result)
	    throw new InvalidArgumentsServiceException();

	return true;
    }

    private boolean checkEditStudentGroupShift(Integer studentGroupCode, Integer groupPropertiesCode, String username)
	    throws FenixServiceException {
	boolean result = false;

	Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
	if (groupProperties == null) {
	    throw new ExistingServiceException();
	}

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
	if (studentGroup == null) {
	    throw new FenixServiceException();
	}

	GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	if (!strategy.checkStudentInGrouping(groupProperties, username)) {
	    throw new NotAuthorizedException();
	}

	result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
	if (result)
	    throw new InvalidSituationServiceException();

	if (groupProperties.getShiftType() == null || studentGroup.getShift() == null) {
	    throw new InvalidChangeServiceException();
	}

	return true;
    }

    private boolean checkEnrollStudentGroupShift(Integer studentGroupCode, Integer groupPropertiesCode, String username)
	    throws FenixServiceException {
	boolean result = false;
	Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
	if (groupProperties == null) {
	    throw new ExistingServiceException();
	}

	StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
	if (studentGroup == null) {
	    throw new FenixServiceException();
	}

	GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
	IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

	if (!strategy.checkStudentInGrouping(groupProperties, username)) {
	    throw new NotAuthorizedException();
	}

	result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
	if (result)
	    throw new InvalidSituationServiceException();

	if (groupProperties.getShiftType() == null || studentGroup.getShift() != null) {
	    throw new InvalidChangeServiceException();
	}

	return true;
    }

    /**
     * Executes the service.
     * 
     * @throws ExcepcaoPersistencia
     */

    public boolean run(Integer groupPropertiesCode, Integer shiftCode, Integer studentGroupCode, String username, Integer option)
	    throws FenixServiceException {

	boolean result = false;

	switch (option.intValue()) {

	case 1:
	    result = checkGroupStudentEnrolment(studentGroupCode, username);
	    return result;

	case 2:
	    result = checkGroupEnrolment(groupPropertiesCode, shiftCode, username);
	    return result;

	case 3:
	    result = checkUnEnrollStudentInGroup(studentGroupCode, username);
	    return result;

	case 4:
	    result = checkEditStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
	    return result;

	case 5:
	    result = checkEnrollStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
	    return result;
	}

	return result;
    }

}