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
package net.sourceforge.fenixedu.domain.candidacyProcess.mobility;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.MobilityApplicationPeriod;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class MobilityQuota extends MobilityQuota_Base {

    public MobilityQuota() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MobilityQuota(MobilityApplicationPeriod period, Degree degree, MobilityAgreement mobilityAgreement,
            Integer numberOfOpenings) {
        this();
        setApplicationPeriod(period);
        setDegree(degree);
        setMobilityAgreement(mobilityAgreement);
        setNumberOfOpenings(numberOfOpenings);
        check();
    }

    public MobilityQuota(final MobilityApplicationPeriod period, Degree degree, MobilityProgram mobilityProgram,
            UniversityUnit unit, Integer numberOfOpenings) {
        this();

        setApplicationPeriod(period);
        setDegree(degree);

        MobilityAgreement agreement = MobilityAgreement.getOrCreateAgreement(mobilityProgram, unit);

        setMobilityAgreement(agreement);

        setNumberOfOpenings(numberOfOpenings);

        check();
    }

    private void check() {
        if (getApplicationPeriod() == null) {
            throw new DomainException("error.erasmus.vacancy.candidacy.period.must.not.be.null");
        }

        if (getDegree() == null) {
            throw new DomainException("error.erasmus.vacancy.degree.must.not.be.null");
        }

        if (getMobilityAgreement().getUniversityUnit() == null) {
            throw new DomainException("error.erasmus.vacancy.university.unit.must.not.be.null");
        }

        if (getNumberOfOpenings() == null) {
            throw new DomainException("error.erasmus.vacancy.number.of.vacancies.must.not.be.null");
        }
    }

    @Atomic
    public static MobilityQuota createVacancy(final MobilityApplicationPeriod period, final Degree degree,
            final MobilityProgram mobilityProgram, final UniversityUnit unit, final Integer numberOfOpenings) {
        return new MobilityQuota(period, degree, mobilityProgram, unit, numberOfOpenings);
    }

    public List<MobilityIndividualApplicationProcess> getStudentApplicationProcesses() {
        List<MobilityIndividualApplicationProcess> processList = new ArrayList<MobilityIndividualApplicationProcess>();

        for (MobilityStudentData data : getApplications()) {
            processList.add(data.getMobilityIndividualApplication().getCandidacyProcess());
        }

        return processList;
    }

    public boolean isQuotaAssociatedWithAnyApplication() {
        return hasAnyApplications();
    }

    public void delete() {
        if (isQuotaAssociatedWithAnyApplication()) {
            throw new DomainException("error.mobility.quota.is.associated.with.applications");
        }

        setMobilityAgreement(null);
        setDegree(null);
        setApplicationPeriod(null);
        setRootDomainObject(null);

        deleteDomainObject();
    }

    public boolean isFor(final MobilityProgram mobilityProgram) {
        return getMobilityAgreement().getMobilityProgram() == mobilityProgram;
    }

    public boolean isAssociatedToApplications() {
        return !getStudentApplicationProcesses().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityStudentData> getApplications() {
        return getApplicationsSet();
    }

    @Deprecated
    public boolean hasAnyApplications() {
        return !getApplicationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasNumberOfOpenings() {
        return getNumberOfOpenings() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasApplicationPeriod() {
        return getApplicationPeriod() != null;
    }

    @Deprecated
    public boolean hasMobilityAgreement() {
        return getMobilityAgreement() != null;
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}
