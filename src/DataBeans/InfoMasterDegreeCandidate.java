package DataBeans;

import java.util.Date;

/*
 * InfoMasterDegreeCandidate.java
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


public class InfoMasterDegreeCandidate {
 
    private String majorDegree = null;
    private Integer candidateNumber = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
	private InfoPerson infoPerson = null;
	private InfoExecutionDegree infoExecutionDegree = null;
    private String specialization = null;
	private InfoCandidateSituation infoCandidateSituation = null;

    	
    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public InfoMasterDegreeCandidate() {
        majorDegree = null;
        candidateNumber = null;
        specialization = null;
        majorDegreeSchool = null;
        majorDegreeYear = null;
        average = null;
		infoCandidateSituation = null;
		infoExecutionDegree = null;
		infoPerson = null;
    } // Fim do Construtor Sem Argumentos
    
    /**
     * @deprecated
     */
    public InfoMasterDegreeCandidate(String name, String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict, 
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType, 
			String maritalStatus, InfoCountry country, InfoCountry nationality, String specialization, 
			Integer candidateNumber, Double average, Date birth, Date identificationDocumentIssueDate, 
			Date identificationDocumentExpirationDate
    		) {
	    
    }
        
	public InfoMasterDegreeCandidate(InfoPerson person, InfoExecutionDegree executionDegree, Integer candidateNumber, String specialization, 
			   String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, Double average){
				this.infoPerson = person;
				this.infoExecutionDegree = executionDegree;
				this.candidateNumber = candidateNumber;
				this.specialization = specialization;
				this.majorDegree = majorDegree;
				this.majorDegreeSchool = majorDegreeSchool;
				this.majorDegreeYear = majorDegreeYear;
				this.average = average;
    	   	
		}
		
		
    public boolean equals(Object o) {
			return
			((o instanceof InfoMasterDegreeCandidate) &&
		
			((this.infoPerson.equals(((InfoMasterDegreeCandidate)o).getInfoPerson())) &&
			 (this.specialization.equals(((InfoMasterDegreeCandidate)o).getSpecialization())) &&
			 (this.infoExecutionDegree.equals(((InfoMasterDegreeCandidate)o).getInfoExecutionDegree()))) ||
		 
			((this.infoExecutionDegree.equals(((InfoMasterDegreeCandidate)o).getInfoExecutionDegree())) &&
			 (this.candidateNumber.equals(((InfoMasterDegreeCandidate)o).getCandidateNumber())) &&
			 (this.specialization.equals(((InfoMasterDegreeCandidate)o).getSpecialization()))));       
        
		}


	public String toString() {
		   String result = "Master Degree Candidate :\n";
		   result += "\n  - Person : " + infoPerson;
		   result += "\n  - Major Degree : " + majorDegree;
		   result += "\n  - Candidate Number : " + candidateNumber;
		   result += "\n  - Specialization : " + specialization;
		   result += "\n  - Major Degree School : " + majorDegreeSchool;
		   result += "\n  - Major Degree Year : " + majorDegreeYear;
		   result += "\n  - Major Degree Average : " + average;
		   result += "\n  - Master Degree : " + infoExecutionDegree;

        
		   return result;
	   }  
    
	/**
	 * @return Double
	 */
	public Double getAverage() {
		return average;
	}

/**
 * @return Integer
 */
public Integer getCandidateNumber() {
	return candidateNumber;
}

/**
 * @return InfoCandidateSituation
 */
public InfoCandidateSituation getInfoCandidateSituation() {
	return infoCandidateSituation;
}

	/**
	 * @return InfoExecutionDegree
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @return InfoPerson
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}

/**
 * @return String
 */
public String getMajorDegree() {
	return majorDegree;
}

	/**
	 * @return String
	 */
	public String getMajorDegreeSchool() {
		return majorDegreeSchool;
	}

	/**
	 * @return Integer
	 */
	public Integer getMajorDegreeYear() {
		return majorDegreeYear;
	}

/**
 * @return String
 */
public String getSpecialization() {
	return specialization;
}

	/**
	 * Sets the average.
	 * @param average The average to set
	 */
	public void setAverage(Double average) {
		this.average = average;
	}

/**
 * Sets the candidateNumber.
 * @param candidateNumber The candidateNumber to set
 */
public void setCandidateNumber(Integer candidateNumber) {
	this.candidateNumber = candidateNumber;
}

/**
 * Sets the infoCandidateSituation.
 * @param infoCandidateSituation The infoCandidateSituation to set
 */
public void setInfoCandidateSituation(InfoCandidateSituation infoCandidateSituation) {
	this.infoCandidateSituation = infoCandidateSituation;
}

	/**
	 * Sets the infoExecutionDegree.
	 * @param infoExecutionDegree The infoExecutionDegree to set
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree) {
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * Sets the infoPerson.
	 * @param infoPerson The infoPerson to set
	 */
	public void setInfoPerson(InfoPerson infoPerson) {
		this.infoPerson = infoPerson;
	}

/**
 * Sets the majorDegree.
 * @param majorDegree The majorDegree to set
 */
public void setMajorDegree(String majorDegree) {
	this.majorDegree = majorDegree;
}

	/**
	 * Sets the majorDegreeSchool.
	 * @param majorDegreeSchool The majorDegreeSchool to set
	 */
	public void setMajorDegreeSchool(String majorDegreeSchool) {
		this.majorDegreeSchool = majorDegreeSchool;
	}

	/**
	 * Sets the majorDegreeYear.
	 * @param majorDegreeYear The majorDegreeYear to set
	 */
	public void setMajorDegreeYear(Integer majorDegreeYear) {
		this.majorDegreeYear = majorDegreeYear;
	}

/**
 * Sets the specialization.
 * @param specialization The specialization to set
 */
public void setSpecialization(String specialization) {
	this.specialization = specialization;
}

	}
