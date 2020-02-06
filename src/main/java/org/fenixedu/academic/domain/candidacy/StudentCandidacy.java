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
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class StudentCandidacy extends StudentCandidacy_Base {

    protected StudentCandidacy() {
        super();
    }

    public StudentCandidacy(final Person person, final ExecutionDegree executionDegree) {
        this();
        init(person, executionDegree);
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

    public ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
    }

    public boolean isFirstCycleCandidacy() {
        return false;
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.CANDIDATE, "label.degreeCandidacy") + " - "
                + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    protected Set<Operation> getOperations(CandidacySituation candidacySituation) {
        return Collections.emptySet();
    }

    @Override
    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return null;
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public void setState(CandidacySituationType state) {
        super.setState(state);
        setStateDate(new DateTime());
    }

    @Override
    public Integer createCandidacyNumber() {
        return new Random().nextInt(); // temp override, for migration
    }

    public static StudentCandidacy convertStudentCandidacy(final StudentCandidacy oldCandidacy) {
        if (oldCandidacy.getClass().equals(StudentCandidacy.class)) {
            // already converted
            return null;
        }

        final StudentCandidacy result = new StudentCandidacy();
        result.setPerson(oldCandidacy.getPerson());
        result.setExecutionDegree(oldCandidacy.getExecutionDegree());
        result.setRegistration(oldCandidacy.getRegistration());
        result.setIngressionType(oldCandidacy.getIngressionType());
        result.setSummaryFile(oldCandidacy.getSummaryFile());
        result.setPrecedentDegreeInformation(oldCandidacy.getPrecedentDegreeInformation());

        final CandidacySituation activeCandidacySituation = oldCandidacy.getActiveCandidacySituation();
        if (activeCandidacySituation != null) {
            result.setState(activeCandidacySituation.getCandidacySituationType());
            result.setStateDate(activeCandidacySituation.getSituationDate());
        }

        result.setNumber(oldCandidacy.getNumber());
        result.setStartDate(oldCandidacy.getStartDate());

        result.setContigent(oldCandidacy.getContigent());
        result.setEntryGrade(oldCandidacy.getEntryGrade());
        result.setEntryPhase(oldCandidacy.getEntryPhase());
        result.setPlacingOption(oldCandidacy.getPlacingOption());
        result.setNumberOfCandidaciesToHigherSchool(oldCandidacy.getNumberOfCandidaciesToHigherSchool());
        result.setFirstTimeCandidacy(oldCandidacy.getFirstTimeCandidacy());

        oldCandidacy.delete();

        return result;
    }

}
