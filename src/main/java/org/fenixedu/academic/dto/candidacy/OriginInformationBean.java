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
/**
 * 
 */
package org.fenixedu.academic.dto.candidacy;

import java.io.Serializable;

import org.fenixedu.academic.domain.District;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.GrantOwnerType;
import org.fenixedu.academic.domain.ProfessionType;
import org.fenixedu.academic.domain.ProfessionalSituationConditionType;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.organizationalStructure.AcademicalInstitutionType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitName;
import org.fenixedu.academic.dto.person.PersonBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class OriginInformationBean implements Serializable {

    /*
     * if foreign dislocated
     * 
     * if dislocated schoolTimeDistrictSubdivisionOfResidence != null
     */

    private Boolean dislocatedFromPermanentResidence;

    private District schoolTimeDistrictOfResidence;// nonpersistent

    private DistrictSubdivision schoolTimeDistrictSubdivisionOfResidence;

    private GrantOwnerType grantOwnerType;

    private Unit grantOwnerProvider;

    private String grantOwnerProviderName;

    private Integer numberOfCandidaciesToHigherSchool;

    private Integer numberOfFlunksOnHighSchool;

    private AcademicalInstitutionType highSchoolType;

    private SchoolLevelType motherSchoolLevel;

    private ProfessionType motherProfessionType;

    private ProfessionalSituationConditionType motherProfessionalCondition;

    private SchoolLevelType fatherSchoolLevel;

    private ProfessionType fatherProfessionType;

    private ProfessionalSituationConditionType fatherProfessionalCondition;

    private SchoolLevelType spouseSchoolLevel;

    private ProfessionType spouseProfessionType;

    private ProfessionalSituationConditionType spouseProfessionalCondition;

    public OriginInformationBean(PersonBean personBean) {
        if (personBean.hasCountryOfResidence() && !personBean.getCountryOfResidence().isDefaultCountry()) {
            setDislocatedFromPermanentResidence(true);
        }
    }

    public Boolean getDislocatedFromPermanentResidence() {
        return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
        this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public District getSchoolTimeDistrictOfResidence() {
        return this.schoolTimeDistrictOfResidence;
    }

    public void setSchoolTimeDistrictOfResidence(District district) {
        this.schoolTimeDistrictOfResidence = district;
    }

    public DistrictSubdivision getSchoolTimeDistrictSubdivisionOfResidence() {
        return this.schoolTimeDistrictSubdivisionOfResidence;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
        this.schoolTimeDistrictSubdivisionOfResidence = districtSubdivision;
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

    public Integer getNumberOfCandidaciesToHigherSchool() {
        return numberOfCandidaciesToHigherSchool;
    }

    public void setNumberOfCandidaciesToHigherSchool(Integer numberOfCandidaciesToHigherSchool) {
        this.numberOfCandidaciesToHigherSchool = numberOfCandidaciesToHigherSchool;
    }

    public Integer getNumberOfFlunksOnHighSchool() {
        return numberOfFlunksOnHighSchool;
    }

    public void setNumberOfFlunksOnHighSchool(Integer numberOfFlunksOnHighSchool) {
        this.numberOfFlunksOnHighSchool = numberOfFlunksOnHighSchool;
    }

    public AcademicalInstitutionType getHighSchoolType() {
        return highSchoolType;
    }

    public void setHighSchoolType(AcademicalInstitutionType highSchoolType) {
        this.highSchoolType = highSchoolType;
    }

    public SchoolLevelType getMotherSchoolLevel() {
        return motherSchoolLevel;
    }

    public void setMotherSchoolLevel(SchoolLevelType motherSchoolLevel) {
        this.motherSchoolLevel = motherSchoolLevel;
    }

    public ProfessionType getMotherProfessionType() {
        return motherProfessionType;
    }

    public void setMotherProfessionType(ProfessionType motherProfessionType) {
        this.motherProfessionType = motherProfessionType;
    }

    public ProfessionalSituationConditionType getMotherProfessionalCondition() {
        return motherProfessionalCondition;
    }

    public void setMotherProfessionalCondition(ProfessionalSituationConditionType motherProfessionalCondition) {
        this.motherProfessionalCondition = motherProfessionalCondition;
    }

    public SchoolLevelType getFatherSchoolLevel() {
        return fatherSchoolLevel;
    }

    public void setFatherSchoolLevel(SchoolLevelType fatherSchoolLevel) {
        this.fatherSchoolLevel = fatherSchoolLevel;
    }

    public ProfessionType getFatherProfessionType() {
        return fatherProfessionType;
    }

    public void setFatherProfessionType(ProfessionType fatherProfessionType) {
        this.fatherProfessionType = fatherProfessionType;
    }

    public ProfessionalSituationConditionType getFatherProfessionalCondition() {
        return fatherProfessionalCondition;
    }

    public void setFatherProfessionalCondition(ProfessionalSituationConditionType fatherProfessionalCondition) {
        this.fatherProfessionalCondition = fatherProfessionalCondition;
    }

    public SchoolLevelType getSpouseSchoolLevel() {
        return spouseSchoolLevel;
    }

    public void setSpouseSchoolLevel(SchoolLevelType spouseSchoolLevel) {
        this.spouseSchoolLevel = spouseSchoolLevel;
    }

    public ProfessionType getSpouseProfessionType() {
        return spouseProfessionType;
    }

    public void setSpouseProfessionType(ProfessionType spouseProfessionType) {
        this.spouseProfessionType = spouseProfessionType;
    }

    public ProfessionalSituationConditionType getSpouseProfessionalCondition() {
        return spouseProfessionalCondition;
    }

    public void setSpouseProfessionalCondition(ProfessionalSituationConditionType spouseProfessionalCondition) {
        this.spouseProfessionalCondition = spouseProfessionalCondition;
    }

}
