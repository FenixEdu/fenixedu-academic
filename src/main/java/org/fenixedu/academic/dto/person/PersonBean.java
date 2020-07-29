/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.District;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ProfessionType;
import org.fenixedu.academic.domain.ProfessionalSituationConditionType;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.contacts.MobilePhone;
import org.fenixedu.academic.domain.contacts.Phone;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.joda.time.YearMonthDay;

import com.google.common.base.Strings;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Angela Almeida (argelina@ist.utl.pt)
 *
 */
public class PersonBean implements Serializable {

    private Person person;

    // personal information
    private String name; // read only

    private String nickname;

    private String givenNames;

    private String familyNames;

    private String username; // read only

    private Gender gender; // read only

    private String documentIdNumber; // read only

    private IDDocumentType idDocumentType;

    private String identificationDocumentSeriesNumber;

    private String documentIdEmissionLocation;

    private YearMonthDay documentIdEmissionDate;

    private YearMonthDay documentIdExpirationDate;

    private Country fiscalCountry;

    private String socialSecurityNumber;

    private ProfessionType professionType;

    private ProfessionalSituationConditionType professionalCondition;

    private String profession;

    private MaritalStatus maritalStatus = MaritalStatus.UNKNOWN;

    // contacts
    private String phone;

    private String workPhone;

    private String mobile;

    private String email;

    private String webAddress;

    private boolean isEmailAvailable;

    private boolean isHomepageAvailable;

    // birth information
    private YearMonthDay dateOfBirth;

    private Country nationality;

    private Country secondNationality;

    private String parishOfBirth;

    private String districtSubdivisionOfBirth;

    private String districtOfBirth;

    private String fatherName;

    private String motherName;

    private Country countryOfBirth;

    // residence
    private String address;

    private String areaCode; // zip code

    private String areaOfAreaCode; // location of zip code

    private String area; // location

    private String parishOfResidence;

    private String districtSubdivisionOfResidence;

    private String districtOfResidence;

    private DistrictSubdivision districtSubdivisionOfResidenceObject;

    private Country countryOfResidence;

    // fiscal address input
    private String fiscalAddressAddress;

    private String fiscalAddressAreaCode; // zip code

    private String fiscalAddressParishOfResidence;

    private String fiscalAddressDistrictSubdivisionOfResidence;

    private String fiscalAddressDistrictOfResidence;

    private DistrictSubdivision fiscalAddressDistrictSubdivisionOfResidenceObject;

    private Country fiscalAddressCountryOfResidence;

    
    private Integer studentNumber;

    private String emailConfirmation;

    private String eidentifier;

    private boolean usePhysicalAddress = true;
    
    private PhysicalAddress fiscalAddress;
    
    private PhysicalAddressForFiscalDataInCreateRegistrationBean fiscalAddressInCreateRegistrationBean;
    
    public PersonBean() {
        super();
    }

    public PersonBean(final String name, final String identificationNumber, final IDDocumentType idDocumentType,
            final YearMonthDay dateOfBirth) {
        setName(name);
        setDocumentIdNumber(identificationNumber);
        setIdDocumentType(idDocumentType);
        setDateOfBirth(dateOfBirth);
    }

    public PersonBean(final String name, final String identificationNumber, final IDDocumentType idDocumentType,
            final YearMonthDay dateOfBirth, final Integer studentNumber) {
        setName(name);
        setDocumentIdNumber(identificationNumber);
        setIdDocumentType(idDocumentType);
        setDateOfBirth(dateOfBirth);
        setStudentNumber(studentNumber);
    }

    public PersonBean(final Person person) {
        initPerson(person);
    }

    private void initPerson(final Person person) {
        setName(person.getName());
        setNickname(person.getNickname());
        setGivenNames(person.getGivenNames());
        setFamilyNames(person.getFamilyNames());
        setUsername(person.getUsername());
        setGender(person.getGender());
        setMaritalStatus(person.getMaritalStatus());
        setFatherName(person.getNameOfFather());
        setMotherName(person.getNameOfMother());
        setProfession(person.getProfession());
        setNationality(person.getCountry());
        setSecondNationality(person.getSecondNationality());

        setCountryOfBirth(person.getCountryOfBirth());
        setDateOfBirth(person.getDateOfBirthYearMonthDay());
        setParishOfBirth(person.getParishOfBirth());
        setDistrictOfBirth(person.getDistrictOfBirth());
        setDistrictSubdivisionOfBirth(person.getDistrictSubdivisionOfBirth());

        setDocumentIdEmissionDate(person.getEmissionDateOfDocumentIdYearMonthDay());
        setDocumentIdEmissionLocation(person.getEmissionLocationOfDocumentId());
        setDocumentIdExpirationDate(person.getExpirationDateOfDocumentIdYearMonthDay());
        setDocumentIdNumber(person.getDocumentIdNumber());
        setIdDocumentType(person.getIdDocumentType());
        setIdentificationDocumentSeriesNumber(person.getIdentificationDocumentSeriesNumber());
        setFiscalCountry(person.getFiscalCountry());
        setSocialSecurityNumber(person.getSocialSecurityNumber());

        if (person.hasDefaultPhysicalAddress()) {
            final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
            setAddress(physicalAddress.getAddress());
            setArea(physicalAddress.getArea());
            setAreaCode(physicalAddress.getAreaCode());
            setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
            setParishOfResidence(physicalAddress.getParishOfResidence());
            setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
            setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
            setCountryOfResidence(physicalAddress.getCountryOfResidence());
            
            if(getCountryOfResidence() != null && getCountryOfResidence().isDefaultCountry()) {
                if(StringUtils.isNotEmpty(getDistrictOfResidence()) && StringUtils.isNotEmpty(getDistrictSubdivisionOfResidence())) {
                    District district = District.readByName(getDistrictOfResidence());
                    setDistrictSubdivisionOfResidenceObject(district != null ? district.getDistrictSubdivisionByName(getDistrictSubdivisionOfResidence()) : null);
                }
            }
        }

        setPhone(person.hasDefaultPhone() ? person.getDefaultPhone().getNumber() : null);
        setMobile(person.hasDefaultMobilePhone() ? person.getDefaultMobilePhone().getNumber() : null);
        setWebAddress(person.hasDefaultWebAddress() ? person.getDefaultWebAddress().getUrl() : null);

        setEmail(person.getEmail());

        setEmailAvailable(person.getAvailableEmail());
        setHomepageAvailable(person.getAvailableWebSite());

        setPerson(person);

        setIdentificationDocumentSeriesNumber(person.getIdentificationDocumentSeriesNumberValue());

        if (Strings.isNullOrEmpty(getIdentificationDocumentSeriesNumber())) {
            setIdentificationDocumentSeriesNumber(person.getIdentificationDocumentExtraDigitValue());
        }
        
        setFiscalAddress(person.getFiscalAddress());
        if(person.getFiscalAddress() != null) {
            setFiscalAddressInCreateRegistrationBean(new PhysicalAddressForFiscalDataInCreateRegistrationBean(person.getFiscalAddress()));
        }
        
    }

    public String getUiResidenceAddressForFiscalPresentationValue() {
        final List<String> compounds = new ArrayList<>();
        
        if(StringUtils.isNotEmpty(getAddress())) {
            compounds.add(getAddress());
        }
        
        if(StringUtils.isNotEmpty(getAreaCode())) {
            compounds.add(getAreaCode());
        }
        
        if(getCountryOfResidence() != null && getCountryOfResidence().isDefaultCountry()) {
            if(StringUtils.isNotEmpty(getDistrictSubdivisionOfResidenceObject() != null ? getDistrictSubdivisionOfResidenceObject()
                    .getName() : getDistrictSubdivisionOfResidence())) {
                compounds.add(getDistrictSubdivisionOfResidenceObject() != null ? getDistrictSubdivisionOfResidenceObject()
                        .getName() : getDistrictSubdivisionOfResidence());
            }
        } else {
            if(StringUtils.isNotEmpty(getDistrictSubdivisionOfResidence())) {
                compounds.add(getDistrictSubdivisionOfResidence());
            }
        }
        
        if(getCountryOfResidence() != null) {
            compounds.add(getCountryOfResidence().getLocalizedName().getContent());
        }
        
        return String.join(" ", compounds);
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(final String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(final String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(final String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public Country getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(final Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public Country getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(final Country countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public boolean hasCountryOfResidence() {
        return getCountryOfResidence() != null;
    }

    public YearMonthDay getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final YearMonthDay dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDistrictOfBirth() {
        return districtOfBirth;
    }

    public void setDistrictOfBirth(final String districtOfBirth) {
        this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictOfResidence() {
        return this.districtOfResidence;
    }

    public void setDistrictOfResidence(final String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
    }

    public DistrictSubdivision getDistrictSubdivisionOfResidenceObject() {
        return this.districtSubdivisionOfResidenceObject;
    }

    public void setDistrictSubdivisionOfResidenceObject(final DistrictSubdivision districtSubdivision) {
        this.districtSubdivisionOfResidenceObject = districtSubdivision;
    }

    public String getDistrictSubdivisionOfBirth() {
        return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(final String districtSubdivisionOfBirth) {
        this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(final String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public YearMonthDay getDocumentIdEmissionDate() {
        return documentIdEmissionDate;
    }

    public void setDocumentIdEmissionDate(final YearMonthDay documentIdEmissionDate) {
        this.documentIdEmissionDate = documentIdEmissionDate;
    }

    public String getDocumentIdEmissionLocation() {
        return documentIdEmissionLocation;
    }

    public void setDocumentIdEmissionLocation(final String documentIdEmissionLocation) {
        this.documentIdEmissionLocation = documentIdEmissionLocation;
    }

    public YearMonthDay getDocumentIdExpirationDate() {
        return documentIdExpirationDate;
    }

    public void setDocumentIdExpirationDate(final YearMonthDay documentIdExpirationDate) {
        this.documentIdExpirationDate = documentIdExpirationDate;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(final String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getIdentificationDocumentSeriesNumber() {
        return identificationDocumentSeriesNumber;
    }

    public void setIdentificationDocumentSeriesNumber(final String identificationDocumentSeriesNumber) {
        this.identificationDocumentSeriesNumber = identificationDocumentSeriesNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(final String fatherName) {
        this.fatherName = fatherName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public IDDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(final IDDocumentType idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    public boolean isEmailAvailable() {
        return isEmailAvailable;
    }

    public void setEmailAvailable(final boolean isEmailAvailable) {
        this.isEmailAvailable = isEmailAvailable;
    }

    public boolean isHomepageAvailable() {
        return isHomepageAvailable;
    }

    public void setHomepageAvailable(final boolean isHomepageAvailable) {
        this.isHomepageAvailable = isHomepageAvailable;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus == null ? MaritalStatus.UNKNOWN : maritalStatus;
    }

    public void setMaritalStatus(final MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String mobileNumber) {
        this.mobile = mobileNumber;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(final String motherName) {
        this.motherName = motherName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(final String givenNames) {
        this.givenNames = givenNames;
    }

    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(final String familyNames) {
        this.familyNames = familyNames;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(final Country nationality) {
        this.nationality = nationality;
    }

    public Country getSecondNationality() {
        return secondNationality;
    }

    public void setSecondNationality(final Country secondNationality) {
        this.secondNationality = secondNationality;
    }

    public String getParishOfBirth() {
        return parishOfBirth;
    }

    public void setParishOfBirth(final String parishOfBirth) {
        this.parishOfBirth = parishOfBirth;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public void setParishOfResidence(final String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(final String workPhone) {
        this.workPhone = workPhone;
    }

    public ProfessionType getProfessionType() {
        return professionType;
    }

    public void setProfessionType(final ProfessionType professionType) {
        this.professionType = professionType;
    }

    public ProfessionalSituationConditionType getProfessionalCondition() {
        return professionalCondition;
    }

    public void setProfessionalCondition(final ProfessionalSituationConditionType professionalCondition) {
        this.professionalCondition = professionalCondition;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(final String profession) {
        this.profession = profession;
    }

    public Country getFiscalCountry() {
        return fiscalCountry;
    }

    public void setFiscalCountry(final Country fiscalCountry) {
        this.fiscalCountry = fiscalCountry;
    }

    /*
     * 08/05/2009 - VAT Number and Social Security Number is the same thing
     */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(final String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getFiscalCode() {
        return socialSecurityNumber;
    }

    public void setFiscalCode(final String value) {
        this.socialSecurityNumber = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(final String webAddress) {
        this.webAddress = webAddress;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(final Person person) {
        this.person = person;
    }

    public List<PhysicalAddress> getSortedPhysicalAdresses() {
        final List<PhysicalAddress> result = getPerson().getPendingOrValidPhysicalAddresses();
        Collections.sort(result, PhysicalAddress.COMPARATOR_BY_ADDRESS);
        return result;
    }
    
    public List<PhysicalAddress> getSortedValidAddressesForFiscalData() {
        final List<PhysicalAddress> result = new ArrayList<PhysicalAddress>();
        
        result.addAll(getPerson().getValidAddressesForFiscalData());
        
        Collections.sort(result, PhysicalAddress.COMPARATOR_BY_ADDRESS);
        return result;
    }
    
    // Necessary for create registration, the default address can be changed in fillNewPersonData
    public List<PhysicalAddressForFiscalDataInCreateRegistrationBean> getSortedValidAddressesBeansForFiscalDataInCreateRegistration() {
        List<PhysicalAddressForFiscalDataInCreateRegistrationBean> result = new ArrayList<PhysicalAddressForFiscalDataInCreateRegistrationBean>();
        
        for (final PhysicalAddress physicalAddress : getPerson().getValidAddressesForFiscalData()) {
            PhysicalAddressForFiscalDataInCreateRegistrationBean bean = new PhysicalAddressForFiscalDataInCreateRegistrationBean(physicalAddress);
            
            if(physicalAddress.isDefault()) {
                bean.setAddress(getAddress());
                bean.setArea(getArea());
                bean.setAreaCode(getAreaCode());
                bean.setAreaOfAreaCode(getAreaOfAreaCode());
                bean.setCountryOfResidence(getCountryOfResidence());
                bean.setParishOfResidence(bean.getParishOfResidence());
                bean.setDistrictSubdivisionOfResidence(bean.getDistrictSubdivisionOfResidence());
                bean.setDistrictOfResidence(bean.getDistrictOfResidence());
            }
            
            result.add(bean);
        }
        
        Collections.sort(result, PhysicalAddressForFiscalDataInCreateRegistrationBean.COMPARATOR_BY_ADDRESS);

        return result;
    }
    
    public PhysicalAddressData getPhysicalAddressData() {
        String address = getAddress();
        String areaCode = getAreaCode();
        String areaOfAreaCode = getAreaOfAreaCode();
        String area = getArea();
        String parishOfResidence = getParishOfResidence();
        
        String districtSubdivisionOfResidence = null;
        String districtOfResidence = null;
        if(getCountryOfResidence() != null && getCountryOfResidence().isDefaultCountry()) {
            districtSubdivisionOfResidence = getDistrictSubdivisionOfResidenceObject() != null ? getDistrictSubdivisionOfResidenceObject()
                    .getName() : getDistrictSubdivisionOfResidence();
            districtOfResidence = getDistrictSubdivisionOfResidenceObject() != null ? getDistrictSubdivisionOfResidenceObject().getDistrict()
                    .getName() : getDistrictOfResidence();
        } else {
            districtSubdivisionOfResidence = getDistrictSubdivisionOfResidence();
            districtOfResidence = getDistrictOfResidence();
        }
                
        Country countryOfResidence = getCountryOfResidence();
        
        return new PhysicalAddressData(address, areaCode, areaOfAreaCode, area, parishOfResidence,
                districtSubdivisionOfResidence, districtOfResidence, countryOfResidence);
    }
    
    public boolean isPhysicalAddressDataNotEmpty() {
        PhysicalAddressData physicalAddressData = getPhysicalAddressData();
        
        boolean result = false;
        
        result |= StringUtils.isNotEmpty(physicalAddressData.getAddress());
        result |= StringUtils.isNotEmpty(physicalAddressData.getAreaCode());
        result |= StringUtils.isNotEmpty(physicalAddressData.getAreaOfAreaCode());
        result |= StringUtils.isNotEmpty(physicalAddressData.getArea());
        result |= StringUtils.isNotEmpty(physicalAddressData.getParishOfResidence());
        result |= StringUtils.isNotEmpty(physicalAddressData.getDistrictOfResidence());
        result |= StringUtils.isNotEmpty(physicalAddressData.getDistrictSubdivisionOfResidence());
        result |= physicalAddressData.getCountryOfResidence() != null;
        
        return result;
    }

    public List<Phone> getSortedPhones() {
        final List<Phone> result = getPerson().getPendingOrValidPhones();
        Collections.sort(result, Phone.COMPARATOR_BY_NUMBER);
        return result;
    }

    public List<MobilePhone> getSortedMobilePhones() {
        final List<MobilePhone> result = getPerson().getPendingOrValidMobilePhones();
        Collections.sort(result, MobilePhone.COMPARATOR_BY_NUMBER);
        return result;
    }

    public List<EmailAddress> getSortedEmailAddresses() {
        final List<EmailAddress> result = getPerson().getPendingOrValidEmailAddresses();
        Collections.sort(result, EmailAddress.COMPARATOR_BY_EMAIL);
        return result;
    }

    public List<WebAddress> getSortedWebAddresses() {
        final List<WebAddress> result = getPerson().getWebAddresses();
        Collections.sort(result, WebAddress.COMPARATOR_BY_URL);
        return result;
    }

    public boolean hasPerson() {
        return getPerson() != null;
    }

    public boolean hasStudent() {
        return hasPerson() && getPerson().getStudent() != null;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(final Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getEmailConfirmation() {
        return this.emailConfirmation;
    }

    public void setEmailConfirmation(final String value) {
        this.emailConfirmation = value;
    }

    public String getEidentifier() {
        return this.eidentifier;
    }

    public void setEidentifier(final String eidentifier) {
        this.eidentifier = eidentifier;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }
    
    public PhysicalAddress getFiscalAddress() {
        return fiscalAddress;
    }
    
    public void setFiscalAddress(PhysicalAddress fiscalAddress) {
        this.fiscalAddress = fiscalAddress;
    }
    
    public boolean isUsePhysicalAddress() {
        return usePhysicalAddress;
    }
    
    public void setUsePhysicalAddress(boolean usePhysicalAddress) {
        this.usePhysicalAddress = usePhysicalAddress;
    }

    public String getFiscalAddressAddress() {
        return fiscalAddressAddress;
    }

    public void setFiscalAddressAddress(String fiscalAddressAddress) {
        this.fiscalAddressAddress = fiscalAddressAddress;
    }

    public String getFiscalAddressAreaCode() {
        return fiscalAddressAreaCode;
    }

    public void setFiscalAddressAreaCode(String fiscalAddressAreaCode) {
        this.fiscalAddressAreaCode = fiscalAddressAreaCode;
    }

    public String getFiscalAddressDistrictSubdivisionOfResidence() {
        return fiscalAddressDistrictSubdivisionOfResidence;
    }

    public void setFiscalAddressDistrictSubdivisionOfResidence(String fiscalAddressDistrictSubdivisionOfResidence) {
        this.fiscalAddressDistrictSubdivisionOfResidence = fiscalAddressDistrictSubdivisionOfResidence;
    }

    public String getFiscalAddressDistrictOfResidence() {
        return fiscalAddressDistrictOfResidence;
    }

    public void setFiscalAddressDistrictOfResidence(String fiscalAddressDistrictOfResidence) {
        this.fiscalAddressDistrictOfResidence = fiscalAddressDistrictOfResidence;
    }

    public DistrictSubdivision getFiscalAddressDistrictSubdivisionOfResidenceObject() {
        return fiscalAddressDistrictSubdivisionOfResidenceObject;
    }

    public void setFiscalAddressDistrictSubdivisionOfResidenceObject(
            DistrictSubdivision fiscalAddressDistrictSubdivisionOfResidenceObject) {
        this.fiscalAddressDistrictSubdivisionOfResidenceObject = fiscalAddressDistrictSubdivisionOfResidenceObject;
    }

    public Country getFiscalAddressCountryOfResidence() {
        return fiscalAddressCountryOfResidence;
    }

    public void setFiscalAddressCountryOfResidence(Country fiscalAddressCountryOfResidence) {
        this.fiscalAddressCountryOfResidence = fiscalAddressCountryOfResidence;
    }

    public String getFiscalAddressParishOfResidence() {
        return fiscalAddressParishOfResidence;
    }
    
    public void setFiscalAddressParishOfResidence(String fiscalAddressParishOfResidence) {
        this.fiscalAddressParishOfResidence = fiscalAddressParishOfResidence;
    }
    
    public PhysicalAddressForFiscalDataInCreateRegistrationBean getFiscalAddressInCreateRegistrationBean() {
        return fiscalAddressInCreateRegistrationBean;
    }
    
    public void setFiscalAddressInCreateRegistrationBean(
            PhysicalAddressForFiscalDataInCreateRegistrationBean fiscalAddressInCreateRegistrationBean) {
        this.fiscalAddressInCreateRegistrationBean = fiscalAddressInCreateRegistrationBean;
    }
    
    public Person save() {
        return save(getPerson());
    }

    @Atomic(mode = TxMode.WRITE)
    public Person save(final Person person) {
        person.getProfile().changeName(this.getGivenNames(), this.getFamilyNames(), null);

        person.setGender(this.getGender());
        person.setProfession(this.getProfession());
        person.setMaritalStatus(this.getMaritalStatus());

        // identification
        person.setIdentification(this.getDocumentIdNumber(), this.getIdDocumentType());
        person.setIdentificationDocumentSeriesNumber(getIdentificationDocumentSeriesNumber());
        person.setEmissionLocationOfDocumentId(this.getDocumentIdEmissionLocation());
        person.setEmissionDateOfDocumentIdYearMonthDay(this.getDocumentIdEmissionDate());
        person.setExpirationDateOfDocumentIdYearMonthDay(this.getDocumentIdExpirationDate());
        person.setEidentifier(this.getEidentifier());

        // filiation
        person.setDateOfBirthYearMonthDay(this.getDateOfBirth());
        person.setCountry(this.getNationality());
        person.setSecondNationality(this.secondNationality);
        person.setParishOfBirth(this.getParishOfBirth());
        person.setDistrictSubdivisionOfBirth(this.getDistrictSubdivisionOfBirth());
        person.setDistrictOfBirth(this.getDistrictOfBirth());
        person.setCountryOfBirth(this.getCountryOfBirth());
        person.setNameOfMother(this.getMotherName());
        person.setNameOfFather(this.getFatherName());

        person.setDefaultPhysicalAddressData(this.getPhysicalAddressData(), true);
        person.setDefaultPhoneNumber(this.getPhone());
        person.setDefaultMobilePhoneNumber(this.getMobile());
        person.setDefaultWebAddressUrl(this.getWebAddress());
        person.setDefaultEmailAddressValue(this.getEmail(), true);
        return person;
    }
}
