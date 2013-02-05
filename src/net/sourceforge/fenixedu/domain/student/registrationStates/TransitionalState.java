package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class TransitionalState extends TransitionalState_Base {

    private TransitionalState() {
        super();
    }

    protected TransitionalState(final Registration registration, final Person responsiblePerson, final DateTime stateDate) {
        this();
        init(registration, responsiblePerson, stateDate);
    }

    @Override
    public RegistrationStateType getStateType() {
        return RegistrationStateType.TRANSITION;
    }

    @Override
    public Set<String> getValidNextStates() {
        final Set<String> result = new HashSet<String>();
        result.add(RegistrationStateType.TRANSITED.name());
        result.add(RegistrationStateType.CANCELED.name());
        result.add(RegistrationStateType.REGISTERED.name());
        result.add(RegistrationStateType.CONCLUDED.name());

        return result;
    }

}
