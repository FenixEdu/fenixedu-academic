package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

import org.joda.time.DateTime;

public class StudyPlanConcludedState extends StudyPlanConcludedState_Base {

    public StudyPlanConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
	registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
	if (registration.getStudent().getRegistrationsCount() == 1) {
	    registration.getPerson().removeRoleByType(RoleType.STUDENT);
	}
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.STUDYPLANCONCLUDED;
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.studyPlanConcluded");

    }

    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.studyPlanConcluded");

    }

    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    public IState nextState() {
	throw new DomainException("error.impossible.to.forward.from.studyPlanConcluded");
    }

}
