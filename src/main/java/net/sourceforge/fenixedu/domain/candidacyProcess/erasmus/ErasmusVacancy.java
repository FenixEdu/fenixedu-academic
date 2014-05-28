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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;

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

        for (MobilityStudentData data : getCandidacies()) {
            processList.add(data.getMobilityIndividualApplication().getCandidacyProcess());
        }

        return processList;
    }

    public boolean isVacancyAssociatedToAnyCandidacy() {
        return hasAnyCandidacies();
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData> getCandidacies() {
        return getCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacies() {
        return !getCandidaciesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasNumberOfVacancies() {
        return getNumberOfVacancies() != null;
    }

    @Deprecated
    public boolean hasUniversityUnit() {
        return getUniversityUnit() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Deprecated
    public boolean hasCandidacyPeriod() {
        return getCandidacyPeriod() != null;
    }

}
