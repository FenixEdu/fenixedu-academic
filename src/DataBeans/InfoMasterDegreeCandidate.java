package DataBeans;

import java.util.Date;
import java.util.Iterator;

import Dominio.ICandidateSituation;
import Dominio.IMasterDegreeCandidate;
import Util.CandidateSituationValidation;

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
    private String name = null;
    private Date birth;
    private String fatherName = null;
    private String motherName = null;
    private String nationality = null;
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
    private Integer applicationYear = null;
    private String majorDegreeSchool = null;
    private Integer majorDegreeYear = null;
    private Double average = null;
  
    private String sex = null;    
  
    private String degreeName = null;
    private String degreeCode = null;
    private String country = null;

    private String identificationDocumentType = null;
    private String specialization = null;
	private String maritalStatus = null;
	private InfoCandidateSituation infoCandidateSituation = null;

    	
    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public InfoMasterDegreeCandidate() {
        identificationDocumentNumber = null;
	    identificationDocumentType = null;
        identificationDocumentIssuePlace = null;
        identificationDocumentIssueDate = null;
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
        candidateNumber = null;
        applicationYear = null;
        specialization = null;
        majorDegreeSchool = null;
        majorDegreeYear = null;
        average = null;
		degreeName = null;
		degreeCode = null;
		country = null;		
		infoCandidateSituation = null;

    } // Fim do Construtor Sem Argumentos
    
    
    public InfoMasterDegreeCandidate(String name, String password,
    		String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, 
    		String fatherName, String motherName, String birthPlaceParish, 
    		String birthPlaceMunicipality, String birthPlaceDistrict, 
    		String identificationDocumentNumber, String identificationDocumentIssuePlace, 
    		String address, String place, String postCode, 
    		String addressParish, String addressMunicipality, String addressDistrict, 
    		String telephone, String mobilePhone, String email, String webSite, 
    		String contributorNumber, String occupation, String sex, String identificationDocumentType, 
			String maritalStatus, String country, String nationality, String specialization, String degreeName,
			String degreeCode, Integer applicationYear, Integer candidateNumber, Double average,
			Date birth, Date identificationDocumentIssueDate
    		) {
	    
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
        setPassword(password);
        setMajorDegreeSchool(majorDegreeSchool);
        setMajorDegreeYear(majorDegreeYear);
		setSpecialization(specialization);		
        setAverage(average);
        setCountry(country);
		setDegreeName(degreeName);
		setDegreeCode(degreeCode);
		setApplicationYear(applicationYear);
		setCandidateNumber(candidateNumber);
        
        
        
    }
    
    public InfoMasterDegreeCandidate(IMasterDegreeCandidate masterDegreeCandidate) {
	    setIdentificationDocumentNumber(masterDegreeCandidate.getIdentificationDocumentNumber());
        setIdentificationDocumentType(masterDegreeCandidate.getIdentificationDocumentType().toString2());
        setIdentificationDocumentIssuePlace(masterDegreeCandidate.getIdentificationDocumentIssuePlace());
        setIdentificationDocumentIssueDate(masterDegreeCandidate.getIdentificationDocumentIssueDate());
        setName(masterDegreeCandidate.getName());
        setSex(masterDegreeCandidate.getSex().toString());
        setMaritalStatus(masterDegreeCandidate.getMaritalStatus().toString());
		
        setBirth(masterDegreeCandidate.getBirth());
        setFatherName(masterDegreeCandidate.getFatherName());
        setMotherName(masterDegreeCandidate.getMotherName());
        setNationality(masterDegreeCandidate.getNationality());
        setBirthPlaceParish(masterDegreeCandidate.getBirthPlaceParish());
        setBirthPlaceMunicipality(masterDegreeCandidate.getBirthPlaceMunicipality());
        setBirthPlaceDistrict(masterDegreeCandidate.getBirthPlaceDistrict());
        setAddress(masterDegreeCandidate.getAddress());
        setPlace(masterDegreeCandidate.getPlace());
        setPostCode(masterDegreeCandidate.getPostCode());
        setAddressParish(masterDegreeCandidate.getAddressParish());
        setAddressMunicipality(masterDegreeCandidate.getAddressMunicipality());
        setAddressDistrict(masterDegreeCandidate.getAddressDistrict());
        setTelephone(masterDegreeCandidate.getTelephone());
        setMobilePhone(masterDegreeCandidate.getMobilePhone());
        setEmail(masterDegreeCandidate.getEmail());
        setWebSite(masterDegreeCandidate.getWebSite());
        setContributorNumber(masterDegreeCandidate.getContributorNumber());
        setOccupation(masterDegreeCandidate.getOccupation());
        setMajorDegree(masterDegreeCandidate.getMajorDegree());
        setUsername(masterDegreeCandidate.getUsername());
        setPassword(masterDegreeCandidate.getPassword());
        setCandidateNumber(masterDegreeCandidate.getCandidateNumber());
        setApplicationYear(masterDegreeCandidate.getApplicationYear());
        setSpecialization(masterDegreeCandidate.getSpecialization().toString());
        setMajorDegreeSchool(masterDegreeCandidate.getMajorDegreeSchool());
        setMajorDegreeYear(masterDegreeCandidate.getMajorDegreeYear());
        setAverage(masterDegreeCandidate.getAverage());
  		setDegreeName(masterDegreeCandidate.getDegree().getNome());      
  		setDegreeCode(masterDegreeCandidate.getDegree().getSigla());      
		setCountry(masterDegreeCandidate.getCountry().getName());
		
		// Get Active Master Degree Candidate Situation
		Iterator iterator = masterDegreeCandidate.getSituations().iterator();

		while(iterator.hasNext()){
			ICandidateSituation candidateSituationTemp = (ICandidateSituation) iterator.next();
			
			if ((candidateSituationTemp.getValidation().getCandidateSituationValidation()).equals(new Integer(CandidateSituationValidation.ACTIVE))) {
				setInfoCandidateSituation(new InfoCandidateSituation(candidateSituationTemp.getDate(),
										  candidateSituationTemp.getRemarks(),
										  candidateSituationTemp.getSituation().toString()));
			}
		}
		
		
    }
    
    public boolean equals(Object o) {
        return
        ((o instanceof InfoMasterDegreeCandidate) &&
        (identificationDocumentNumber.equals(((InfoMasterDegreeCandidate)o).getIdentificationDocumentNumber())) &&
        (identificationDocumentType.equals(((InfoMasterDegreeCandidate)o).getIdentificationDocumentType())) &&
        (identificationDocumentIssuePlace.equals(((InfoMasterDegreeCandidate)o).getIdentificationDocumentIssuePlace())) &&
        (identificationDocumentIssueDate.getTime() == (((InfoMasterDegreeCandidate)o).getIdentificationDocumentIssueDate().getTime())) &&
        (name.equals(((InfoMasterDegreeCandidate)o).getName())) &&
        (sex.equals(((InfoMasterDegreeCandidate)o).getSex())) &&
        (maritalStatus.equals(((InfoMasterDegreeCandidate)o).getMaritalStatus())) &&
        (birth.getTime() == (((InfoMasterDegreeCandidate)o).getBirth().getTime())) &&
        (fatherName.equals(((InfoMasterDegreeCandidate)o).getFatherName())) &&
        (motherName.equals(((InfoMasterDegreeCandidate)o).getMotherName())) &&
        (nationality.equals(((InfoMasterDegreeCandidate)o).getNationality())) &&
        (birthPlaceParish.equals(((InfoMasterDegreeCandidate)o).getBirthPlaceParish())) &&
        (birthPlaceMunicipality.equals(((InfoMasterDegreeCandidate)o).getBirthPlaceMunicipality())) &&
        (birthPlaceDistrict.equals(((InfoMasterDegreeCandidate)o).getBirthPlaceDistrict())) &&
        (address.equals(((InfoMasterDegreeCandidate)o).getAddress())) &&
        (place.equals(((InfoMasterDegreeCandidate)o).getPlace())) &&
        (postCode.equals(((InfoMasterDegreeCandidate)o).getPostCode())) &&
        (addressParish.equals(((InfoMasterDegreeCandidate)o).getAddressParish())) &&
        (addressMunicipality.equals(((InfoMasterDegreeCandidate)o).getAddressMunicipality())) &&
        (addressDistrict.equals(((InfoMasterDegreeCandidate)o).getAddressDistrict())) &&
        (telephone.equals(((InfoMasterDegreeCandidate)o).getTelephone())) &&
        (mobilePhone.equals(((InfoMasterDegreeCandidate)o).getMobilePhone())) &&
        (email.equals(((InfoMasterDegreeCandidate)o).getEmail())) &&
        (webSite.equals(((InfoMasterDegreeCandidate)o).getWebSite())) &&
        (contributorNumber.equals(((InfoMasterDegreeCandidate)o).getContributorNumber())) &&
        (occupation.equals(((InfoMasterDegreeCandidate)o).getOccupation())) &&
        (majorDegree.equals(((InfoMasterDegreeCandidate)o).getMajorDegree())) &&
        (username.equals(((InfoMasterDegreeCandidate)o).getUsername())) &&
        (password.equals(((InfoMasterDegreeCandidate)o).getPassword())) &&
        (candidateNumber == ((InfoMasterDegreeCandidate)o).getCandidateNumber()) &&
		(applicationYear == ((InfoMasterDegreeCandidate)o).getApplicationYear()) &&
		(specialization.equals(((InfoMasterDegreeCandidate)o).getSpecialization())) &&
		(majorDegreeSchool.equals(((InfoMasterDegreeCandidate)o).getMajorDegreeSchool())) &&
		(majorDegreeYear == ((InfoMasterDegreeCandidate)o).getMajorDegreeYear()) &&
		(degreeName.equals(((InfoMasterDegreeCandidate)o).getDegreeName())) &&
		(degreeCode.equals(((InfoMasterDegreeCandidate)o).getDegreeCode())) &&
		(country.equals(((InfoMasterDegreeCandidate)o).getCountry())) &&
		(average == ((InfoMasterDegreeCandidate)o).getAverage()));
    }

    public String toString() {
        String result = "DataBean do Candidato Mestrado :\n";
        result += "\n  - Numero do Documento de Identificacao : " + identificationDocumentNumber;
        result += "\n  - Tipo do Documento de Identificacao : " + identificationDocumentType;
        result += "\n  - Local de Emissao do Documento de Identificacao : " + identificationDocumentIssuePlace;
        result += "\n  - Data de Emissao do Documento de Identificacao : " + identificationDocumentIssueDate;
        result += "\n  - Nome do Candidato : " + name;
        result += "\n  - Sexo : " + sex;
        result += "\n  - Estado Civil : " + maritalStatus;
        result += "\n  - Data de Nascimento : " + birth;
        result += "\n  - Nome do Pai : " + fatherName;
        result += "\n  - Nome da Mae : " + motherName;
        result += "\n  - Nacionalidade : " + nationality;
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
        result += "\n  - Ano de Candidatura : " + applicationYear;
        result += "\n  - Especializacao : " + specialization;
        result += "\n  - Escola de Licenciatura : " + majorDegreeSchool;
        result += "\n  - Ano de Licenciatura : " + majorDegreeYear;
        result += "\n  - Media de Licenciatura : " + average;
        result += "\n  - Nome do Curso : " + degreeName;
        result += "\n  - Sigla do Curso : " + degreeCode;
		result += "\n  - Pais : " + country;
		result += "\n  - Situação Activa : " + infoCandidateSituation;
        return result;
    }
    



	/**
	 * Returns the address.
	 * @return String
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the addressDistrict.
	 * @return String
	 */
	public String getAddressDistrict() {
		return addressDistrict;
	}

	/**
	 * Returns the addressMunicipality.
	 * @return String
	 */
	public String getAddressMunicipality() {
		return addressMunicipality;
	}

	/**
	 * Returns the addressParish.
	 * @return String
	 */
	public String getAddressParish() {
		return addressParish;
	}

	/**
	 * Returns the applicationYear.
	 * @return Integer
	 */
	public Integer getApplicationYear() {
		return applicationYear;
	}

	/**
	 * Returns the average.
	 * @return Double
	 */
	public Double getAverage() {
		return average;
	}

	/**
	 * Returns the birth.
	 * @return Date
	 */
	public Date getBirth() {
		return birth;
	}

	/**
	 * Returns the birthPlaceDistrict.
	 * @return String
	 */
	public String getBirthPlaceDistrict() {
		return birthPlaceDistrict;
	}

	/**
	 * Returns the birthPlaceMunicipality.
	 * @return String
	 */
	public String getBirthPlaceMunicipality() {
		return birthPlaceMunicipality;
	}

	/**
	 * Returns the birthPlaceParish.
	 * @return String
	 */
	public String getBirthPlaceParish() {
		return birthPlaceParish;
	}

	/**
	 * Returns the candidateNumber.
	 * @return Integer
	 */
	public Integer getCandidateNumber() {
		return candidateNumber;
	}

	/**
	 * Returns the contributorNumber.
	 * @return String
	 */
	public String getContributorNumber() {
		return contributorNumber;
	}

	/**
	 * Returns the country.
	 * @return String
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Returns the degreeCode.
	 * @return String
	 */
	public String getDegreeCode() {
		return degreeCode;
	}

	/**
	 * Returns the degreeName.
	 * @return String
	 */
	public String getDegreeName() {
		return degreeName;
	}

	/**
	 * Returns the email.
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns the fatherName.
	 * @return String
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * Returns the identificationDocumentIssueDate.
	 * @return Date
	 */
	public Date getIdentificationDocumentIssueDate() {
		return identificationDocumentIssueDate;
	}

	/**
	 * Returns the identificationDocumentIssuePlace.
	 * @return String
	 */
	public String getIdentificationDocumentIssuePlace() {
		return identificationDocumentIssuePlace;
	}

	/**
	 * Returns the identificationDocumentNumber.
	 * @return String
	 */
	public String getIdentificationDocumentNumber() {
		return identificationDocumentNumber;
	}

	/**
	 * Returns the identificationDocumentType.
	 * @return String
	 */
	public String getIdentificationDocumentType() {
		return identificationDocumentType;
	}

	/**
	 * Returns the majorDegree.
	 * @return String
	 */
	public String getMajorDegree() {
		return majorDegree;
	}

	/**
	 * Returns the majorDegreeSchool.
	 * @return String
	 */
	public String getMajorDegreeSchool() {
		return majorDegreeSchool;
	}

	/**
	 * Returns the majorDegreeYear.
	 * @return Integer
	 */
	public Integer getMajorDegreeYear() {
		return majorDegreeYear;
	}

	/**
	 * Returns the maritalStatus.
	 * @return String
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * Returns the mobilePhone.
	 * @return String
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * Returns the motherName.
	 * @return String
	 */
	public String getMotherName() {
		return motherName;
	}

	/**
	 * Returns the nationality.
	 * @return String
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the occupation.
	 * @return String
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * Returns the password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the place.
	 * @return String
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * Returns the postCode.
	 * @return String
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * Returns the sex.
	 * @return String
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Returns the specialization.
	 * @return String
	 */
	public String getSpecialization() {
		return specialization;
	}

	/**
	 * Returns the telephone.
	 * @return String
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the webSite.
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
	 * Sets the applicationYear.
	 * @param applicationYear The applicationYear to set
	 */
	public void setApplicationYear(Integer applicationYear) {
		this.applicationYear = applicationYear;
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
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Sets the degreeCode.
	 * @param degreeCode The degreeCode to set
	 */
	public void setDegreeCode(String degreeCode) {
		this.degreeCode = degreeCode;
	}

	/**
	 * Sets the degreeName.
	 * @param degreeName The degreeName to set
	 */
	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
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
	 * Sets the identificationDocumentType.
	 * @param identificationDocumentType The identificationDocumentType to set
	 */
	public void setIdentificationDocumentType(String identificationDocumentType) {
		this.identificationDocumentType = identificationDocumentType;
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
	 * Sets the nationality.
	 * @param nationality The nationality to set
	 */
	public void setNationality(String nacionality) {
		this.nationality = nacionality;
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
	 * Returns the infoCandidateSituation.
	 * @return InfoCandidateSituation
	 */
	public InfoCandidateSituation getInfoCandidateSituation() {
		return infoCandidateSituation;
	}

	/**
	 * Sets the infoCandidateSituation.
	 * @param infoCandidateSituation The infoCandidateSituation to set
	 */
	public void setInfoCandidateSituation(InfoCandidateSituation infoCandidateSituation) {
		this.infoCandidateSituation = infoCandidateSituation;
	}

}
