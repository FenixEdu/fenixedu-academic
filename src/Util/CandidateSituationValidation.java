/*
 * CandidateSituationValidation.java
 *
 * Created on 08 of February of 2003, 11:09
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class CandidateSituationValidation {

	public static final int INACTIVE = 0;
    public static final int ACTIVE = 1;
	
	public static final String INACTIVE_STRING = "Inactiva";
	public static final String ACTIVE_STRING = "Activa";
    
	private Integer candidateSituationValidation;

	/** Creates a new instance of CandidateSituationValidation */
	public CandidateSituationValidation() {
	}
    
	public CandidateSituationValidation(int validation) {
		this.candidateSituationValidation = new Integer(validation);
	}

	public CandidateSituationValidation(Integer validation) {
		this.candidateSituationValidation = validation;
	}

	public CandidateSituationValidation(String validation) {
		if (validation.equals(CandidateSituationValidation.ACTIVE_STRING)) this.candidateSituationValidation = new Integer(CandidateSituationValidation.ACTIVE);
		if (validation.equals(CandidateSituationValidation.INACTIVE_STRING)) this.candidateSituationValidation = new Integer(CandidateSituationValidation.INACTIVE);
	}

	public boolean equals(Object o) {
		if(o instanceof CandidateSituationValidation) {
			CandidateSituationValidation aux = (CandidateSituationValidation) o;
			return this.candidateSituationValidation.equals(aux.getCandidateSituationValidation());
		}
		else {
			return false;
		}
	}

	public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(CandidateSituationValidation.ACTIVE_STRING, CandidateSituationValidation.ACTIVE_STRING));
		result.add(new LabelValueBean(CandidateSituationValidation.INACTIVE_STRING, CandidateSituationValidation.INACTIVE_STRING));
		return result;	
	}
    
	public String toString() {
		if (candidateSituationValidation.intValue()== CandidateSituationValidation.ACTIVE) return CandidateSituationValidation.ACTIVE_STRING;
		if (candidateSituationValidation.intValue()== CandidateSituationValidation.INACTIVE) return CandidateSituationValidation.INACTIVE_STRING;
		return "ERROR!"; 
	}




	/**
	 * Returns the candidateSituationValidation.
	 * @return Integer
	 */
	public Integer getCandidateSituationValidation() {
		return candidateSituationValidation;
	}

	/**
	 * Sets the candidateSituationValidation.
	 * @param candidateSituationValidation The candidateSituationValidation to set
	 */
	public void setCandidateSituationValidation(Integer candidateSituationValidation) {
		this.candidateSituationValidation = candidateSituationValidation;
	}

}
