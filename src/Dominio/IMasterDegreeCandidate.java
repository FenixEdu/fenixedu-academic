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

import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;


public interface IMasterDegreeCandidate {
    
    // Set Methods
    void setIdentificationDocumentNumber(String identificationDocumentNumber);
    void setIdentificationDocumentType(TipoDocumentoIdentificacao identificationDocumentType);
    void setIdentificationDocumentIssuePlace(String identificationDocumentIssuePlace);
    void setIdentificationDocumentIssueDate(Date identificationDocumentIssueDate);
    void setName(String name);
    void setSex(Sexo sex);
    void setMaritalStatus(EstadoCivil maritalStatus);
    void setBirth(Date birth);
    void setFatherName(String fatherName);
    void setMotherName(String motherName);
    void setNationality(String nationality);
    void setBirthPlaceParish(String birthPlaceParish);
    void setBirthPlaceMunicipality(String birthPlaceMunicipality);
    void setBirthPlaceDistrict(String birthPlaceDistrict);
    void setAddress(String address);
    void setPlace(String place);
    void setPostCode(String postCode);
    void setAddressParish(String addressParish);
    void setAddressMunicipality(String addressMunicipality);
    void setAddressDistrict(String addressDistrict);
    void setTelephone(String telephone);
    void setMobilePhone(String mobilePhone);
    void setEmail(String email);
    void setWebSite(String webSite);
    void setContributorNumber(String contributorNumber);
    void setOccupation(String occupation);
    void setMajorDegree(String majorDegree);
    void setUsername(String username);
    void setPassword(String password);
    void setCandidateNumber(Integer candidateNumber);
    void setApplicationYear(Integer applicationYear);
    void setSpecialization(Specialization specialization);
    void setMajorDegreeSchool(String majorDegreeSchool);
    void setMajorDegreeYear(Integer majorDegreeYear);
    void setAverage(Double average);
    void setDegree(ICurso degree);    
    void setCountry(ICountry country);
    void setSituations(Set situations);
    
    // Get Methods
    String getIdentificationDocumentNumber();
    TipoDocumentoIdentificacao getIdentificationDocumentType();
    String getIdentificationDocumentIssuePlace();
    Date getIdentificationDocumentIssueDate();
    String getName();
    Sexo getSex();
    EstadoCivil getMaritalStatus();
    Date getBirth();
    String getFatherName();
    String getMotherName();
    String getNationality();
    String getBirthPlaceParish();
    String getBirthPlaceMunicipality();
    String getBirthPlaceDistrict();
    String getAddress();
    String getPlace();
    String getPostCode();
    String getAddressParish();
    String getAddressMunicipality();
    String getAddressDistrict();
    String getTelephone();
    String getMobilePhone();
    String getEmail();
    String getWebSite();
    String getContributorNumber();
    String getOccupation();
    String getMajorDegree();
    String getUsername();
    String getPassword();
    Integer getCandidateNumber();
    Integer getApplicationYear();
    Specialization getSpecialization();
    String getMajorDegreeSchool();
    Integer getMajorDegreeYear();
    Double getAverage();
    ICurso getDegree();    
	ICountry getCountry();    
	Set getSituations();
    
    void changePersonalData(String name, String password,
    		String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict, 
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType, 
    		String maritalStatus, ICountry country, String nationality, String specialization, Double average,
    		Date birth, Date identificationDocumentIssueDate 
    );
    
    
}

