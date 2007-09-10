package net.sourceforge.fenixedu.domain.elections;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public abstract class DelegateElectionPeriod extends DelegateElectionPeriod_Base {
    
    public  DelegateElectionPeriod() {
    	setRootDomainObject(RootDomainObject.getInstance());
    }
    
    @Override
    public void setEndDate(YearMonthDay endDate) {
    	if(getStartDate() != null && endDate.isBefore(getStartDate()))
    		throw new DomainException("error.elections.create.invalidEndDate");
    	
    	super.setEndDate(endDate);
    }
    
    public boolean isCurrentPeriod() {
    	YearMonthDay currentDate = new YearMonthDay();
    	return (!currentDate.isBefore(getStartDate()) && !currentDate.isAfter(getEndDate()) ? true : false );
    }
    
    public boolean isPastPeriod() {
		YearMonthDay currentDate = new YearMonthDay();
		return (currentDate.isAfter(this.getEndDate()) ? true : false);
	}
	
	public boolean endsBefore(DelegateElectionPeriod electionPeriod) {
		if(this.getEndDate().isBefore(electionPeriod.getStartDate()))
			return true;
		return false;
	}
	
	public boolean isCandidacyPeriod() {
		return (this instanceof DelegateElectionCandidacyPeriod);
	}
	
	public boolean isVotingPeriod() {
		return (this instanceof DelegateElectionVotingPeriod);
	}
	
	public String getPeriod() {
		return getStartDate().toString("dd/MM/yyyy") + " - " + getEndDate().toString("dd/MM/yyyy"); 
	}
    
	public void delete(){
		super.removeRootDomainObject();
		super.deleteDomainObject();
	}
}
