package DataBeans;

import java.util.Date;

import Dominio.ICandidateSituation;
import Util.SituationName;
import Util.State;

/*
 * InfoCandidateSituation.java
 *
 * Created on 29 de Novembro de 2002, 15:57
 */ 

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */


public class InfoCandidateSituation extends InfoObject {
    
    private Date date = null;                // Situation Date
    private String remarks = null; 	 		 // Remarks
    private SituationName situation = null;         // Situation
	private State validation = null; 
  
    public InfoCandidateSituation() {
        situation = null;
        date = null;
        remarks = null;
        validation = null;
    } 
    
    public InfoCandidateSituation(Date date, String remarks, String situation) {
        setSituation(new SituationName(situation));
        setDate(date);
        setRemarks(remarks);
    } 

    public InfoCandidateSituation(ICandidateSituation candidateSituation) {
        setSituation(candidateSituation.getSituation());
        setDate(candidateSituation.getDate());
        setRemarks(candidateSituation.getRemarks());
    } 
    
    public String toString() {
        String result = "DataBean Situacao do Candidato:\n";
        result += "\n  - Data : " + date;
        result += "\n  - Observacoes : " + remarks;
        result += "\n  - Situacao : " + situation;
        
        return result;
    }


	
	/**
	 * Returns the date.
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the remarks.
	 * @return String
	 */
	public String getRemarks() {
		return remarks;
	}

	public SituationName getSituation() {
		return situation;
	}

	/**
	 * Sets the date.
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Sets the remarks.
	 * @param remarks The remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Sets the situation.
	 * @param situation The situation to set
	 */
	public void setSituation(SituationName situation) {
		this.situation = situation;
	}

	/**
	 * @return
	 */
	public State getValidation() {
		return validation;
	}

	/**
	 * @param validation
	 */
	public void setValidation(State validation) {
		this.validation = validation;
	}

} 