package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

abstract public class PhdProcessState extends PhdProcessState_Base {

    static final public Comparator<PhdProcessState> COMPARATOR_BY_DATE = new Comparator<PhdProcessState>() {
	@Override
	public int compare(PhdProcessState o1, PhdProcessState o2) {
	    return o1.getWhenCreated().compareTo(o2.getWhenCreated());
	}
    };

    protected PhdProcessState() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setWhenCreated(new DateTime());
    }

    protected void init(final Person person, final String remarks) {
	check(person, "error.PhdProcessState.invalid.person");
	setPerson(person);
	setRemarks(remarks);
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
}
