package net.sourceforge.fenixedu.domain.phd;

import java.util.Collection;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.exceptions.PhdDomainOperationException;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessStateBean;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

abstract public class PhdProcessState extends PhdProcessState_Base {

    static final public Comparator<PhdProcessState> COMPARATOR_BY_DATE = new Comparator<PhdProcessState>() {
	@Override
	public int compare(PhdProcessState o1, PhdProcessState o2) {
	    int result = o1.getWhenCreated().compareTo(o2.getWhenCreated());
	    return result != 0 ? result : o1.getIdInternal().compareTo(o2.getIdInternal());
	}
    };

    protected PhdProcessState() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    protected void init(final Person person, final String remarks, final DateTime stateDate) {
	check(person, "error.PhdProcessState.invalid.person");
	check(stateDate, "error.PhdProcessState.invalid.stateDate");

	checkStateDate(stateDate);

	setPerson(person);
	setRemarks(remarks);
	setStateDate(stateDate);
    }

    private void checkStateDate(DateTime stateDate) {
	Collection<? extends PhdProcessState> orderedStates = getProcess().getOrderedStates();

	for (PhdProcessState phdProcessState : orderedStates) {
	    if (phdProcessState == this) {
		continue;
	    }

	    if (phdProcessState.getStateDate() != null && phdProcessState.getStateDate().isAfter(stateDate)) {
		throw new PhdDomainOperationException("error.PhdProcessState.state.date.is.previous.of.actual.state.on.process");
	    }
	}
    }

    public void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	removePerson();
	removeRootDomainObject();
    }

    abstract public PhdProcessStateType getType();

    abstract public boolean isLast();

    public abstract PhdProgramProcess getProcess();

    @Service
    public void editStateDate(PhdProcessStateBean bean) {
	if (bean.getStateDate() == null) {
	    throw new PhdDomainOperationException("error.PhdProcessState.state.date.required");
	}

	setStateDate(bean.getStateDate());
    }
}
