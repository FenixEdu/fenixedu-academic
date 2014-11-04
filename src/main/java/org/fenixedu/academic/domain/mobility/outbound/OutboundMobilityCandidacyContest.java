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
package org.fenixedu.academic.domain.mobility.outbound;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.mobility.MobilityAgreement;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationState;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class OutboundMobilityCandidacyContest extends OutboundMobilityCandidacyContest_Base implements
        Comparable<OutboundMobilityCandidacyContest> {

    public OutboundMobilityCandidacyContest(final OutboundMobilityCandidacyPeriod outboundMobilityCandidacyPeriod,
            final OutboundMobilityCandidacyContestGroup mobilityGroup, final MobilityAgreement mobilityAgreement,
            final Integer vacancies) {
        setRootDomainObject(Bennu.getInstance());
        setOutboundMobilityCandidacyPeriod(outboundMobilityCandidacyPeriod);
        setMobilityAgreement(mobilityAgreement);
        setOutboundMobilityCandidacyContestGroup(mobilityGroup);
        for (final ExecutionDegree executionDegree : mobilityGroup.getExecutionDegreeSet()) {
            addExecutionDegree(executionDegree);
        }
        setVacancies(vacancies);
    }

    @Override
    public int compareTo(final OutboundMobilityCandidacyContest o) {
        final int edc = compareDegrees(o);
        if (edc == 0) {
            final int cc = compareCountries(o);
            if (cc == 0) {
                final int uc = compareUniversities(o);
                if (uc == 0) {
                    final int pc = comparePrograms(o);
                    return pc == 0 ? getExternalId().compareTo(o.getExternalId()) : pc;
                }
                return uc;
            }
            return cc;
        }
        return edc;
    }

    private int compareCountries(final OutboundMobilityCandidacyContest o) {
        final Country c1 = getMobilityAgreement().getUniversityUnit().getCountry();
        final Country c2 = o.getMobilityAgreement().getUniversityUnit().getCountry();
        return c1 == c2 ? 0 : (c1 != null && c2 != null ? c1.getName().compareTo(c2.getName()) : (c1 == null ? -1 : 1));
    }

    private int comparePrograms(final OutboundMobilityCandidacyContest o) {
        return getMobilityAgreement().getMobilityProgram().getRegistrationProtocol().getDescription()
                .compareTo(o.getMobilityAgreement().getMobilityProgram().getRegistrationProtocol().getDescription());
    }

    private int compareUniversities(final OutboundMobilityCandidacyContest o) {
        return Unit.COMPARATOR_BY_NAME_AND_ID.compare(getMobilityAgreement().getUniversityUnit(), o.getMobilityAgreement()
                .getUniversityUnit());
    }

    private int compareDegrees(final OutboundMobilityCandidacyContest o) {
        return executionDegreesCompareHash(o.getOutboundMobilityCandidacyContestGroup().getExecutionDegreeSet()).compareTo(
                executionDegreesCompareHash(getOutboundMobilityCandidacyContestGroup().getExecutionDegreeSet()));
    }

    private String executionDegreesCompareHash(final Set<ExecutionDegree> executionDegreeSet) {
        final SortedSet<String> strings = new TreeSet<String>();
        for (final ExecutionDegree executionDegree : executionDegreeSet) {
            strings.add(executionDegree.getDegree().getSigla());
        }
        final StringBuilder builder = new StringBuilder();
        for (final String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    public boolean isAcceptingCandidacies() {
        return getOutboundMobilityCandidacyPeriod().isAcceptingCandidacies();
    }

    public boolean isAcceptingCandidacies(final Student student) {
        return isAcceptingCandidacies() && findBestRegistration(student) != null;
    }

    public void apply(final Student student) {
        if (isAcceptingCandidacies()) {
            final Registration registration = findBestRegistration(student);
            if (registration == null) {
                throw new DomainException("error.OutboundMobilityCandidacyContest.not.accepting.for.studnt");
            } else {
                apply(registration);
            }
        } else {
            throw new DomainException("error.CandidacyPeriod.not.accepting.candidacies");
        }
    }

    private void apply(final Registration registration) {
        OutboundMobilityCandidacySubmission.apply(this, registration);
    }

    public Registration findBestRegistration(final Student student) {
        for (final Registration registration : student.getRegistrationsSet()) {
            for (final ExecutionDegree executionDegree : getOutboundMobilityCandidacyContestGroup().getExecutionDegreeSet()) {
                if (executionDegree.getDegree() == registration.getDegree()) {
                    final ExecutionYear executionYear =
                            (ExecutionYear) getOutboundMobilityCandidacyPeriod().getExecutionInterval();
                    final RegistrationState registrationState = registration.getLastRegistrationState(executionYear);
                    if (registrationState != null && registrationState.getStateType().isActive()) {
                        return registration;
                    }
                }
            }
        }
        return null;
    }

    public boolean hasCandidacy(final Student student) {
        for (final Registration registration : student.getRegistrationsSet()) {
            for (final OutboundMobilityCandidacySubmission submission : registration.getOutboundMobilityCandidacySubmissionSet()) {
                for (final OutboundMobilityCandidacy candidacy : submission.getOutboundMobilityCandidacySet()) {
                    if (candidacy.getOutboundMobilityCandidacyContest() == this) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void addExecutionDegree(final ExecutionDegree executionDegree) {
        super.addExecutionDegree(executionDegree);
        // TODO : This is a hack due to a bug in the consistency predicate or fenix-framework code.
        //        When the relation is initialized but never traversed, the consistency predicate always
        //        fails. Forcing a traversal will resolve this issue. The bug has already been solved in
        //        the framework, but the framework has not yet been updated on this project.
        getExecutionDegreeSet().size();
    }

    @Atomic
    public void delete() {
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getOutboundMobilityCandidacyContestGroup();
        for (final OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            candidacy.deleteWithNotification();
        }
        getExecutionDegreeSet().clear();
        setMobilityAgreement(null);
        setOutboundMobilityCandidacyContestGroup(null);
        setOutboundMobilityCandidacyPeriod(null);
        setRootDomainObject(null);
        if (mobilityGroup != null && mobilityGroup.getOutboundMobilityCandidacyContestSet().size() == 0) {
            mobilityGroup.delete();
        }
        deleteDomainObject();
    }

    public int countSelectedCandidates() {
        int c = 0;
        for (OutboundMobilityCandidacy candidacy : getOutboundMobilityCandidacySet()) {
            if (candidacy.getSubmissionFromSelectedCandidacy() != null) {
                c++;
            }
        }
        return c;
    }

    public boolean hasVacancy() {
        final Integer vacancies = getVacancies();
        return vacancies == null || vacancies.intValue() > countSelectedCandidates();
    }

    @Atomic
    public void editVacancies(final Integer vacancies) {
        setVacancies(vacancies);
    }

}
