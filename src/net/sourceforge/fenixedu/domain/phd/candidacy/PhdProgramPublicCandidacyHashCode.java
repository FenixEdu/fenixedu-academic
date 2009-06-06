package net.sourceforge.fenixedu.domain.phd.candidacy;

public class PhdProgramPublicCandidacyHashCode extends PhdProgramPublicCandidacyHashCode_Base {
    
    private PhdProgramPublicCandidacyHashCode() {
        super();
    }

    @Override
    public boolean hasCandidacyProcess() {
	return hasPhdProgramCandidacyProcess();
    }
    
}
