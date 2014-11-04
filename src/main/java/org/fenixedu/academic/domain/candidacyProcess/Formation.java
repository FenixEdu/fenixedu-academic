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
package org.fenixedu.academic.domain.candidacyProcess;

import java.util.Formatter;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class Formation extends Formation_Base {

    public Formation() {
        super();
    }

    public Formation(IndividualCandidacy individualCandidacy, FormationBean bean) {
        this();

        edit(bean);
        this.setIndividualCandidacy(individualCandidacy);
    }

    public void edit(FormationBean bean) {
        this.setBeginYear(bean.getFormationBeginYear());
        this.setBranch(null);
        this.setConcluded(bean.isConcluded());
        this.setCountry(null);
        this.setDateYearMonthDay(null);
        this.setDegree(null);
        this.setDegreeRecognition(null);
        this.setDesignation(bean.getDesignation());
        this.setEctsCredits(null);
        this.setEducationArea(null);
        this.setEquivalenceDateYearMonthDay(null);
        this.setEquivalenceSchool(null);
        this.setFormationHours(null);
        this.setFormationType(null);
        this.setInstitution(getOrCreateInstitution(bean));
        this.setMark(null);
        this.setPerson(null);
        this.setSchool(null);
        this.setSpecializationArea(null);
        this.setTitle(null);
        this.setType(null);
        this.setYear(bean.getFormationEndYear());
        this.setConclusionGrade(bean.getConclusionGrade());
        this.setConclusionExecutionYear(bean.getConclusionExecutionYear());
    }

    private Unit getOrCreateInstitution(final FormationBean bean) {
        if (StringUtils.isEmpty(bean.getInstitutionName()) && bean.getInstitutionUnit() != null) {
            return bean.getInstitutionUnit();
        }

        if (bean.getInstitutionName() == null || bean.getInstitutionName().isEmpty()) {
            throw new DomainException("error.ExternalPrecedentDegreeCandidacy.invalid.institution.name");
        }

        final Unit unit = Unit.findFirstExternalUnitByName(bean.getInstitutionName());
        return (unit != null) ? unit : Unit.createNewNoOfficialExternalInstitution(bean.getInstitutionName());
    }

    public void exportValues(StringBuilder result) {
        Formatter formatter = new Formatter(result);
        formatter.format("\n%s:\n", BundleUtil.getString(Bundle.CANDIDATE, "title.other.academic.titles"));
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.other.academic.titles.program.name"),
                getDesignation());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.other.academic.titles.institution"),
                getInstitution().getName());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.other.academic.titles.conclusion.date"),
                StringUtils.isEmpty(getYear()) ? StringUtils.EMPTY : getYear());
        formatter.format("%s: %s\n", BundleUtil.getString(Bundle.CANDIDATE, "label.other.academic.titles.conclusion.grade"),
                StringUtils.isEmpty(getConclusionGrade()) ? StringUtils.EMPTY : getConclusionGrade());
        formatter.close();
    }

}
