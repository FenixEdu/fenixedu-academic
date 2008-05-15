package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

import org.joda.time.DateTime;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ConcludedState extends ConcludedState_Base {

    public ConcludedState(Registration registration, Person person, DateTime dateTime) {
	super();

	if (!registration.hasConcluded()) {
	    throw new DomainException("error.registration.is.not.concluded");
	}

	init(registration, person, dateTime);
	registration.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);
	if (registration.getStudent().getRegistrationsCount() == 1) {
	    registration.getPerson().removeRoleByType(RoleType.STUDENT);
	}
    }

    @Override
    public void delete() {
	if (!getRegistration().getSucessfullyFinishedDocumentRequests(DocumentRequestType.DEGREE_FINALIZATION_CERTIFICATE)
		.isEmpty()) {
	    throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.degree.finalization.request");
	}

	if (!getRegistration().getSucessfullyFinishedDocumentRequests(DocumentRequestType.DIPLOMA_REQUEST).isEmpty()) {
	    throw new DomainException("cannot.delete.concluded.state.of.registration.with.concluded.diploma.request");
	}

	if (!getRegistration().isBolonha()) {
	    getRegistration().setFinalAverage(null);
	    getRegistration().setConclusionDate(null);
	} else {
	    getRegistration().getLastStudentCurricularPlan().getLastOrderedCycleCurriculumGroup().removeConcludedInformation();
	}
	super.delete();
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    public IState nextState() {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public IState nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.concluded");
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.CONCLUDED;
    }

}
