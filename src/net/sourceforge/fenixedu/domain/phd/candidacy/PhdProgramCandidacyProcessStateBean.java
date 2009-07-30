package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;

public class PhdProgramCandidacyProcessStateBean implements Serializable {

    private static final long serialVersionUID = 4435060583355083376L;

    private PhdProgramCandidacyProcessState state;
    private String remarks;

    public PhdProgramCandidacyProcessStateBean() {
	super();
    }

    public PhdProgramCandidacyProcessState getState() {
	return state;
    }

    public void setState(PhdProgramCandidacyProcessState state) {
	this.state = state;
    }

    public String getRemarks() {
	return remarks;
    }

    public void setRemarks(String remarks) {
	this.remarks = remarks;
    }

}
