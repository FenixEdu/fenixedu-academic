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
 
    private String identificationDocumentNumber = null;
    private String identificationDocumentIssuePlace = null;
	private Date identificationDocumentIssueDate;
	private Date identificationDocumentExpirationDate;
    private String name = null;
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
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
  
    private String sex = null;    
  
  
  	private InfoDegree infoDegree = null;
//    private String degreeName = null;
//    private String degreeCode = null;
    private InfoCountry infoCountry = null;
	private InfoCountry infoNationality = null;
	private InfoExecutionYear infoExecutionYear = null;

    private String infoIdentificationDocumentType = null;
    private String specialization = null;
	private String maritalStatus = null;
	private InfoCandidateSituation infoCandidateSituation = null;

    	
    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public InfoMasterDegreeCandidate() {
        identificationDocumentNumber = null;
	    infoIdentificationDocumentType = null;
        identificationDocumentIssuePlace = null;
		identificationDocumentIssueDate = null;
		identificationDocumentExpirationDate = null;
        name = null;
        sex = null;
        maritalStatus = null;
        birth = null;
        fatherName = null;
        motherName = null;
        infoNationality = null;
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
        candidateNumber = null;
        infoExecutionYear = null;
        specialization = null;
        majorDegreeSchool = null;
        majorDegreeYear = null;
        average = null;
		infoCountry = null;		
		infoCandidateSituation = null;
		infoDegree = null;

    } // Fim do Construtor Sem Argumentos
    
    
    public InfoMasterDegreeCandidate(String name, String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict, 
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType, 
			String maritalStatus, InfoCountry country, InfoCountry nationality, String specialization, InfoExecutionYear infoExecutionYear,
			Integer candidateNumber, Double average, Date birth, Date identificationDocumentIssueDate, Date identificationDocumentExpirationDate
    		) {
	    
        setIdentificationDocumentNumber(identificationDocumentNumber);
        setInfoIdentificationDocumentType(identificationDocumentType);
        setIdentificationDocumentIssuePlace(identificationDocumentIssuePlace);
		setIdentificationDocumentIssueDate(identificationDocumentIssueDate);
		setIdentificationDocumentExpirationDate(identificationDocumentExpirationDate);
        setName(name);
        setSex(sex);
        setMaritalStatus(maritalStatus);
        setBirth(birth);
        setFatherName(fatherName);
        setMotherName(motherName);
        setInfoNationality(nationality);
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
        setMajorDegreeSchool(majorDegreeSchool);
        setMajorDegreeYear(majorDegreeYear);
		setSpecialization(specialization);		
        setAverage(average);
        setInfoCountry(country);
		setInfoExecutionYear(infoExecutionYear);
		setCandidateNumber(candidateNumber);
    }
        
    public boolean equals(Object o) {
        return
        ((o instanceof InfoMasterDegreeCandidate) &&
        (username.equals(((InfoMasterDegreeCandidate)o).getUsername())));
    }

    public String toString() {
        String result = "DataBean do Candidato Mestrado :\n";
        result += "\n  - Numero do Documento de Identificacao : " + identificationDocumentNumber;
        result += "\n  - Tipo do Documento de Identificacao : " + infoIdentificationDocumentType;
        result += "\n  - Local de Emissao do Documento de Identificacao : " + identificationDocumentIssuePlace;
		result += "\n  - Data de Emissao do Documento de Identificacao : " + identificationDocumentIssueDate;
		result += "\n  - Data de Validade do Documento de Identificacao : " + identificationDocumentExpirationDate;
        result += "\n  - Nome do Candidato : " + name;
        result += "\n  - Sexo : " + sex;
        result += "\n  - Estado Civil : " + maritalStatus;
        result += "\n  - Data de Nascimento : " + birth;
        result += "\n  - Nome do Pai : " + fatherName;
        result += "\n  - Nome da Mae : " + motherName;
        result += "\n  - Nacionalidade : " + infoNationality;
        result += "\n  - Freguesia de Naturalidade : " + birthPlaceParish;
        result += "\n  - Concelho de Naturalidade : " + birthPlaceMunicipality;
        result += "\n  - Distrito de Naturalidade : " + birthPlaceDistrict;
        result += "\n  - Morada : " + address;
        result += "\n  - Localidade : " + place;
        result += "\n  - Codigo Postal : " + postCode;
        result += "\n  - Freguesia de Morada : " + addressParish;
        result += "\n  - Concelho de Morada : " + addressMunicipality;
        result += "\n  - Distrito de Morada : " + addressDistrict;
        result += "\n  - Numero de Telefone : " + telephone;
        result += "\n  - Numero de Telemovel : " + mobilePhone;
        result += "\n  - E-Mail : " + email;
        result += "\n  - HomePage : " + webSite;
        result += "\n  - Numero de Contribuinte : " + contributorNumber;
        result += "\n  - Profissao : " + occupation;
        result += "\n  - Licenciatura : " + majorDegree;
        result += "\n  - Username : " + username;
        result += "\n  - Password : " + password;
        result += "\n  - Numero de Candidato : " + candidateNumber;
        result += "\n  - Ano de Candidatura : " + infoExecutionYear;
        result += "\n  - Especializacao : " + specialization;
        result += "\n  - Escola de Licenciatura : " + majorDegreeSchool;
        result += "\n  - Ano de Licenciatura : " + majorDegreeYear;
        result += "\n  - Media de Licenciatura : " + average;
		result += "\n  - Pais : " + infoCountry;
		result += "\n  - Situação Activa : " + infoCandidateSituation;
		result += "\n  - InfoDegree : " + infoDegree;
        return result;
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
	 * @return String
	 */
	public String getEmail() {
		return email;
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
	 * @return String
	 */
	public String getInfoIdentificationDocumentType() {
		return infoIdentificationDocumentType;
	}

	/**
	 * @return InfoCandidateSituation
	 */
	public InfoCandidateSituation getInfoCandidateSituation() {
		return infoCandidateSituation;
	}

/**
 * @return InfoCountry
 */
public InfoCountry getInfoCountry() {
	return infoCountry;
}

	/**
	 * @return InfoDegree
	 */
	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	/**
	 * @return InfoExecutionYear
	 */
	public InfoExecutionYear getInfoExecutionYear() {
		return infoExecutionYear;
	}

	/**
	 * @return InfoCountry
	 */
	public InfoCountry getInfoNationality() {
		return infoNationality;
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
	public String getMaritalStatus() {
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
	 * @return String
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @return String
	 */
	public String getSpecialization() {
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
	 * Sets the email.
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the fatherName.
	 * @param fatherName The fatherName to set
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
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
	 * Sets the infoIdentificationDocumentType.
	 * @param infoIdentificationDocumentType The infoIdentificationDocumentType to set
	 */
	public void setInfoIdentificationDocumentType(String identificationDocumentType) {
		this.infoIdentificationDocumentType = identificationDocumentType;
	}

	/**
	 * Sets the infoCandidateSituation.
	 * @param infoCandidateSituation The infoCandidateSituation to set
	 */
	public void setInfoCandidateSituation(InfoCandidateSituation infoCandidateSituation) {
		this.infoCandidateSituation = infoCandidateSituation;
	}

/**
 * Sets the infoCountry.
 * @param infoCountry The infoCountry to set
 */
public void setInfoCountry(InfoCountry infoCountry) {
	this.infoCountry = infoCountry;
}

	/**
	 * Sets the infoDegree.
	 * @param infoDegree The infoDegree to set
	 */
	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}

	/**
	 * Sets the infoExecutionYear.
	 * @param infoExecutionYear The infoExecutionYear to set
	 */
	public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
		this.infoExecutionYear = infoExecutionYear;
	}

	/**
	 * Sets the infoNationality.
	 * @param infoNationality The infoNationality to set
	 */
	public void setInfoNationality(InfoCountry infoNationality) {
		this.infoNationality = infoNationality;
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
	public void setMaritalStatus(String maritalStatus) {
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
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Sets the specialization.
	 * @param specialization The specialization to set
	 */
	public void setSpecialization(String specialization) {
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

	/**
	 * @return Date
	 */
	public Date getIdentificationDocumentExpirationDate() {
		return identificationDocumentExpirationDate;
	}

	/**
	 * Sets the identificationDocumentExpirationDate.
	 * @param identificationDocumentExpirationDate The identificationDocumentExpirationDate to set
	 */
	public void setIdentificationDocumentExpirationDate(Date identificationDocumentExpirationDate) {
		this.identificationDocumentExpirationDate =
			identificationDocumentExpirationDate;
	}

}
