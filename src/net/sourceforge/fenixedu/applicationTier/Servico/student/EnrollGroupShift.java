/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author joaosa and rmalo
 * 
 */

public class EnrollGroupShift extends FenixService {

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static Boolean run(Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode, String username)
			throws FenixServiceException {

		Grouping groupProperties = rootDomainObject.readGroupingByOID(groupPropertiesCode);
		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupCode);
		if (studentGroup == null) {
			throw new InvalidArgumentsServiceException();
		}

		Shift shift = rootDomainObject.readShiftByOID(newShiftCode);
		if (groupProperties.getShiftType() == null || studentGroup.getShift() != null
				|| (!shift.containsType(groupProperties.getShiftType()))) {
			throw new InvalidStudentNumberServiceException();
		}

		Registration registration = Registration.readByUsername(username);

		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

		if (!strategy.checkStudentInGrouping(groupProperties, username)) {
			throw new NotAuthorizedException();
		}

		if (!checkStudentInStudentGroup(registration, studentGroup)) {
			throw new InvalidSituationServiceException();
		}

		boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
		if (!result) {
			throw new InvalidChangeServiceException();
		}
		studentGroup.setShift(shift);
		return true;
	}

	private static boolean checkStudentInStudentGroup(Registration registration, StudentGroup studentGroup)
			throws FenixServiceException {

		for (final Attends attend : studentGroup.getAttends()) {
			if (attend.getRegistration() == registration) {
				return true;
			}
		}
		return false;
	}
}