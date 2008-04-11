package net.sourceforge.fenixedu.caseHandling.candidacy;

import net.sourceforge.fenixedu.caseHandling.Process;

public class CandidacyProcess extends Process {

    public CandidacyProcess() {
	addActivity(new PayInitialFeeActivity(this));
	addActivity(new RegisterCandidacyActivity(this));
    }

}
