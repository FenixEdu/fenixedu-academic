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
package org.fenixedu.academic.domain.phd.candidacy;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.candidacy.CandidacySituation;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public class PHDProgramCandidacy extends PHDProgramCandidacy_Base {

    public PHDProgramCandidacy(Person person, ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);

    }

    public PHDProgramCandidacy(Person person) {
        super();
        init(person);
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, I18N.getLocale(), "label.phd") + " - " + getDegreeInformation();
    }

    private String getDegreeInformation() {

        if (getExecutionDegree() != null) {
            return getDegreeCurricularPlan().getName() + " - " + getExecutionYear().getYear();

        } else if (getRegistration() != null) {
            return getRegistration().getDegreeCurricularPlanName();

        } else if (getCandidacyProcess().hasPhdProgram()) {
            return getCandidacyProcess().getPhdProgram().getName().getContent();

        } else {
            return getCandidacyProcess().getPhdProgramFocusArea().getName().getContent();
        }

    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return Collections.emptySet();
    }

    @Override
    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return Collections.emptyMap();
    }

    public void copyFromStudentCandidacy(final StudentCandidacy studentCandidacy) {
        setContigent(studentCandidacy.getContigent());
        setEntryGrade(studentCandidacy.getEntryGrade());
        setEntryPhase(studentCandidacy.getEntryPhase());
        setIngressionType(studentCandidacy.getIngressionType());
        setApplyForResidence(studentCandidacy.getApplyForResidence());
        setNotesAboutResidenceAppliance(studentCandidacy.getNotesAboutResidenceAppliance());
        setStudentPersonalDataAuthorizationChoice(studentCandidacy.getStudentPersonalDataAuthorizationChoice());
        setDislocatedFromPermanentResidence(studentCandidacy.getDislocatedFromPermanentResidence());
        setPlacingOption(studentCandidacy.getPlacingOption());
        setGrantOwnerType(studentCandidacy.getGrantOwnerType());
        setNumberOfCandidaciesToHigherSchool(studentCandidacy.getNumberOfCandidaciesToHigherSchool());
        setNumberOfFlunksOnHighSchool(studentCandidacy.getNumberOfFlunksOnHighSchool());
        setHighSchoolType(studentCandidacy.getHighSchoolType());
        setMaritalStatus(studentCandidacy.getMaritalStatus());
        setProfessionType(studentCandidacy.getProfessionType());
        setProfessionalCondition(studentCandidacy.getProfessionalCondition());
        setMotherSchoolLevel(studentCandidacy.getMotherSchoolLevel());
        setMotherProfessionType(studentCandidacy.getMotherProfessionType());
        setMotherProfessionalCondition(studentCandidacy.getMotherProfessionalCondition());
        setFatherSchoolLevel(studentCandidacy.getFatherSchoolLevel());
        setFatherProfessionType(studentCandidacy.getFatherProfessionType());
        setFatherProfessionalCondition(studentCandidacy.getFatherProfessionalCondition());
        setSpouseSchoolLevel(studentCandidacy.getSpouseSchoolLevel());
        setSpouseProfessionType(studentCandidacy.getSpouseProfessionType());
        setSpouseProfessionalCondition(studentCandidacy.getSpouseProfessionalCondition());
        setFirstTimeCandidacy(studentCandidacy.getFirstTimeCandidacy());

        getPrecedentDegreeInformation().setConclusionGrade(studentCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
        getPrecedentDegreeInformation().setConclusionYear(studentCandidacy.getPrecedentDegreeInformation().getConclusionYear());
        getPrecedentDegreeInformation().setDegreeDesignation(
                studentCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
        getPrecedentDegreeInformation().setSchoolLevel(studentCandidacy.getPrecedentDegreeInformation().getSchoolLevel());
        getPrecedentDegreeInformation().setOtherSchoolLevel(
                studentCandidacy.getPrecedentDegreeInformation().getOtherSchoolLevel());
        getPrecedentDegreeInformation().setCountry(studentCandidacy.getPrecedentDegreeInformation().getCountry());
        getPrecedentDegreeInformation().setInstitution(studentCandidacy.getPrecedentDegreeInformation().getInstitution());
        getPrecedentDegreeInformation().setSourceInstitution(
                studentCandidacy.getPrecedentDegreeInformation().getSourceInstitution());

        setExecutionDegree(studentCandidacy.getExecutionDegree());
        setRegistration(studentCandidacy.getRegistration());
        setGrantOwnerProvider(studentCandidacy.getGrantOwnerProvider());
        getCandidacyDocumentsSet().addAll(studentCandidacy.getCandidacyDocumentsSet());
        getCandidacySituationsSet().addAll(studentCandidacy.getCandidacySituationsSet());

        setSchoolTimeDistrictSubDivisionOfResidence(studentCandidacy.getSchoolTimeDistrictSubDivisionOfResidence());
        setCountryOfResidence(studentCandidacy.getCountryOfResidence());
        setDistrictSubdivisionOfResidence(studentCandidacy.getDistrictSubdivisionOfResidence());
    }

}
