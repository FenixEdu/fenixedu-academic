package DataBeans;

import java.util.Date;
import java.util.List;

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
	private List situationList = null;
    	
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
		situationList = null;
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
	 * @return
	 */
	public Double getAverage() {
		return average;
	}

	/**
	 * @return
	 */
	public Integer getCandidateNumber() {
		return candidateNumber;
	}

	/**
	 * @return
	 */
	public InfoCandidateSituation getInfoCandidateSituation() {
		return infoCandidateSituation;
	}

	/**
	 * @return
	 */
	public InfoExecutionDegree getInfoExecutionDegree() {
		return infoExecutionDegree;
	}

	/**
	 * @return
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}

	/**
	 * @return
	 */
	public String getMajorDegree() {
		return majorDegree;
	}

	/**
	 * @return
	 */
	public String getMajorDegreeSchool() {
		return majorDegreeSchool;
	}

	/**
	 * @return
	 */
	public Integer getMajorDegreeYear() {
		return majorDegreeYear;
	}

	/**
	 * @return
	 */
	public List getSituationList() {
		return situationList;
	}

	/**
	 * @return
	 */
	public String getSpecialization() {
		return specialization;
	}

	/**
	 * @param double1
	 */
	public void setAverage(Double double1) {
		average = double1;
	}

	/**
	 * @param integer
	 */
	public void setCandidateNumber(Integer integer) {
		candidateNumber = integer;
	}

	/**
	 * @param situation
	 */
	public void setInfoCandidateSituation(InfoCandidateSituation situation) {
		infoCandidateSituation = situation;
	}

	/**
	 * @param degree
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree degree) {
		infoExecutionDegree = degree;
	}

	/**
	 * @param person
	 */
	public void setInfoPerson(InfoPerson person) {
		infoPerson = person;
	}

	/**
	 * @param string
	 */
	public void setMajorDegree(String string) {
		majorDegree = string;
	}

	/**
	 * @param string
	 */
	public void setMajorDegreeSchool(String string) {
		majorDegreeSchool = string;
	}

	/**
	 * @param integer
	 */
	public void setMajorDegreeYear(Integer integer) {
		majorDegreeYear = integer;
	}

	/**
	 * @param list
	 */
	public void setSituationList(List list) {
		situationList = list;
	}

	/**
	 * @param string
	 */
	public void setSpecialization(String string) {
		specialization = string;
	}

}