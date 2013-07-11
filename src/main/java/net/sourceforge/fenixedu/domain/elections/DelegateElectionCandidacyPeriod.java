package net.sourceforge.fenixedu.domain.elections;

import org.joda.time.YearMonthDay;

public class DelegateElectionCandidacyPeriod extends DelegateElectionCandidacyPeriod_Base {

    private DelegateElectionCandidacyPeriod() {
        super();
    }

    public DelegateElectionCandidacyPeriod(YearMonthDay startDate, YearMonthDay endDate) {
        this();
        setStartDate(startDate);
        setEndDate(endDate);
    }

    @Override
    public void delete() {
        setDelegateElection(null);
        super.delete();
    }

    @Override
    public boolean isSecondRoundElections() {
        return false;
    }

    @Override
    public boolean isFirstRoundElections() {
        return false;
    }

    @Override
    public boolean isOpenRoundElections() {
        return false;
    }

}
