package net.sourceforge.fenixedu.domain.elections;

import org.joda.time.YearMonthDay;

public class DelegateElectionVotingPeriod extends DelegateElectionVotingPeriod_Base {
    
    private DelegateElectionVotingPeriod() {
        super();
    }
    
    public DelegateElectionVotingPeriod(YearMonthDay startDate, YearMonthDay endDate) {
    	this();
    	setStartDate(startDate);
    	setEndDate(endDate);
    }
	
	@Override
	public void delete() {
		removeDelegateElection();
		super.delete();
	}
}
