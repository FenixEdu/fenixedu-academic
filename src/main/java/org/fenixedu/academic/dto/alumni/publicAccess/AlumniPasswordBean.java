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
package net.sourceforge.fenixedu.dataTransferObject.alumni.publicAccess;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.AlumniRequestType;

import org.joda.time.YearMonthDay;

public class AlumniPasswordBean implements Serializable {

    private Alumni alumni;
    private String contactEmail;
    private String documentIdNumber;
    private String fullName;
    private YearMonthDay dateOfBirthYearMonthDay;
    private String districtOfBirth;
    private String districtSubdivisionOfBirth;
    private String parishOfBirth;
    private String socialSecurityNumber;
    private String nameOfFather;
    private String nameOfMother;
    private String password;
    private String passwordConfirmation;
    private AlumniRequestType requestType;

    public AlumniPasswordBean(Alumni alumni) {
        setAlumni(alumni);
        setPassword("");
        setPasswordConfirmation("");
    }

    public AlumniPasswordBean(Alumni alumni, AlumniRequestType requestType) {
        this(alumni);
        setRequestType(requestType);
    }

    public Alumni getAlumni() {
        return this.alumni;
    }

    public void setAlumni(Alumni alumni) {
        this.alumni = alumni;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public YearMonthDay getDateOfBirthYearMonthDay() {
        return dateOfBirthYearMonthDay;
    }

    public void setDateOfBirthYearMonthDay(YearMonthDay dateOfBirthYearMonthDay) {
        this.dateOfBirthYearMonthDay = dateOfBirthYearMonthDay;
    }

    public String getDistrictOfBirth() {
        return districtOfBirth;
    }

    public void setDistrictOfBirth(String districtOfBirth) {
        this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictSubdivisionOfBirth() {
        return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
        this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
        this.parishOfBirth = parishOfBirth;
    }

    public String getParishOfBirth() {
        return parishOfBirth;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getNameOfFather() {
        return nameOfFather;
    }

    public void setNameOfFather(String nameOfFather) {
        this.nameOfFather = nameOfFather;
    }

    public String getNameOfMother() {
        return nameOfMother;
    }

    public void setNameOfMother(String nameOfMother) {
        this.nameOfMother = nameOfMother;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public AlumniRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(AlumniRequestType requestType) {
        this.requestType = requestType;
    }
}
