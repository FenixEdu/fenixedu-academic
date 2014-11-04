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
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.util.List;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityProgram;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityQuota;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;

public class ErasmusVacancyBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private UniversityUnit university;
    private Country country;
    private List<Degree> degrees;

    private Integer numberOfVacancies;

    private ErasmusVacancy vacancy;
    private MobilityQuota quota;

    private MobilityProgram mobilityProgram;

    public ErasmusVacancyBean() {

    }

    public ErasmusVacancyBean(MobilityQuota quota) {
        this.quota = quota;
    }

    public ErasmusVacancyBean(MobilityProgram program) {
        this.mobilityProgram = program;
    }

    public UniversityUnit getUniversity() {
        return university;
    }

    public void setUniversity(UniversityUnit university) {
        this.university = university;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<Degree> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<Degree> degrees) {
        this.degrees = degrees;
    }

    public Integer getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public void setNumberOfVacancies(Integer numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public MobilityQuota getQuota() {
        return this.quota;
    }

    public void setQuota(final MobilityQuota quota) {
        this.quota = quota;
    }

    public ErasmusVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(ErasmusVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public MobilityProgram getMobilityProgram() {
        return mobilityProgram;
    }

    public void setMobilityProgram(MobilityProgram mobilityProgram) {
        this.mobilityProgram = mobilityProgram;
    }

    public MobilityAgreement getMobilityAgreement() {
        if (mobilityProgram == null || university == null) {
            return null;
        }
        return mobilityProgram.getMobilityAgreementByUniversityUnit(university);
    }
}