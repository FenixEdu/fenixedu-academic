/*
 * MasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 9:53
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package Dominio;

import java.util.Iterator;
import java.util.List;

import Util.Specialization;
import Util.State;

public class MasterDegreeCandidate extends DomainObject implements IMasterDegreeCandidate {

    private String majorDegree = null;

    private Integer candidateNumber = null;

    private Specialization specialization = null;

    private String majorDegreeSchool = null;

    private Integer majorDegreeYear = null;

    private Double average = null;

    private String specializationArea;

    private Integer substituteOrder;

    private Double givenCredits;

    private String givenCreditsRemarks;

    // Instance from class Degree
    private ICursoExecucao executionDegree = null;

    // Instance from class Country

    private IPessoa person;

    // List of Situations
    private List situations;

    // Internal Codes from Database
    private Integer executionDegreeKey; // Foreign Key from table Degree

    private Integer personKey; // Foreign Key from table Person

    /**
     * @return
     */
    public String getGivenCreditsRemarks() {
        return givenCreditsRemarks;
    }

    /**
     * @param givenCreditsRemarks
     */
    public void setGivenCreditsRemarks(String givenCreditsRemarks) {
        this.givenCreditsRemarks = givenCreditsRemarks;
    }

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

    public MasterDegreeCandidate(IPessoa person, ICursoExecucao executionDegree,
            Integer candidateNumber, Specialization specialization, String majorDegree,
            String majorDegreeSchool, Integer majorDegreeYear, Double average) {
        this.person = person;
        this.executionDegree = executionDegree;
        this.candidateNumber = candidateNumber;
        this.specialization = specialization;
        this.majorDegree = majorDegree;
        this.majorDegreeSchool = majorDegreeSchool;
        this.majorDegreeYear = majorDegreeYear;
        this.average = average;

    }

    public ICandidateSituation getActiveCandidateSituation() {
        Iterator iterator = this.getSituations().iterator();
        while (iterator.hasNext()) {
            ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
            if (candidateSituationTemp.getValidation().equals(new State(State.ACTIVE))) {
                return candidateSituationTemp;
            }
        }
        return null;
    }

    public boolean equals(Object o) {

        boolean result = false;
        if (o instanceof IMasterDegreeCandidate) {

            result = getIdInternal().equals(((IMasterDegreeCandidate) o).getIdInternal());
            //			result =
            //					((this.person.equals(((MasterDegreeCandidate)o).getPerson())) &&
            //					 (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization()))
            // &&
            //					 (this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree)))
            // ||
            //				 
            //					((this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree))
            // &&
            //					 (this.candidateNumber.equals(((MasterDegreeCandidate)o).getCandidateNumber()))
            // &&
            //					 (this.specialization.equals(((MasterDegreeCandidate)o).getSpecialization())));
        }
        return result;
    }

    public String toString() {
        String result = "Master Degree Candidate :\n";
        result += "\n  - Internal Code : " + getIdInternal();
        result += "\n  - Person : " + person;
        result += "\n  - Major Degree : " + majorDegree;
        result += "\n  - Candidate Number : " + candidateNumber;
        result += "\n  - Specialization : " + specialization;
        result += "\n  - Major Degree School : " + majorDegreeSchool;
        result += "\n  - Major Degree Year : " + majorDegreeYear;
        result += "\n  - Major Degree Average : " + average;
        result += "\n  - Master Degree : " + executionDegree;
        result += "\n  - Specialization Area : " + specializationArea;
        result += "\n  - Substitute Order : " + substituteOrder;
        result += "\n  - Given Credits : " + givenCredits;
        result += "\n  - Given Credits Remarks : " + givenCreditsRemarks;

        return result;
    }

    /**
     * @return Specialization Area
     */
    public String getSpecializationArea() {
        return specializationArea;
    }

    /**
     * @param specializationArea
     */
    public void setSpecializationArea(String specializationArea) {
        this.specializationArea = specializationArea;
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
    public List getSituations() {
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
    public void setSituations(List list) {
        situations = list;
    }

    /**
     * @param specialization
     */
    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    /**
     * @return
     */
    public Integer getSubstituteOrder() {
        return substituteOrder;
    }

    /**
     * @param integer
     */
    public void setSubstituteOrder(Integer integer) {
        substituteOrder = integer;
    }

    /**
     * @return
     */
    public Double getGivenCredits() {
        return givenCredits;
    }

    /**
     * @param double1
     */
    public void setGivenCredits(Double double1) {
        givenCredits = double1;
    }

} // End of class definition
