/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.List;

import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends
		MasterDegreeThesisDataVersion_Base {

	// fields
	private List externalAssistentGuiders;

	private Timestamp lastModification;

	private State currentState;

	public MasterDegreeThesisDataVersion() {
	}

	public MasterDegreeThesisDataVersion(
			IMasterDegreeThesis masterDegreeThesis,
			IEmployee responsibleEmployee, String dissertationTitle,
			Timestamp lastModification, State currentState) {
		setMasterDegreeThesis(masterDegreeThesis);
		setResponsibleEmployee(responsibleEmployee);
		setDissertationTitle(dissertationTitle);
		this.lastModification = lastModification;
		this.currentState = currentState;
	}

	/**
	 * @param masterDegreeThesis
	 * @param externalAssistentGuiders
	 * @param assistentGuiders
	 * @param guiders
	 * @param responsibleEmployee
	 * @param dissertationTitle
	 * @param lastModification
	 * @param currentState
	 */
	public MasterDegreeThesisDataVersion(
			IMasterDegreeThesis masterDegreeThesis,
			List externalAssistentGuiders, List assistentGuiders, List guiders,
			IEmployee responsibleEmployee, String dissertationTitle,
			Timestamp lastModification, State currentState) {
		setMasterDegreeThesis(masterDegreeThesis);
		this.externalAssistentGuiders = externalAssistentGuiders;
		setAssistentGuiders(assistentGuiders);
		setGuiders(guiders);
		setResponsibleEmployee(responsibleEmployee);
		setDissertationTitle(dissertationTitle);
		this.lastModification = lastModification;
		this.currentState = currentState;
	}

	public void setExternalAssistentGuiders(List externalAssistentGuiders) {
		this.externalAssistentGuiders = externalAssistentGuiders;
	}

	public List getExternalAssistentGuiders() {
		return externalAssistentGuiders;
	}

	public void setLastModification(Timestamp lastModification) {
		this.lastModification = lastModification;
	}

	public Timestamp getLastModification() {
		return lastModification;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getCurrentState() {
		return currentState;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "masterDegreeThesis = "
				+ getMasterDegreeThesis().getIdInternal() + "; \n";
		result += "externalAssistentGuiders = "
				+ this.externalAssistentGuiders.toString() + "; \n";
		result += "assistentGuiders = " + getAssistentGuiders().toString()
				+ "; \n";
		result += "guiders = " + getGuiders().toString() + "; \n";
		result += "responsibleEmployee = "
				+ getResponsibleEmployee().getIdInternal() + "; \n";
		result += "dissertationTitle = " + getDissertationTitle().toString()
				+ "; \n";
		result += "lastModification = " + this.lastModification.toString()
				+ "; \n";
		result += "currentState = " + this.currentState.toString() + "; \n";
		result += "] \n";

		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IMasterDegreeThesisDataVersion) {
			IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) obj;
			result = getMasterDegreeThesis().equals(
					masterDegreeThesisDataVersion.getMasterDegreeThesis())
					&& this.lastModification
							.equals(masterDegreeThesisDataVersion
									.getLastModification());
		}

		return result;
	}

}