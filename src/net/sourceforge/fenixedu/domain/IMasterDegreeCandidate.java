/*
 * IMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 10:29
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.Specialization;

public interface IMasterDegreeCandidate extends IDomainObject {

    // Set Methods
    void setMajorDegree(String majorDegree);

    void setCandidateNumber(Integer candidateNumber);

    void setSpecialization(Specialization specialization);

    void setMajorDegreeSchool(String majorDegreeSchool);

    void setMajorDegreeYear(Integer majorDegreeYear);

    void setAverage(Double average);

    void setExecutionDegree(IExecutionDegree executionDegree);

    void setSituations(List situations);

    void setPerson(IPerson person);

    void setSpecializationArea(String specializationArea);

    void setSubstituteOrder(Integer substituteOrder);

    void setGivenCredits(Double givenCredits);

    void setGivenCreditsRemarks(String givenCreditsRemarks);

    // Get Methods
    String getMajorDegree();

    Integer getCandidateNumber();

    Specialization getSpecialization();

    String getMajorDegreeSchool();

    Integer getMajorDegreeYear();

    Double getAverage();

    IExecutionDegree getExecutionDegree();

    List getSituations();

    IPerson getPerson();

    String getSpecializationArea();

    Integer getSubstituteOrder();

    Double getGivenCredits();

    String getGivenCreditsRemarks();

    /**
     * 
     * @return The candidate's active Situation
     */
    ICandidateSituation getActiveCandidateSituation();

}

