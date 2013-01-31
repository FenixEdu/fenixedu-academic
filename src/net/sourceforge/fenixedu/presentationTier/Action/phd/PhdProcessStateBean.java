package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.phd.PhdProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;

import org.joda.time.DateTime;

public class PhdProcessStateBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private DateTime stateDate;
	private PhdProcessState state;

	public PhdProcessStateBean(final PhdProcessState state) {
		this.state = state;
		this.stateDate = state.getStateDate();
	}

	public DateTime getStateDate() {
		return stateDate;
	}

	public void setStateDate(DateTime stateDate) {
		this.stateDate = stateDate;
	}

	public PhdProcessState getState() {
		return state;
	}

	public PhdProgramProcess getPhdProcess() {
		return state.getProcess();
	}
}
