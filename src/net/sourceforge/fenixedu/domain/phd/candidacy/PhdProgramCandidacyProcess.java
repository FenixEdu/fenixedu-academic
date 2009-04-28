package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {
    
    public  PhdProgramCandidacyProcess() {
        super();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getDisplayName() {
	// TODO Auto-generated method stub
	return null;
    }
    
}
