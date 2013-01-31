package net.sourceforge.fenixedu.domain.student.registrationStates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class TransitedState extends TransitedState_Base {

	private TransitedState() {
		super();
	}

	protected TransitedState(final Registration registration, final Person responsiblePerson, final DateTime stateDate) {
		this();
		init(registration, responsiblePerson, stateDate);
	}

	@Override
	public RegistrationStateType getStateType() {
		return RegistrationStateType.TRANSITED;
	}

}
