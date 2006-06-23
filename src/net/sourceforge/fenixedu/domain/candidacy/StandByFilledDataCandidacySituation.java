package net.sourceforge.fenixedu.domain.candidacy;

public class StandByFilledDataCandidacySituation extends StandByFilledDataCandidacySituation_Base {
    
    public  StandByFilledDataCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
    }

	@Override
	public void checkConditionsToForward() {
		
	}

	@Override
	public void nextState() {
		CandidacySituation candidacySituation = new StandByConfirmedDataCandidacySituation(getCandidacy());
	}
    
    @Override
    public boolean canChangePersonalData() {
        return true;
    }
    
}
