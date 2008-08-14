package net.sourceforge.fenixedu.domain.resource;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.joda.time.YearMonthDay;

public abstract class ResourceResponsibility extends ResourceResponsibility_Base {

    protected ResourceResponsibility() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public void delete() {
	super.setParty(null);
	super.setResource(null);
	super.setRootDomainObject(null);
	deleteDomainObject();
    }

    public boolean isActive(YearMonthDay currentDate) {
	return (!getBegin().isAfter(currentDate) && (getEnd() == null || !getEnd().isBefore(currentDate)));
    }

    public boolean isSpaceResponsibility() {
	return false;
    }

    public boolean isMaterialResponsibility() {
	return false;
    }

    @Override
    public void setResource(Resource resource) {
	if (resource == null) {
	    throw new DomainException("error.ResourceResponsibility.empty.resource");
	}
	super.setResource(resource);
    }

    @Override
    public void setParty(Party party) {
	if (party == null) {
	    throw new DomainException("error.ResourceResponsibility.empty.party");
	}
	super.setParty(party);
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkDateInterval() {
	final YearMonthDay start = getBegin();
	final YearMonthDay end = getEnd();
	return start != null && (end == null || !start.isAfter(end));
    }
}
