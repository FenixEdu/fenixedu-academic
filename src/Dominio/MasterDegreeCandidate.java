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


import java.util.Set;

import Util.Specialization;
 

public class MasterDegreeCandidate implements IMasterDegreeCandidate {
 

    private String majorDegree = null;
    private Integer candidateNumber = null;
    private Specialization specialization = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
    
    // Instance from class Degree
    private ICursoExecucao executionDegree = null;

    // Instance from class Country

	private IPessoa person;
	// List of Situations
    private Set situations;
 
    
    // Internal Codes from Database
    private Integer internalCode;            // Internal code for Master Degree Candidate
    private Integer executionDegreeKey;               // Foreign Key from table Degree
	private Integer personKey;				// Foreign Key from table Person
    
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
	public ICursoExecucao getExecutionDegree() {
		return executionDegree;
	}

	/**
	 * @return
	 */
	public Integer getExecutionDegreeKey() {
		return executionDegreeKey;
	}

	/**
	 * @return
	 */
	public Integer getInternalCode() {
		return internalCode;
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
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * @return
	 */
	public Integer getPersonKey() {
		return personKey;
	}

	/**
	 * @return
	 */
	public Set getSituations() {
		return situations;
	}

	/**
	 * @return
	 */
	public Specialization getSpecialization() {
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
	 * @param execucao
	 */
	public void setExecutionDegree(ICursoExecucao execucao) {
		executionDegree = execucao;
	}

	/**
	 * @param integer
	 */
	public void setExecutionDegreeKey(Integer integer) {
		executionDegreeKey = integer;
	}

	/**
	 * @param integer
	 */
	public void setInternalCode(Integer integer) {
		internalCode = integer;
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
	 * @param pessoa
	 */
	public void setPerson(IPessoa pessoa) {
		person = pessoa;
	}

	/**
	 * @param integer
	 */
	public void setPersonKey(Integer integer) {
		personKey = integer;
	}

	/**
	 * @param set
	 */
	public void setSituations(Set set) {
		situations = set;
	}

	/**
	 * @param specialization
	 */
	public void setSpecialization(Specialization specialization) {
		this.specialization = specialization;
	}

} // End of class definition
