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

import java.util.Date;
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
    
    /**
     * @deprecated
     * @param name
     * @param majorDegree
     * @param majorDegreeSchool
     * @param majorDegreeYear
     * @param fatherName
     * @param motherName
     * @param birthPlaceParish
     * @param birthPlaceMunicipality
     * @param birthPlaceDistrict
     * @param identificationDocumentNumber
     * @param identificationDocumentIssuePlace
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
     * @param sex
     * @param identificationDocumentType
     * @param maritalStatus
     * @param country
     * @param nationality
     * @param specialization
     * @param average
     * @param birth
     * @param identificationDocumentIssueDate
     * @param identificationDocumentExpirationDate
     */
    void changePersonalData(String name, String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict, 
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType, 
    		String maritalStatus, ICountry country, ICountry nationality, String specialization, Double average,
    		Date birth, Date identificationDocumentIssueDate , Date identificationDocumentExpirationDate
    );
    
    
}

