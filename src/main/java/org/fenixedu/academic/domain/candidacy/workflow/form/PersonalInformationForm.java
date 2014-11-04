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
package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.GrantOwnerType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ProfessionType;
import net.sourceforge.fenixedu.domain.ProfessionalSituationConditionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartySocialSecurityNumber;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitName;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PersonalInformationForm extends Form {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // Filiation
    private String name; // read only

    private String username; // read only

    private Gender gender; // read only

    private String documentIdNumber; // read only

    private IDDocumentType idDocumentType;

    private String identificationDocumentExtraDigit;

    private String identificationDocumentSeriesNumber;

    private String documentIdEmissionLocation;

    private YearMonthDay documentIdEmissionDate;

    private YearMonthDay documentIdExpirationDate;

    private String socialSecurityNumber;

    private ProfessionType professionType;

    private ProfessionalSituationConditionType professionalCondition;

    private String profession;

    private MaritalStatus maritalStatus;

    private GrantOwnerType grantOwnerType;

    private Unit grantOwnerProvider;

    private String grantOwnerProviderName;

    public PersonalInformationForm() {
        super();
    }

    private PersonalInformationForm(YearMonthDay documentIdEmissionDate, String documentIdEmissionLocation,
            YearMonthDay documentIdExpirationDate, String documentIdNumber, IDDocumentType documentType, Gender gender,
            MaritalStatus maritalStatus, String name, String profession, String socialSecurityNumber, String username,
            String identificationDocumentExtraDigit, String identificationDocumentSeriesNumber) {
        this();
        this.documentIdEmissionDate = documentIdEmissionDate;
        this.documentIdEmissionLocation = documentIdEmissionLocation;
        this.documentIdExpirationDate = documentIdExpirationDate;
        this.documentIdNumber = documentIdNumber;
        this.idDocumentType = documentType;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.name = name;
        this.profession = profession;
        this.socialSecurityNumber = socialSecurityNumber;
        this.username = username;
        this.professionType = ProfessionType.OTHER;
        this.professionalCondition = ProfessionalSituationConditionType.STUDENT;
        this.grantOwnerType = GrantOwnerType.STUDENT_WITHOUT_SCHOLARSHIP;
        this.identificationDocumentExtraDigit = identificationDocumentExtraDigit;
        this.identificationDocumentSeriesNumber = identificationDocumentSeriesNumber;
    }

    public static PersonalInformationForm createFromPerson(final Person person) {
        return new PersonalInformationForm(person.getEmissionDateOfDocumentIdYearMonthDay(),
                person.getEmissionLocationOfDocumentId(), person.getExpirationDateOfDocumentIdYearMonthDay(),
                person.getDocumentIdNumber(), person.getIdDocumentType(), person.getGender(), person.getMaritalStatus(),
                person.getName(), person.getProfession(), person.getSocialSecurityNumber(), person.getUsername(),
                person.getIdentificationDocumentExtraDigitValue(), person.getIdentificationDocumentSeriesNumberValue());
    }

    @Override
    public List<LabelFormatter> validate() {
        final List<LabelFormatter> result = new ArrayList<LabelFormatter>();

        checkGrantOwnerType(result);
        validateSocialSecurityNumber(result);
        return result;
    }

    private void validateSocialSecurityNumber(List<LabelFormatter> result) {
        final Party party = PartySocialSecurityNumber.readPartyBySocialSecurityNumber(socialSecurityNumber);
        final User user = User.findByUsername(username);
        if (party != null && party != user.getPerson()) {
            result.add(new LabelFormatter().appendLabel(
                    "error.candidacy.workflow.PersonalInformationForm.socialSecurityNumber.already.exists", "application"));
        }
    }

    private void checkGrantOwnerType(final List<LabelFormatter> result) {
        if (getGrantOwnerType().equals(GrantOwnerType.OTHER_INSTITUTION_GRANT_OWNER) && getGrantOwnerProvider() == null) {
            result.add(new LabelFormatter().appendLabel(
                    "error.candidacy.workflow.PersonalInformationForm.grant.owner.must.choose.granting.institution",
                    "application"));
        }
    }

    public YearMonthDay getDocumentIdEmissionDate() {
        return documentIdEmissionDate;
    }

    public void setDocumentIdEmissionDate(YearMonthDay documentIdEmissionDate) {
        this.documentIdEmissionDate = documentIdEmissionDate;
    }

    public String getDocumentIdEmissionLocation() {
        return documentIdEmissionLocation;
    }

    public void setDocumentIdEmissionLocation(String documentIdEmissionLocation) {
        this.documentIdEmissionLocation = documentIdEmissionLocation;
    }

    public YearMonthDay getDocumentIdExpirationDate() {
        return documentIdExpirationDate;
    }

    public void setDocumentIdExpirationDate(YearMonthDay documentIdExpirationDate) {
        this.documentIdExpirationDate = documentIdExpirationDate;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public IDDocumentType getIdDocumentType() {
        return idDocumentType;
    }

    public void setIdDocumentType(IDDocumentType documentType) {
        this.idDocumentType = documentType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ProfessionType getProfessionType() {
        return professionType;
    }

    public void setProfessionType(ProfessionType professionType) {
        this.professionType = professionType;
    }

    public ProfessionalSituationConditionType getProfessionalCondition() {
        return professionalCondition;
    }

    public void setProfessionalCondition(ProfessionalSituationConditionType professionalCondition) {
        this.professionalCondition = professionalCondition;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GrantOwnerType getGrantOwnerType() {
        return grantOwnerType;
    }

    public void setGrantOwnerType(GrantOwnerType grantOwnerType) {
        this.grantOwnerType = grantOwnerType;
    }

    public Unit getGrantOwnerProvider() {
        return this.grantOwnerProvider;
    }

    public void setGrantOwnerProvider(Unit grantOwnerProvider) {
        this.grantOwnerProvider = grantOwnerProvider;
    }

    public String getGrantOwnerProviderName() {
        return grantOwnerProviderName;
    }

    public void setGrantOwnerProviderName(String grantOwnerProviderName) {
        this.grantOwnerProviderName = grantOwnerProviderName;
    }

    public UnitName getGrantOwnerProviderUnitName() {
        return (grantOwnerProvider == null) ? null : grantOwnerProvider.getUnitName();
    }

    public void setGrantOwnerProviderUnitName(UnitName grantOwnerProviderUnitName) {
        this.grantOwnerProvider = (grantOwnerProviderUnitName == null) ? null : grantOwnerProviderUnitName.getUnit();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.personalInformationForm";
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