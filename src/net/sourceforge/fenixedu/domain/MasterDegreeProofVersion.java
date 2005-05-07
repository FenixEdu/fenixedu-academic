/*
 * Created on Oct 10, 2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersion extends MasterDegreeProofVersion_Base {

	// fields
	private Timestamp lastModification;

	private Date proofDate;

	private Date thesisDeliveryDate;

	private MasterDegreeClassification finalResult;

	private State currentState;

	/**
	 * Default Constructor
	 */
	public MasterDegreeProofVersion() {

	}

	/**
	 * @param masterDegreeThesis
	 * @param responsibleEmployee
	 * @param lastModification
	 * @param proofDate
	 * @param thesisDeliveryDate
	 * @param finalResult
	 * @param attachedCopiesNumber
	 * @param currentState
	 */
	public MasterDegreeProofVersion(IMasterDegreeThesis masterDegreeThesis,
			IEmployee responsibleEmployee, Timestamp lastModification,
			Date proofDate, Date thesisDeliveryDate,
			MasterDegreeClassification finalResult,
			Integer attachedCopiesNumber, State currentState, List juries,
			List externalJuries) {
		setMasterDegreeThesis(masterDegreeThesis);
		setResponsibleEmployee(responsibleEmployee);
		this.lastModification = lastModification;
		this.proofDate = proofDate;
		this.thesisDeliveryDate = thesisDeliveryDate;
		this.finalResult = finalResult;
		setAttachedCopiesNumber(attachedCopiesNumber);
		this.currentState = currentState;
		setJuries(juries);
		setExternalJuries(externalJuries);
	}

	public void setLastModification(Timestamp lastModification) {
		this.lastModification = lastModification;
	}

	public Timestamp getLastModification() {
		return lastModification;
	}

	public void setProofDate(Date proofDate) {
		this.proofDate = proofDate;
	}

	public Date getProofDate() {
		return proofDate;
	}

	public void setThesisDeliveryDate(Date thesisDeliveryDate) {
		this.thesisDeliveryDate = thesisDeliveryDate;
	}

	public Date getThesisDeliveryDate() {
		return thesisDeliveryDate;
	}

	public void setFinalResult(MasterDegreeClassification finalResult) {
		this.finalResult = finalResult;
	}

	public MasterDegreeClassification getFinalResult() {
		return finalResult;
	}

	public void setCurrentState(State state) {
		this.currentState = state;
	}

	public State getCurrentState() {
		return currentState;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "masterDegreeThesis = "
				+ getMasterDegreeThesis().getIdInternal() + "; \n";
		result += "responsibleEmployee = "
				+ getResponsibleEmployee().getIdInternal() + "; \n";
		result += "lastModification = " + this.lastModification.toString()
				+ "; \n";
		result += "proofDate = " + this.proofDate.toString() + "; \n";
		result += "thesisDeliveryDate = " + this.thesisDeliveryDate.toString()
				+ "; \n";
		result += "finalResult = " + this.finalResult.toString() + "; \n";
		result += "attachedCopiesNumber = "
				+ getAttachedCopiesNumber().toString() + "; \n";
		result += "currentState = " + this.currentState.toString() + "; \n";
		result += "] \n";

		return result;
	}

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IMasterDegreeProofVersion) {
			IMasterDegreeProofVersion masterDegreeProofVersion = (IMasterDegreeProofVersion) obj;
			result = getMasterDegreeThesis().equals(
					masterDegreeProofVersion.getMasterDegreeThesis())
					&& this.lastModification.equals(masterDegreeProofVersion
							.getLastModification());
		}

		return result;
	}
}