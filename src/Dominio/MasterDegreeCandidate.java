/*
 * MasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 9:53
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package Dominio;


import java.util.Date;
import java.util.Set;

import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;
 

public class MasterDegreeCandidate implements IMasterDegreeCandidate {
 
//    private String identificationDocumentNumber = null;
//    private TipoDocumentoIdentificacao identificationDocumentType;
//    private String identificationDocumentIssuePlace = null;
//	private Date identificationDocumentIssueDate;
//	private Date identificationDocumentExpirationDate;
//    private String name = null;
//    private Sexo sex = null;
//	private EstadoCivil maritalStatus = null;
//    private Date birth;
//    private String fatherName = null;
//    private String motherName = null;
//    private String birthPlaceParish = null;
//    private String birthPlaceMunicipality = null;
//    private String birthPlaceDistrict = null;
//    private String address = null;
//    private String place = null;
//    private String postCode = null;
//    private String addressParish = null;
//    private String addressMunicipality = null;
//    private String addressDistrict = null;
//    private String telephone = null;
//    private String mobilePhone = null;
//    private String email = null;
//    private String webSite = null;
//    private String contributorNumber = null;
//    private String occupation = null;
    private String majorDegree = null;
//    private String username = null;
//    private String password = null;
    private Integer candidateNumber = null;
    private Specialization specialization = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
    
    // Instance from class Degree
    private ICursoExecucao executionDegree = null;

    // Instance from class Country
//    private ICountry country = null;
//	private ICountry nationality = null;

	private IPessoa person;
	// List of Situations
    private Set situations;
 
    
    // Internal Codes from Database
    private Integer internalCode;            // Internal code for Master Degree Candidate
    private Integer executionDegreeKey;               // Foreign Key from table Degree
	private Integer personKey;				// Foreign Key from table Person
//	private Integer countryKey;              // Foreign Key from table Country
//	private Integer nationalityCountryKey;   // Foreign Key from table Country
    
    public MasterDegreeCandidate() {
        majorDegree = null;
        executionDegree = null;
        candidateNumber = null;
        specialization = null;
        majorDegreeSchool = null;
        majorDegreeYear = null;
        average = null;
		situations = null;
		person = null;
    } 
    
    /**
     * @deprecated
     * @param identificationDocumentNumber
     * @param identificationDocumentType
     * @param identificationDocumentIssuePlace
     * @param identificationDocumentIssueDate
     * @param name
     * @param sex
     * @param maritalStatus
     * @param birth
     * @param fatherName
     * @param motherName
     * @param nationality
     * @param birthPlaceParish
     * @param birthPlaceMunicipality
     * @param birthPlaceDistrict
     * @param address
     * @param place
     * @param postCode
     * @param addressParish
     * @param addressMunicipality
     * @param addressDistrict
     * @param telephone
     * @param mobilePhone
     * @param email
     * @param webSite
     * @param contributorNumber
     * @param occupation
     * @param majorDegree
     * @param username
     * @param password
     * @param candidateNumber
     * @param specialization
     * @param majorDegreeSchool
     * @param majorDegreeYear
     * @param average
     * @param executionDegree
     * @param country
     * @param identificationDocumentExpirationDate
     */
    public MasterDegreeCandidate(String identificationDocumentNumber,
    TipoDocumentoIdentificacao identificationDocumentType, String identificationDocumentIssuePlace,
    Date identificationDocumentIssueDate, String name, Sexo sex,
    EstadoCivil maritalStatus, Date birth, String fatherName, String motherName,
    ICountry nationality, String birthPlaceParish, String birthPlaceMunicipality,
    String birthPlaceDistrict, String address, String place,
    String postCode, String addressParish, String addressMunicipality,
    String addressDistrict, String telephone, String mobilePhone, String email,
    String webSite, String contributorNumber, String occupation, String majorDegree,
    String username, String password, Integer candidateNumber,
    Specialization specialization, String majorDegreeSchool, Integer majorDegreeYear, 
    Double average, ICursoExecucao executionDegree, ICountry country, Date identificationDocumentExpirationDate) {
            } 
    
    public MasterDegreeCandidate(IPessoa person, ICursoExecucao executionDegree, Integer candidateNumber, Specialization specialization, 
    	   String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, Double average){
    	   	this.person = person;
    	   	this.executionDegree = executionDegree;
    	   	this.candidateNumber = candidateNumber;
    	   	this.specialization = specialization;
    	   	this.majorDegree = majorDegree;
    	   	this.majorDegreeSchool = majorDegreeSchool;
    	   	this.majorDegreeYear = majorDegreeYear;
    	   	this.average = average;
    	   	
    }
    
     
    public boolean equals(Object o) {
        return
        ((o instanceof MasterDegreeCandidate) &&
		
		((this.person.equals(((MasterDegreeCandidate)o).getPerson())) &&
		 (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization())) &&
		 (this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree))) ||
		 
		((this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree)) &&
		 (this.candidateNumber.equals(((MasterDegreeCandidate)o).getCandidateNumber())) &&
		 (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization()))));       
        
    }

    public String toString() {
        String result = "Master Degree Candidate :\n";
        result += "\n  - Internal Code : " + internalCode;
		result += "\n  - Person : " + person;
        result += "\n  - Major Degree : " + majorDegree;
        result += "\n  - Candidate Number : " + candidateNumber;
        result += "\n  - Specialization : " + specialization;
        result += "\n  - Major Degree School : " + majorDegreeSchool;
        result += "\n  - Major Degree Year : " + majorDegreeYear;
        result += "\n  - Major Degree Average : " + average;
        result += "\n  - Master Degree : " + executionDegree;

        
        return result;
    }  

	public void changePersonalData(String name, String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict,  
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType,
    		String maritalStatus, ICountry country , ICountry nationality, String specialization, Double average,
    		Date birth, Date identificationDocumentIssueDate, Date identificationDocumentExpirationDate
    		) {
		
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
	 * @return ICursoExecucao
	 */
	public ICursoExecucao getExecutionDegree() {
		return executionDegree;
	}

	/**
	 * @return Integer
	 */
	public Integer getExecutionDegreeKey() {
		return executionDegreeKey;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
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
	 * @return IPessoa
	 */
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * @return Integer
	 */
	public Integer getPersonKey() {
		return personKey;
	}

	/**
	 * @return Set
	 */
	public Set getSituations() {
		return situations;
	}

	/**
	 * @return Specialization
	 */
	public Specialization getSpecialization() {
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
	 * Sets the executionDegree.
	 * @param executionDegree The executionDegree to set
	 */
	public void setExecutionDegree(ICursoExecucao executionDegree) {
		this.executionDegree = executionDegree;
	}

	/**
	 * Sets the executionDegreeKey.
	 * @param executionDegreeKey The executionDegreeKey to set
	 */
	public void setExecutionDegreeKey(Integer executionDegreeKey) {
		this.executionDegreeKey = executionDegreeKey;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
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
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setPerson(IPessoa person) {
		this.person = person;
	}

	/**
	 * Sets the personKey.
	 * @param personKey The personKey to set
	 */
	public void setPersonKey(Integer personKey) {
		this.personKey = personKey;
	}

	/**
	 * Sets the situations.
	 * @param situations The situations to set
	 */
	public void setSituations(Set situations) {
		this.situations = situations;
	}

	/**
	 * Sets the specialization.
	 * @param specialization The specialization to set
	 */
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

} // End of class definition
