package net.sourceforge.fenixedu.domain.student;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class RegistrationRegime extends RegistrationRegime_Base {

    static public Comparator<RegistrationRegime> COMPARATOR_BY_EXECUTION_YEAR = new Comparator<RegistrationRegime>() {
	@Override
	public int compare(RegistrationRegime o1, RegistrationRegime o2) {
	    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
	}
    };

    private RegistrationRegime() {
	super();
	super.setRootDomainObject(RootDomainObject.getInstance());
	super.setWhenCreated(new DateTime());
    }

    public RegistrationRegime(final Registration registration, final ExecutionYear executionYear,
	    final RegistrationRegimeType type) {

	this();
	checkParameters(registration, executionYear, type);

	super.setRegistration(registration);
	super.setExecutionYear(executionYear);
	super.setRegimeType(type);
    }

    private void checkParameters(final Registration registration, final ExecutionYear executionYear,
	    final RegistrationRegimeType type) {
	if (registration == null) {
	    throw new DomainException("error.RegistrationRegime.invalid.registration");
	}
	if (executionYear == null) {
	    throw new DomainException("error.RegistrationRegime.invalid.executionYear");
	}
	if (type == null) {
	    throw new DomainException("error.RegistrationRegime.invalid.regimeType");
	}

	if (registration.hasRegistrationRegime(executionYear, type)) {
	    throw new DomainException("error.RegistrationRegime.already.has.regime.type.in.given.executionYear");
	}
    }

    public boolean isPartTime() {
	return getRegimeType() == RegistrationRegimeType.PART_TIME;
    }

    public void delete() {
	removeRegistration();
	removeExecutionYear();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean isFor(final ExecutionYear executionYear) {
	return getExecutionYear() == executionYear;
    }

    public boolean hasRegime(final RegistrationRegimeType type) {
	return getRegimeType() == type;
    }
}
