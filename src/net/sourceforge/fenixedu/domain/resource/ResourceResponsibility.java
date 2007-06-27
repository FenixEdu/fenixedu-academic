package net.sourceforge.fenixedu.domain.resource;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public abstract class ResourceResponsibility extends ResourceResponsibility_Base {
    
    protected ResourceResponsibility() {
        super();
        setOjbConcreteClass(getClass().getName());
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
    
    @Override
    public void setBegin(YearMonthDay beginDate) {
	if (beginDate == null || (getEnd() != null && getEnd().isBefore(beginDate))) {
	    throw new DomainException("error.ResourceResponsibility.empty.beginDate");
	}
	super.setBegin(beginDate);
    }

    @Override
    public void setEnd(YearMonthDay endDate) {
	if (getBegin() == null || (endDate != null && endDate.isBefore(getBegin()))) {
	    throw new DomainException("error.ResourceResponsibility.endDate.before.beginDate");
	}
	super.setEnd(endDate);
    }
    
    public boolean isSpaceResponsibility() {
	return false;
    }
    
    public boolean isMaterialResponsibility() {
	return false;
    }
    
    @Override
    public void setResource(Resource resource) {
        if(resource == null) {
            throw new DomainException("error.ResourceResponsibility.empty.resource");
        }
	super.setResource(resource);
    }
    
    @Override
    public void setParty(Party party) {
	if(party == null) {
            throw new DomainException("error.ResourceResponsibility.empty.party");
        }
	super.setParty(party);
    }
}
