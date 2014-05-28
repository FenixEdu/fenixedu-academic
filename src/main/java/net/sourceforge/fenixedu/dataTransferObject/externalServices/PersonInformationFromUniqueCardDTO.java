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
package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class PersonInformationFromUniqueCardDTO {

    private static final Logger logger = LoggerFactory.getLogger(PersonInformationFromUniqueCardDTO.class);

    private String name;

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

    private byte[] photo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void print() {
        StringBuilder builder = new StringBuilder();

        builder.append("\nname: " + name);
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

}
