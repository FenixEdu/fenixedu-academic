/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package pt.ist.fenix.external;

import java.text.ParseException;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

import com.google.common.io.BaseEncoding;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonInformationFromUniqueCardDTO {

    private static final Logger logger = LoggerFactory.getLogger(PersonInformationFromUniqueCardDTO.class);

    private String givenNames;

    private String familyNames;

    private String gender;

    private String documentIdNumber;

    private String identificationDocumentExtraDigit;

    private String identificationDocumentSeriesNumber;

    private String documentIdEmissionLocation;

    private String documentIdEmissionDate;

    private String documentIdExpirationDate;

    private String fiscalNumber;

    private String birthDate;

    private String nationality;

    private String fatherName;

    private String motherName;

    private String country;

    private String address;

    private String postalCode;

    private String postalArea;

    private String locality;

    private String parish;

    private String municipality;

    private String district;

    private String photo;

    public String getGivenNames() {
        return givenNames;
    }

    public void setGivenNames(String givenNames) {
        this.givenNames = givenNames;
    }

    public String getFamilyNames() {
        return familyNames;
    }

    public void setFamilyNames(String familyNames) {
        this.familyNames = familyNames;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getDocumentIdEmissionLocation() {
        return documentIdEmissionLocation;
    }

    public void setDocumentIdEmissionLocation(String documentIdEmissionLocation) {
        this.documentIdEmissionLocation = documentIdEmissionLocation;
    }

    public String getDocumentIdEmissionDate() {
        return documentIdEmissionDate;
    }

    public void setDocumentIdEmissionDate(String documentIdEmissionDate) {
        this.documentIdEmissionDate = documentIdEmissionDate;
    }

    public String getDocumentIdExpirationDate() {
        return documentIdExpirationDate;
    }

    public void setDocumentIdExpirationDate(String documentIdExpirationDate) {
        this.documentIdExpirationDate = documentIdExpirationDate;
    }

    public String getFiscalNumber() {
        return fiscalNumber;
    }

    public void setFiscalNumber(String fiscalNumber) {
        this.fiscalNumber = fiscalNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalArea() {
        return postalArea;
    }

    public void setPostalArea(String postalArea) {
        this.postalArea = postalArea;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getParish() {
        return parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void print() {
        StringBuilder builder = new StringBuilder();

        builder.append("\ngivenNames: " + givenNames);
        builder.append("\nfamilyNames: " + familyNames);
        builder.append("\ngender: " + gender);
        builder.append("\ndocumentIdNumber: " + documentIdNumber);
        builder.append("\ndocumentIdEmissionLocation: " + documentIdEmissionLocation);
        builder.append("\ndocumentIdEmissionDate: " + documentIdEmissionDate);
        builder.append("\ndocumentIdExpirationDate: " + documentIdExpirationDate);
        builder.append("\nfiscalNumber: " + fiscalNumber);
        builder.append("\nbirthDate: " + birthDate);
        builder.append("\nnationality: " + nationality);
        builder.append("\nfatherName: " + fatherName);
        builder.append("\nmotherName: " + motherName);
        builder.append("\ncountry: " + country);
        builder.append("\naddress: " + address);
        builder.append("\npostalCode: " + postalCode);
        builder.append("\npostalArea: " + postalArea);
        builder.append("\nlocality: " + locality);
        builder.append("\nparish: " + parish);
        builder.append("\nmunicipality: " + municipality);
        builder.append("\ndistrict: " + district);
        builder.append("\nphoto: " + (photo != null));

        logger.info(builder.toString());
    }

    public String getIdentificationDocumentExtraDigit() {
        return identificationDocumentExtraDigit;
    }

    public void setIdentificationDocumentExtraDigit(String identificationDocumentExtraDigit) {
        this.identificationDocumentExtraDigit = identificationDocumentExtraDigit;
    }

    public String getIdentificationDocumentSeriesNumber() {
        return identificationDocumentSeriesNumber;
    }

    public void setIdentificationDocumentSeriesNumber(String identificationDocumentSeriesNumber) {
        this.identificationDocumentSeriesNumber = identificationDocumentSeriesNumber;
    }

    @Atomic
    public void edit(Person person) throws ParseException {
        final String dateFormat = "dd MM yyyy";

        person.ensureUserProfile();
        person.getProfile().changeName(StringFormatter.prettyPrint(getGivenNames()),
                StringFormatter.prettyPrint(getFamilyNames()), null);

        if (!StringUtils.isEmpty(getGender())) {
            person.setGender(getGender().equalsIgnoreCase("m") ? Gender.MALE : Gender.FEMALE);
        }

        if (getIdentificationDocumentExtraDigit() != null) {
            person.setIdentificationDocumentExtraDigit(getIdentificationDocumentExtraDigit().replaceAll("\\s", "")); //remove white spaces
        }
        if (getIdentificationDocumentSeriesNumber() != null) {
            person.setIdentificationDocumentSeriesNumber(getIdentificationDocumentSeriesNumber().replaceAll("\\s", "")); //remove white spaces
        }

        if (!StringUtils.isEmpty(getDocumentIdEmissionLocation())) {
            person.setEmissionLocationOfDocumentId(getDocumentIdEmissionLocation());
        }
        if (!StringUtils.isEmpty(getDocumentIdEmissionDate())) {
            person.setEmissionDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat,
                    getDocumentIdEmissionDate())));
        }
        if (!StringUtils.isEmpty(getDocumentIdExpirationDate())) {
            person.setExpirationDateOfDocumentIdYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat,
                    getDocumentIdExpirationDate())));
        }
        if (!StringUtils.isEmpty(getFiscalNumber())) {
            person.setSocialSecurityNumber(getFiscalNumber());
        }
        if (!StringUtils.isEmpty(getBirthDate())) {
            person.setDateOfBirthYearMonthDay(YearMonthDay.fromDateFields(DateFormatUtil.parse(dateFormat, getBirthDate())));
        }
        if (!StringUtils.isEmpty(getNationality())) {
            person.setNationality(Country.readByThreeLetterCode(getNationality()));
        }
        if (!StringUtils.isEmpty(getMotherName())) {
            person.setNameOfMother(StringFormatter.prettyPrint(getMotherName()));
        }
        if (!StringUtils.isEmpty(getFatherName())) {
            person.setNameOfFather(StringFormatter.prettyPrint(getFatherName()));
        }

        if (getPhoto() != null) {
            person.setPersonalPhoto(new Photograph(PhotoType.INSTITUTIONAL, ContentType.JPG, new ByteArray(BaseEncoding.base64()
                    .decode(getPhoto()))));
        }

        final PhysicalAddressData physicalAddress = new PhysicalAddressData();
        physicalAddress.setAddress(StringFormatter.prettyPrint(getAddress()));
        physicalAddress.setAreaCode(getPostalCode());
        physicalAddress.setAreaOfAreaCode(StringFormatter.prettyPrint(getPostalArea()));
        physicalAddress.setArea(StringFormatter.prettyPrint(getLocality()));
        physicalAddress.setParishOfResidence(StringFormatter.prettyPrint(getParish()));
        physicalAddress.setDistrictSubdivisionOfResidence(StringFormatter.prettyPrint(getMunicipality()));
        physicalAddress.setDistrictOfResidence(StringFormatter.prettyPrint(getDistrict()));
        physicalAddress.setCountryOfResidence(Country.readByTwoLetterCode(getCountry()));

        if (!physicalAddress.isEmpty()) {
            person.setDefaultPhysicalAddressData(physicalAddress, true);
        }
    }

}
