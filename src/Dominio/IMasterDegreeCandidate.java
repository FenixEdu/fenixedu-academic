/*
 * IMasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 10:29
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


public interface IMasterDegreeCandidate {
    
    // Set Methods
    void setMajorDegree(String majorDegree);
    void setCandidateNumber(Integer candidateNumber);
    void setSpecialization(Specialization specialization);
    void setMajorDegreeSchool(String majorDegreeSchool);
    void setMajorDegreeYear(Integer majorDegreeYear);
    void setAverage(Double average);
    void setExecutionDegree(ICursoExecucao executionDegree);    
    void setSituations(Set situations);
    void setPerson(IPessoa person);
    void setSpecializationArea(String specializationArea);
    
    
    // Get Methods
    String getMajorDegree();
    Integer getCandidateNumber();
    Specialization getSpecialization();
    String getMajorDegreeSchool();
    Integer getMajorDegreeYear();
    Double getAverage();
    ICursoExecucao getExecutionDegree();    
	Set getSituations();
    IPessoa getPerson();
    String getSpecializationArea();
    
    
}

