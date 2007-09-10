package net.sourceforge.fenixedu.domain.elections;

import org.joda.time.YearMonthDay;

public class DelegateElectionCandidacyPeriod extends DelegateElectionCandidacyPeriod_Base {
    
    private DelegateElectionCandidacyPeriod() {
        super();
    }
    
    public DelegateElectionCandidacyPeriod (YearMonthDay startDate, YearMonthDay endDate) {
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
