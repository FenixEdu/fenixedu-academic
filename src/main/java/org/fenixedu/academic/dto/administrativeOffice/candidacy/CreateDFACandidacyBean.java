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
package org.fenixedu.academic.dto.administrativeOffice.candidacy;

import java.io.Serializable;

import org.fenixedu.academic.domain.person.IDDocumentType;
import org.joda.time.YearMonthDay;

public class CreateDFACandidacyBean extends DFACandidacyBean implements Serializable {

    private String givenNames;

    private String familyNames;

    private String identificationNumber;

    private IDDocumentType idDocumentType;

    private String contributorNumber;

    private YearMonthDay candidacyDate = new YearMonthDay();

    public IDDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType idDocumentType) {
        this.idDocumentType = idDocumentType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

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

    public String getContributorNumber() {
        return contributorNumber;
    }

    public void setContributorNumber(String contributorNumber) {
        this.contributorNumber = contributorNumber;
    }

    public YearMonthDay getCandidacyDate() {
        return candidacyDate;
    }

    public void setCandidacyDate(YearMonthDay startDate) {
        this.candidacyDate = startDate;
    }

}
