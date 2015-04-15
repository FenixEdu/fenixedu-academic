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
package org.fenixedu.academic.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.ProfessionType;
import org.fenixedu.academic.domain.ProfessionalSituationConditionType;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.util.LabelFormatter;

public class HouseholdInformationForm extends Form {

    private SchoolLevelType motherSchoolLevel;

    private ProfessionType motherProfessionType;

    private ProfessionalSituationConditionType motherProfessionalCondition;

    private SchoolLevelType fatherSchoolLevel;

    private ProfessionType fatherProfessionType;

    private ProfessionalSituationConditionType fatherProfessionalCondition;

    private static final long serialVersionUID = 1L;

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.householdInformationForm";
    }

    @Override
    public List<LabelFormatter> validate() {
        return Collections.emptyList();
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

}
