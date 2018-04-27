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
package org.fenixedu.academic.domain.candidacy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.candidacy.PHDProgramCandidacy;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public abstract class StudentCandidacy extends StudentCandidacy_Base {

    public StudentCandidacy() {
        super();
    }

    protected void init(Person person, ExecutionDegree executionDegree) {
        String[] args = {};
        if (executionDegree == null) {
            throw new DomainException("execution degree cannot be null", args);
        }
        String[] args1 = {};
        if (person == null) {
            throw new DomainException("person cannot be null", args1);
        }

        if (person.hasStudentCandidacyForExecutionDegree(executionDegree)) {
            StudentCandidacy existentCandidacy = person.getStudentCandidacyForExecutionDegree(executionDegree);
            if (existentCandidacy.getRegistration() == null
                    || existentCandidacy.getRegistration().getActiveStateType().isActive()) {
                throw new DomainException("error.candidacy.already.created");
            }
        }
        setExecutionDegree(executionDegree);
        setPerson(person);
        setPrecedentDegreeInformation(new PrecedentDegreeInformation());
    }

    protected void init(Person person) {
        String[] args = {};
        if (person == null) {
            throw new DomainException("person cannot be null", args);
        }
        setPerson(person);
        setPrecedentDegreeInformation(new PrecedentDegreeInformation());
    }

    private void checkParameters(final Person person, final ExecutionDegree executionDegree, final Person creator,
            final Double entryGrade, final String contigent, final IngressionType ingressionType, final EntryPhase entryPhase) {
        if (executionDegree == null) {
            throw new DomainException("error.candidacy.DegreeCandidacy.executionDegree.cannot.be.null");
        }

        if (person == null) {
            throw new DomainException("error.candidacy.DegreeCandidacy.person.cannot.be.null");
        }

        if (person.getCandidaciesSet().stream().filter(DegreeCandidacy.class::isInstance).map(DegreeCandidacy.class::cast)
                .filter(degreeCandidacy -> degreeCandidacy.isActive() && degreeCandidacy.getRegistration().isActive())
                .anyMatch(degreeCandidacy -> degreeCandidacy.getExecutionDegree() == executionDegree)) {
            throw new DomainException("error.candidacy.DegreeCandidacy.candidacy.already.created");
        }

        if (creator == null) {
            throw new DomainException("error.candidacy.DegreeCandidacy.creator.cannot.be.null");
        }

        if (entryPhase == null) {
            throw new DomainException("error.candidacy.DegreeCandidacy.entryPhase.cannot.be.null");
        }

    }

    protected void init(final Person person, final ExecutionDegree executionDegree, final Person creator, Double entryGrade,
            String contigent, IngressionType ingressionType, EntryPhase entryPhase, Integer placingOption) {
        checkParameters(person, executionDegree, creator, entryGrade, contigent, ingressionType, entryPhase);
        super.setExecutionDegree(executionDegree);
        super.setPerson(person);
        super.setPrecedentDegreeInformation(new PrecedentDegreeInformation());
        super.setEntryGrade(entryGrade);
        super.setContigent(contigent);
        super.setIngressionType(ingressionType);
        super.setEntryPhase(entryPhase);
        super.setPlacingOption(placingOption);
    }

    public DateTime getCandidacyDate() {
        return Collections.min(getCandidacySituationsSet(), CandidacySituation.DATE_COMPARATOR).getSituationDate();
    }

    public static StudentCandidacy createStudentCandidacy(ExecutionDegree executionDegree, Person studentPerson) {
        if (executionDegree.getDegree().getDegreeType().isEmpty()) {
            return new DegreeCandidacy(studentPerson, executionDegree);
        }
        if (executionDegree.getDegree().getDegreeType().isSpecializationDegree()) {
            return new SDCandidacy(studentPerson, executionDegree);
        }
        if (executionDegree.getDegree().getDegreeType().isAdvancedSpecializationDiploma()) {
            // TODO: remove this after PHD Program candidacy is completed and
            // data migrated
            return new PHDProgramCandidacy(studentPerson, executionDegree);
        }
        if (executionDegree.getDegree().getDegreeType().isAdvancedFormationDiploma()) {
            return new DFACandidacy(studentPerson, executionDegree);
        }

        if (executionDegree.getDegree().getDegreeType().isDegree()) {
            return new DegreeCandidacy(studentPerson, executionDegree);
        }

        if (executionDegree.getDegree().getDegreeType().isBolonhaMasterDegree()) {
            return new MDCandidacy(studentPerson, executionDegree);
        }

        if (executionDegree.getDegree().getDegreeType().isIntegratedMasterDegree()) {
            return new IMDCandidacy(studentPerson, executionDegree);
        }

        // Fallback, if the Degree Type is not recognized
        return new DegreeCandidacy(studentPerson, executionDegree);
    }

    public static Set<StudentCandidacy> readByIds(final List<String> studentCandidacyIds) {
        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();

        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy instanceof StudentCandidacy) {
                if (studentCandidacyIds.contains(candidacy.getExternalId())) {
                    result.add((StudentCandidacy) candidacy);
                }
            }
        }

        return result;
    }

    public static Set<StudentCandidacy> readByExecutionYear(final ExecutionYear executionYear) {
        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy instanceof StudentCandidacy) {
                final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
                if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
                    result.add(studentCandidacy);
                }
            }
        }

        return result;
    }

    public static Set<StudentCandidacy> readNotConcludedBy(final ExecutionDegree executionDegree,
            final ExecutionYear executionYear, final EntryPhase entryPhase) {
        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
            if (candidacy instanceof StudentCandidacy) {
                final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
                if (!studentCandidacy.getCandidacySituationsSet().isEmpty() && !studentCandidacy.isConcluded()
                        && studentCandidacy.getExecutionDegree() == executionDegree
                        && studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear
                        && studentCandidacy.getEntryPhase() != null && studentCandidacy.getEntryPhase().equals(entryPhase)) {
                    result.add(studentCandidacy);
                }
            }
        }

        return result;
    }

    @Override
    public void delete() {
        setRegistration(null);
        setIngressionType(null);
        setExecutionDegree(null);
        setSchoolTimeDistrictSubDivisionOfResidence(null);
        setCountryOfResidence(null);
        setDistrictSubdivisionOfResidence(null);

        if (getGrantOwnerProvider() != null) {
            setGrantOwnerProvider(null);
        }

        if (getPrecedentDegreeInformation() != null && getPrecedentDegreeInformation().getStudent() == null) {
            getPrecedentDegreeInformation().delete();
        }

        super.delete();
    }

    @Override
    public boolean isConcluded() {
        final CandidacySituation activeSituation = getActiveCandidacySituation();
        return activeSituation != null && (activeSituation.getCandidacySituationType() == CandidacySituationType.REGISTERED
                || getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.CANCELLED);
    }

    public boolean cancelCandidacy() {
        if (!isConcluded()) {
            new CancelledCandidacySituation(this, this.getPerson());

            for (PaymentCode paymentCode : getAvailablePaymentCodesSet()) {
                AccountingEventPaymentCode accountingEventPaymentCode = (AccountingEventPaymentCode) paymentCode;
                if (accountingEventPaymentCode.isNew() && accountingEventPaymentCode.getAccountingEvent() == null) {
                    accountingEventPaymentCode.cancel();
                }
            }

            return true;
        }
        return false;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan();
    }

    public boolean hasEntryPhase() {
        return getEntryPhase() != null;
    }

    public boolean hasApplyForResidence() {
        return getApplyForResidence() != null;
    }

    public ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
    }

    public boolean isFirstCycleCandidacy() {
        return false;
    }
}
