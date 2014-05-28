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
package net.sourceforge.fenixedu.domain.phd.migration;

import java.io.Serializable;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.NationalityTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.joda.time.LocalDate;

public class PhdMigrationIndividualPersonalDataBean implements Serializable {

    private static final long serialVersionUID = 1863846900977920325L;

    private String data;

    private Person chosenPersonManually;

    private Integer phdStudentNumber;
    private String identificationNumber;
    private String socialSecurityNumber;
    private String fullName;
    private String familyName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private Country nationality;

    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;

    private String fatherName;
    private String motherName;

    private String address;
    private String areaCode;
    private String area;
    private String areaOfAreaCode;

    private String contactNumber;
    private String otherContactNumber;
    private String profession;
    private String workPlace;
    private String email;

    public PhdMigrationIndividualPersonalDataBean(String data) {
        setData(data);
        parse();
    }

    public void parse() {
        String[] fields = getData().split("\t");

        try {
            try {
                phdStudentNumber = Integer.valueOf(fields[0].trim());
            } catch (NumberFormatException e) {
                throw new IncompleteFieldsException("processNumber");
            }
            identificationNumber = fields[1].trim();
            socialSecurityNumber = parseSocialSecurityNumber(fields[2].trim());
            fullName = StringFormatter.prettyPrint(fields[3].trim());
            familyName = StringFormatter.prettyPrint(fields[4].trim());
            dateOfBirth = ConversionUtilities.parseDate(fields[5].trim());
            gender = ConversionUtilities.parseGender(fields[6].trim());
            nationality = NationalityTranslator.translate(fields[7].trim());

            // Address
            parishOfResidence = fields[8].trim();
            districtSubdivisionOfResidence = fields[9].trim();
            districtOfResidence = fields[10].trim();

            fatherName = fields[11].trim();
            motherName = fields[12].trim();

            address = fields[13].trim();
            areaCode = fields[14].trim();
            area = fields[15].trim();
            areaOfAreaCode = area;

            // -- Address

            contactNumber = fields[16].trim();
            otherContactNumber = fields[17].trim();
            profession = fields[18].trim();
            workPlace = fields[19].trim();
            email = fields[20].trim();

        } catch (NoSuchElementException e) {
            throw new IncompleteFieldsException("Not enough fields");
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPhdStudentNumber() {
        return phdStudentNumber;
    }

    public void setPhdStudentNumber(Integer phdStudentNumber) {
        this.phdStudentNumber = phdStudentNumber;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public void setParishOfResidence(String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public String getDistrictOfResidence() {
        return districtOfResidence;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getOtherContactNumber() {
        return otherContactNumber;
    }

    public void setOtherContactNumber(String otherContactNumber) {
        this.otherContactNumber = otherContactNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Person getChosenPersonManually() {
        return chosenPersonManually;
    }

    public void setChosenPersonManually(Person chosenPersonManually) {
        this.chosenPersonManually = chosenPersonManually;
    }

    public boolean hasChosenPersonManually() {
        return this.chosenPersonManually != null;
    }

    private String parseSocialSecurityNumber(String socialSecurityNumber) {
        if (socialSecurityNumber.matches("/--+|0+/")) {
            return null;
        }

        return socialSecurityNumber;
    }

}