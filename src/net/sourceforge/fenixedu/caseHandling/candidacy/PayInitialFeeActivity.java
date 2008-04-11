package net.sourceforge.fenixedu.caseHandling.candidacy;

import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreCondition;

public class PayInitialFeeActivity extends Activity {

    public PayInitialFeeActivity(CandidacyProcess candidacyProcess) {
	super(candidacyProcess);

	addPreCondition(new PreCondition(null, "") {
	    public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	    }
	});

	addPreCondition(new PreCondition(null, "") {
	    public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	    }
	});

    }

    @Override
    public void execute() {
	
	// TODO Auto-generated method stub

    }

}
