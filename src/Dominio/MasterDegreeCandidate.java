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
 
    private String identificationDocumentNumber = null;
    private TipoDocumentoIdentificacao identificationDocumentType;
    private String identificationDocumentIssuePlace = null;
	private Date identificationDocumentIssueDate;
	private Date identificationDocumentExpirationDate;
    private String name = null;
    private Sexo sex = null;
	private EstadoCivil maritalStatus = null;
    private Date birth;
    private String fatherName = null;
    private String motherName = null;
    private String birthPlaceParish = null;
    private String birthPlaceMunicipality = null;
    private String birthPlaceDistrict = null;
    private String address = null;
    private String place = null;
    private String postCode = null;
    private String addressParish = null;
    private String addressMunicipality = null;
    private String addressDistrict = null;
    private String telephone = null;
    private String mobilePhone = null;
    private String email = null;
    private String webSite = null;
    private String contributorNumber = null;
    private String occupation = null;
    private String majorDegree = null;
    private String username = null;
    private String password = null;
    private Integer candidateNumber = null;
    private Specialization specialization = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
  
    // Instance from class Degree
    private ICursoExecucao executionDegree = null;

    // Instance from class Country
    private ICountry country = null;
	private ICountry nationality = null;

	// List of Situations
    private Set situations;
 
    
    // Internal Codes from Database
    private Integer internalCode;            // Internal code for Master Degree Candidate
    private Integer executionDegreeKey;               // Foreign Key from table Degree
	private Integer countryKey;              // Foreign Key from table Country
	private Integer nationalityCountryKey;   // Foreign Key from table Country
    
    public MasterDegreeCandidate() {
        identificationDocumentNumber = null;
        identificationDocumentType = null;
        identificationDocumentIssuePlace = null;
		identificationDocumentIssueDate = null;
		identificationDocumentExpirationDate = null;
        name = null;
        sex = null;
        maritalStatus = null;
        birth = null;
        fatherName = null;
        motherName = null;
        nationality = null;
        birthPlaceParish = null;
        birthPlaceMunicipality = null; 
        birthPlaceDistrict = null;
        address = null;
        place = null;
        postCode = null;
        addressParish = null;
        addressMunicipality = null;
        addressDistrict = null;
        telephone = null;
        mobilePhone = null;
        email = null;
        webSite = null;
        contributorNumber = null;
        occupation = null;
        majorDegree = null;
        username = null;
        password = null;
        executionDegree = null;
        candidateNumber = null;
        specialization = null;
        majorDegreeSchool = null;
        majorDegreeYear = null;
        average = null;
        country = null;
		situations = null;
    } 
    
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
        
        setIdentificationDocumentNumber(identificationDocumentNumber);
        setIdentificationDocumentType(identificationDocumentType);
        setIdentificationDocumentIssuePlace(identificationDocumentIssuePlace);
        setIdentificationDocumentIssueDate(identificationDocumentIssueDate);
        setName(name);
        setSex(sex);
        setMaritalStatus(maritalStatus);
        setBirth(birth);
        setFatherName(fatherName);
        setMotherName(motherName);
        setNationality(nationality);
        setBirthPlaceParish(birthPlaceParish);
        setBirthPlaceMunicipality(birthPlaceMunicipality);
        setBirthPlaceDistrict(birthPlaceDistrict);
        setAddress(address);
        setPlace(place);
        setPostCode(postCode);
        setAddressParish(addressParish);
        setAddressMunicipality(addressMunicipality);
        setAddressDistrict(addressDistrict);
        setTelephone(telephone);
        setMobilePhone(mobilePhone);
        setEmail(email);
        setWebSite(webSite);
        setContributorNumber(contributorNumber);
        setOccupation(occupation);
        setMajorDegree(majorDegree);
        setUsername(username);
        setPassword(password);
        setCandidateNumber(candidateNumber);

        setSpecialization(specialization);
        setMajorDegreeSchool(majorDegreeSchool);
        setMajorDegreeYear(majorDegreeYear);
        setAverage(average);
        setExecutionDegree(executionDegree);
        setCountry(country);
        setIdentificationDocumentExpirationDate(identificationDocumentExpirationDate);
    } 
    
     
    public boolean equals(Object o) {
        return
        ((o instanceof MasterDegreeCandidate) &&
		
		(this.username.equals(((MasterDegreeCandidate)o).getUsername())) ||
		
		((this.candidateNumber.equals(((MasterDegreeCandidate)o).getCandidateNumber())) &&
		 (this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree))) ||
		 
		((this.executionDegree.equals(((MasterDegreeCandidate)o).executionDegree)) &&
		 (this.identificationDocumentNumber.equals(((MasterDegreeCandidate)o).getIdentificationDocumentNumber())) &&
		 (this.identificationDocumentType.equals(((MasterDegreeCandidate)o).getIdentificationDocumentType()))));       
        
    }

    public String toString() {
        String result = "Master Degree Candidate :\n";
        result += "\n  - Internal Code : " + internalCode;
        result += "\n  - Identification Document Number : " + identificationDocumentNumber;
        result += "\n  - Identification Document Type : " + identificationDocumentType;
        result += "\n  - Identification Document Issue Place : " + identificationDocumentIssuePlace;
		result += "\n  - Identification Document Issue Date : " + identificationDocumentIssueDate;
		result += "\n  - Identification Document Expiration Date : " + identificationDocumentExpirationDate;
        result += "\n  - Candidate Name : " + name;
        result += "\n  - Sex : " + sex;
        result += "\n  - Marital Status : " + maritalStatus;
        result += "\n  - Birth : " + birth;
        result += "\n  - Father Name : " + fatherName;
        result += "\n  - Mother Name : " + motherName;
        result += "\n  - Nationality : " + nationality;
        result += "\n  - Birth Place Parish : " + birthPlaceParish;
        result += "\n  - Birth Place Municipality : " + birthPlaceMunicipality;
        result += "\n  - Birth Place District : " + birthPlaceDistrict;
        result += "\n  - Address : " + address;
        result += "\n  - Place : " + place;
        result += "\n  - Post Code : " + postCode;
        result += "\n  - Address Parish : " + addressParish;
        result += "\n  - Address Municipality : " + addressMunicipality;
        result += "\n  - Address District : " + addressDistrict;
        result += "\n  - Telephone : " + telephone;
        result += "\n  - MobilePhone : " + mobilePhone;
        result += "\n  - E-Mail : " + email;
        result += "\n  - HomePage : " + webSite;
        result += "\n  - Contributor Number : " + contributorNumber;
        result += "\n  - Occupation : " + occupation;
        result += "\n  - Major Degree : " + majorDegree;
        result += "\n  - Username : " + username;
        result += "\n  - Password : " + password;
        result += "\n  - Candidate Number : " + candidateNumber;
        result += "\n  - Specialization : " + specialization;
        result += "\n  - Major Degree School : " + majorDegreeSchool;
        result += "\n  - Major Degree Year : " + majorDegreeYear;
        result += "\n  - Major Degree Average : " + average;
        result += "\n  - Master Degree : " + executionDegree;
        result += "\n  - Country : " + country;
//        result += "\n  - List of Situations : " + situations;
        
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
        setIdentificationDocumentNumber(identificationDocumentNumber);
		setIdentificationDocumentIssuePlace(identificationDocumentIssuePlace);
		setIdentificationDocumentIssueDate(identificationDocumentIssueDate);
		setIdentificationDocumentExpirationDate(identificationDocumentExpirationDate);
		setIdentificationDocumentType(new TipoDocumentoIdentificacao(identificationDocumentType));
        setName(name);
        setFatherName(fatherName);
        setMotherName(motherName);
        setBirthPlaceParish(birthPlaceParish);
        setBirth(birth);
        setBirthPlaceMunicipality(birthPlaceMunicipality);
        setBirthPlaceDistrict(birthPlaceDistrict);
        setAddress(address);
        setPlace(place);
        setPostCode(postCode);
        setAddressParish(addressParish);
        setAddressMunicipality(addressMunicipality);
        setAddressDistrict(addressDistrict);
        setTelephone(telephone);
        setMobilePhone(mobilePhone);
        setEmail(email);
        setWebSite(webSite);
        setContributorNumber(contributorNumber);
        setOccupation(occupation);
        setMajorDegree(majorDegree);
        setUsername(username);
        setPassword(password);
        setMajorDegreeSchool(majorDegreeSchool);
        setMajorDegreeYear(majorDegreeYear);
        setSex(new Sexo(sex));
        setMaritalStatus(new EstadoCivil(maritalStatus));
		setCountry(country);    
		setNationality(nationality);  
		setSpecialization(new Specialization(specialization));  
		setAverage(average);
		
	}
 
	/**
	 * @return String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return String
	 */
	public String getAddressDistrict() {
		return addressDistrict;
	}

	/**
	 * @return String
	 */
	public String getAddressMunicipality() {
		return addressMunicipality;
	}

	/**
	 * @return String
	 */
	public String getAddressParish() {
		return addressParish;
	}

	/**
	 * @return Double
	 */
	public Double getAverage() {
		return average;
	}

	/**
	 * @return Date
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * @return String
	 */
	public String getBirthPlaceDistrict() {
		return birthPlaceDistrict;
	}

	/**
	 * @return String
	 */
	public String getBirthPlaceMunicipality() {
		return birthPlaceMunicipality;
	}

	/**
	 * @return String
	 */
	public String getBirthPlaceParish() {
		return birthPlaceParish;
	}

	/**
	 * @return Integer
	 */
	public Integer getCandidateNumber() {
		return candidateNumber;
	}

	/**
	 * @return String
	 */
	public String getContributorNumber() {
		return contributorNumber;
	}

	/**
	 * @return ICountry
	 */
	public ICountry getCountry() {
		return country;
	}

	/**
	 * @return Integer
	 */
	public Integer getCountryKey() {
		return countryKey;
	}

	/**
	 * @return String
	 */
	public String getEmail() {
		return email;
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
	 * @return String
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * @return Date
	 */
	public Date getIdentificationDocumentExpirationDate() {
		return identificationDocumentExpirationDate;
	}

	/**
	 * @return Date
	 */
	public Date getIdentificationDocumentIssueDate() {
		return identificationDocumentIssueDate;
	}

	/**
	 * @return String
	 */
	public String getIdentificationDocumentIssuePlace() {
		return identificationDocumentIssuePlace;
	}

	/**
	 * @return String
	 */
	public String getIdentificationDocumentNumber() {
		return identificationDocumentNumber;
	}

	/**
	 * @return TipoDocumentoIdentificacao
	 */
	public TipoDocumentoIdentificacao getIdentificationDocumentType() {
		return identificationDocumentType;
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
	 * @return EstadoCivil
	 */
	public EstadoCivil getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @return String
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @return String
	 */
	public String getMotherName() {
		return motherName;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return ICountry
	 */
	public ICountry getNationality() {
		return nationality;
	}

	/**
	 * @return Integer
	 */
	public Integer getNationalityCountryKey() {
		return nationalityCountryKey;
	}

	/**
	 * @return String
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return String
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @return String
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @return Sexo
	 */
	public Sexo getSex() {
		return sex;
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
	 * @return String
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return String
	 */
	public String getWebSite() {
		return webSite;
	}

	/**
	 * Sets the address.
	 * @param address The address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the addressDistrict.
	 * @param addressDistrict The addressDistrict to set
	 */
	public void setAddressDistrict(String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}

	/**
	 * Sets the addressMunicipality.
	 * @param addressMunicipality The addressMunicipality to set
	 */
	public void setAddressMunicipality(String addressMunicipality) {
		this.addressMunicipality = addressMunicipality;
	}

	/**
	 * Sets the addressParish.
	 * @param addressParish The addressParish to set
	 */
	public void setAddressParish(String addressParish) {
		this.addressParish = addressParish;
	}

	/**
	 * Sets the average.
	 * @param average The average to set
	 */
	public void setAverage(Double average) {
		this.average = average;
	}

	/**
	 * Sets the birth.
	 * @param birth The birth to set
	 */
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	/**
	 * Sets the birthPlaceDistrict.
	 * @param birthPlaceDistrict The birthPlaceDistrict to set
	 */
	public void setBirthPlaceDistrict(String birthPlaceDistrict) {
		this.birthPlaceDistrict = birthPlaceDistrict;
	}

	/**
	 * Sets the birthPlaceMunicipality.
	 * @param birthPlaceMunicipality The birthPlaceMunicipality to set
	 */
	public void setBirthPlaceMunicipality(String birthPlaceMunicipality) {
		this.birthPlaceMunicipality = birthPlaceMunicipality;
	}

	/**
	 * Sets the birthPlaceParish.
	 * @param birthPlaceParish The birthPlaceParish to set
	 */
	public void setBirthPlaceParish(String birthPlaceParish) {
		this.birthPlaceParish = birthPlaceParish;
	}

	/**
	 * Sets the candidateNumber.
	 * @param candidateNumber The candidateNumber to set
	 */
	public void setCandidateNumber(Integer candidateNumber) {
		this.candidateNumber = candidateNumber;
	}

	/**
	 * Sets the contributorNumber.
	 * @param contributorNumber The contributorNumber to set
	 */
	public void setContributorNumber(String contributorNumber) {
		this.contributorNumber = contributorNumber;
	}

	/**
	 * Sets the country.
	 * @param country The country to set
	 */
	public void setCountry(ICountry country) {
		this.country = country;
	}

	/**
	 * Sets the countryKey.
	 * @param countryKey The countryKey to set
	 */
	public void setCountryKey(Integer countryKey) {
		this.countryKey = countryKey;
	}

	/**
	 * Sets the email.
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * Sets the fatherName.
	 * @param fatherName The fatherName to set
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * Sets the identificationDocumentExpirationDate.
	 * @param identificationDocumentExpirationDate The identificationDocumentExpirationDate to set
	 */
	public void setIdentificationDocumentExpirationDate(Date identificationDocumentExpirationDate) {
		this.identificationDocumentExpirationDate =
			identificationDocumentExpirationDate;
	}

	/**
	 * Sets the identificationDocumentIssueDate.
	 * @param identificationDocumentIssueDate The identificationDocumentIssueDate to set
	 */
	public void setIdentificationDocumentIssueDate(Date identificationDocumentIssueDate) {
		this.identificationDocumentIssueDate = identificationDocumentIssueDate;
	}

	/**
	 * Sets the identificationDocumentIssuePlace.
	 * @param identificationDocumentIssuePlace The identificationDocumentIssuePlace to set
	 */
	public void setIdentificationDocumentIssuePlace(String identificationDocumentIssuePlace) {
		this.identificationDocumentIssuePlace =
			identificationDocumentIssuePlace;
	}

	/**
	 * Sets the identificationDocumentNumber.
	 * @param identificationDocumentNumber The identificationDocumentNumber to set
	 */
	public void setIdentificationDocumentNumber(String identificationDocumentNumber) {
		this.identificationDocumentNumber = identificationDocumentNumber;
	}

	/**
	 * Sets the identificationDocumentType.
	 * @param identificationDocumentType The identificationDocumentType to set
	 */
	public void setIdentificationDocumentType(TipoDocumentoIdentificacao identificationDocumentType) {
		this.identificationDocumentType = identificationDocumentType;
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
	 * Sets the maritalStatus.
	 * @param maritalStatus The maritalStatus to set
	 */
	public void setMaritalStatus(EstadoCivil maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Sets the mobilePhone.
	 * @param mobilePhone The mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * Sets the motherName.
	 * @param motherName The motherName to set
	 */
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the nationality.
	 * @param nationality The nationality to set
	 */
	public void setNationality(ICountry nationality) {
		this.nationality = nationality;
	}

	/**
	 * Sets the nationalityCountryKey.
	 * @param nationalityCountryKey The nationalityCountryKey to set
	 */
	public void setNationalityCountryKey(Integer nationalityCountryKey) {
		this.nationalityCountryKey = nationalityCountryKey;
	}

	/**
	 * Sets the occupation.
	 * @param occupation The occupation to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * Sets the password.
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Sets the place.
	 * @param place The place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * Sets the postCode.
	 * @param postCode The postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * Sets the sex.
	 * @param sex The sex to set
	 */
	public void setSex(Sexo sex) {
		this.sex = sex;
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

	/**
	 * Sets the telephone.
	 * @param telephone The telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * Sets the username.
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets the webSite.
	 * @param webSite The webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

} // End of class definition
