package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;

public class PhdProgramCandidacyProcessStateBean implements Serializable {

	private static final long serialVersionUID = 4435060583355083376L;

	private PhdProgramCandidacyProcessState state;
	private String remarks;
	private Boolean generateAlert;

	public PhdProgramCandidacyProcessStateBean() {
		super();
	}

	public PhdProgramCandidacyProcessStateBean(final PhdIndividualProgramProcess process) {
		this();
		setGenerateAlert(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
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

	public Boolean getGenerateAlert() {
		return generateAlert;
	}

	public void setGenerateAlert(Boolean generateAlert) {
		this.generateAlert = generateAlert;
	}
}
