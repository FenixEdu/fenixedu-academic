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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityStudentData;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UniversityUnit;
import org.fenixedu.academic.domain.period.MobilityApplicationPeriod;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class ErasmusVacancy extends ErasmusVacancy_Base {

    private ErasmusVacancy() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ErasmusVacancy(final MobilityApplicationPeriod period, Degree degree, UniversityUnit unit, Integer numberOfVacancies) {
        this();

        setCandidacyPeriod(period);
        setDegree(degree);
        setUniversityUnit(unit);

        setNumberOfVacancies(numberOfVacancies);

        check();
    }

    private void check() {
        if (getCandidacyPeriod() == null) {
            throw new DomainException("error.erasmus.vacancy.candidacy.period.must.not.be.null");
        }

        if (getDegree() == null) {
            throw new DomainException("error.erasmus.vacancy.degree.must.not.be.null");
        }

        if (getUniversityUnit() == null) {
            throw new DomainException("error.erasmus.vacancy.university.unit.must.not.be.null");
        }

        if (getNumberOfVacancies() == null) {
            throw new DomainException("error.erasmus.vacancy.number.of.vacancies.must.not.be.null");
        }
    }

    @Atomic
    public static ErasmusVacancy createVacancy(final MobilityApplicationPeriod period, Degree degree, UniversityUnit unit,
            Integer numberOfVacancies) {
        return new ErasmusVacancy(period, degree, unit, numberOfVacancies);
    }

    public List<MobilityIndividualApplicationProcess> getStudentApplicationProcesses() {
        List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();

        for (MobilityStudentData data : getCandidaciesSet()) {
            processList.add(data.getMobilityIndividualApplication().getCandidacyProcess());
        }

        return processList;
    }

    public boolean isVacancyAssociatedToAnyCandidacy() {
        return !getCandidaciesSet().isEmpty();
    }

    public void delete() {
        if (isVacancyAssociatedToAnyCandidacy()) {
            throw new DomainException("error.erasmus.vacancy.is.associated.to.candidacies");
        }

        setUniversityUnit(null);
        setDegree(null);
        setCandidacyPeriod(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

}
