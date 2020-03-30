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
package org.fenixedu.academic.domain.student;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class PrecedentDegreeInformation extends PrecedentDegreeInformation_Base {

    public PrecedentDegreeInformation() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setLastModifiedDate(new DateTime());
    }

    public void edit(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
        Unit institution = precedentDegreeInformationBean.getInstitution();
        if (institution == null && !StringUtils.isEmpty(precedentDegreeInformationBean.getInstitutionName())) {
            institution = UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getInstitutionName());
            if (institution == null) {
                institution = Unit.createNewNoOfficialExternalInstitution(precedentDegreeInformationBean.getInstitutionName());
            }
        }
        setInstitution(institution);
        setDegreeDesignation(precedentDegreeInformationBean.getDegreeDesignation());
        setConclusionGrade(precedentDegreeInformationBean.getConclusionGrade());
        setConclusionYear(precedentDegreeInformationBean.getConclusionYear());
        setCountry(precedentDegreeInformationBean.getCountry());
        setCountryHighSchool(precedentDegreeInformationBean.getCountryWhereFinishedHighSchoolLevel());
        setSchoolLevel(precedentDegreeInformationBean.getSchoolLevel());
        setOtherSchoolLevel(precedentDegreeInformationBean.getOtherSchoolLevel());

        setLastModifiedDate(new DateTime());
    }

    public void editPreviousPrecedentInformation(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
        if (precedentDegreeInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) {
            Unit precedentInstitution = precedentDegreeInformationBean.getPrecedentInstitution();
            if (precedentInstitution == null
                    && !StringUtils.isEmpty(precedentDegreeInformationBean.getPrecedentInstitutionName())) {
                precedentInstitution =
                        UnitUtils.readExternalInstitutionUnitByName(precedentDegreeInformationBean.getPrecedentInstitutionName());
                if (precedentInstitution == null) {
                    precedentInstitution = Unit
                            .createNewNoOfficialExternalInstitution(precedentDegreeInformationBean.getPrecedentInstitutionName());
                }
            }
            setInstitution(precedentInstitution);
            setDegreeDesignation(precedentDegreeInformationBean.getPrecedentDegreeDesignation());
            setSchoolLevel(precedentDegreeInformationBean.getPrecedentSchoolLevel());
            setOtherSchoolLevel(precedentDegreeInformationBean.getOtherPrecedentSchoolLevel());
            setNumberOfEnrolmentsInPreviousDegrees(
                    precedentDegreeInformationBean.getNumberOfPreviousYearEnrolmentsInPrecedentDegree());
            setMobilityProgramDuration(precedentDegreeInformationBean.getMobilityProgramDuration());
        }
    }

    public String getInstitutionName() {
        return getInstitution() != null ? getInstitution().getName() : null;
    }

    public void delete() {
        setCountry(null);
        setCountryHighSchool(null);
        setInstitution(null);

        setCompletedStudentCandidacy(null);
        setPreviousStudentCandidacy(null);

        setRootDomainObject(null);
        deleteDomainObject();
    }

}
