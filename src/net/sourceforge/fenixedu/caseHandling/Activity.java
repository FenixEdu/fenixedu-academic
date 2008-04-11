package net.sourceforge.fenixedu.caseHandling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.applicationTier.IUserView;

public abstract class Activity {

    private Collection<PreCondition> preConditions = new ArrayList<PreCondition>();

    private Process process;

    public Activity(Process candidacyProcess) {
	this.process = candidacyProcess;
    }

    public PreConditionResult checkPreConditions(IUserView userView) {
	Collection<String> errors = new HashSet<String>();

	for (final PreCondition preCondition : preConditions) {
	    PreConditionResult result = preCondition.check(userView, this);
	    if (result.isChecked()) {
		return result;
	    } else if (result.isNotChecked()) {
		errors.add(result.getResultMessage());
	    }
	}

	if (!errors.isEmpty()) {
	    return PreConditionResult.notChecked(this, errors);
	}

	return PreConditionResult.groupNotChecked(this);
    }

    abstract public void execute();

    protected void addPreCondition(PreCondition preCondition) {
	this.preConditions.add(preCondition);
    }

    public Process getProcess() {
	return process;
    }
}
