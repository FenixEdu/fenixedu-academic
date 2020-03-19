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

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class StudentCandidacy extends StudentCandidacy_Base {

    protected StudentCandidacy() {
        super();
        setNumber(createCandidacyNumber());
        setRootDomainObject(Bennu.getInstance());
        setStartDate(new YearMonthDay());
    }

    public StudentCandidacy(final Person person, final ExecutionDegree executionDegree) {
        this();
        init(person, executionDegree);
    }

    public Integer createCandidacyNumber() {
        Integer max = Bennu.getInstance().getCandidaciesSet().stream().map(sc -> sc.getNumber()).max(Comparator.naturalOrder())
                .orElse(0);
        return max + 1;
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

        final StudentCandidacy existentCandidacy = person.getCandidaciesSet().stream()
                .filter(c -> c.isActive() && c.getExecutionDegree() == executionDegree).findFirst().orElse(null);
        if (existentCandidacy != null) {
            if (existentCandidacy.getRegistration() == null
                    || existentCandidacy.getRegistration().getActiveStateType().isActive()) {
                throw new DomainException("error.candidacy.already.created");
            }
        }
        setExecutionDegree(executionDegree);
        setPerson(person);
//        super.setPrecedentDegreeInformation(new PrecedentDegreeInformation()); // deprecated
        setCompletedDegreeInformation(new PrecedentDegreeInformation());
        setPreviousDegreeInformation(new PrecedentDegreeInformation());
    }

//    protected void init(Person person) {
//        String[] args = {};
//        if (person == null) {
//            throw new DomainException("person cannot be null", args);
//        }
//        setPerson(person);
//        setPrecedentDegreeInformation(new PrecedentDegreeInformation());
//    }

//    private void checkParameters(final Person person, final ExecutionDegree executionDegree, final Person creator,
//            final Double entryGrade, final String contigent, final IngressionType ingressionType, final EntryPhase entryPhase) {
//        if (executionDegree == null) {
//            throw new DomainException("error.candidacy.DegreeCandidacy.executionDegree.cannot.be.null");
//        }
//
//        if (person == null) {
//            throw new DomainException("error.candidacy.DegreeCandidacy.person.cannot.be.null");
//        }
//
//        if (person.getCandidaciesSet().stream()
//                .filter(degreeCandidacy -> degreeCandidacy.isActive() && degreeCandidacy.getRegistration().isActive())
//                .anyMatch(degreeCandidacy -> degreeCandidacy.getExecutionDegree() == executionDegree)) {
//            throw new DomainException("error.candidacy.DegreeCandidacy.candidacy.already.created");
//        }
//
//        if (creator == null) {
//            throw new DomainException("error.candidacy.DegreeCandidacy.creator.cannot.be.null");
//        }
//
//        if (entryPhase == null) {
//            throw new DomainException("error.candidacy.DegreeCandidacy.entryPhase.cannot.be.null");
//        }
//
//    }

//    protected void init(final Person person, final ExecutionDegree executionDegree, final Person creator, Double entryGrade,
//            String contigent, IngressionType ingressionType, EntryPhase entryPhase, Integer placingOption) {
//        checkParameters(person, executionDegree, creator, entryGrade, contigent, ingressionType, entryPhase);
//        super.setExecutionDegree(executionDegree);
//        super.setPerson(person);
//        super.setPrecedentDegreeInformation(new PrecedentDegreeInformation());
//        super.setEntryGrade(entryGrade);
//        super.setContigent(contigent);
//        super.setIngressionType(ingressionType);
//        super.setEntryPhase(entryPhase);
//        super.setPlacingOption(placingOption);
//    }

    /**
     * @deprecated use {@link #getState()}
     */
    @Deprecated
    public CandidacySituationType getActiveCandidacySituationType() {
        return getState();
    }

    public boolean isActive() {
        final CandidacySituationType situationType = getState();
        return situationType != null && situationType.isActive();
    }

//    public DateTime getCandidacyDate() {
//        return getStartDate();
//    }

//    public static Set<StudentCandidacy> readByIds(final List<String> studentCandidacyIds) {
//        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
//
//        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
//            if (candidacy instanceof StudentCandidacy) {
//                if (studentCandidacyIds.contains(candidacy.getExternalId())) {
//                    result.add((StudentCandidacy) candidacy);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public static Set<StudentCandidacy> readByExecutionYear(final ExecutionYear executionYear) {
//        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
//        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
//            if (candidacy instanceof StudentCandidacy) {
//                final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
//                if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear) {
//                    result.add(studentCandidacy);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public static Set<StudentCandidacy> readNotConcludedBy(final ExecutionDegree executionDegree,
//            final ExecutionYear executionYear, final EntryPhase entryPhase) {
//        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
//        for (final Candidacy candidacy : Bennu.getInstance().getCandidaciesSet()) {
//            if (candidacy instanceof StudentCandidacy) {
//                final StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
//                if (!studentCandidacy.getCandidacySituationsSet().isEmpty() && !studentCandidacy.isConcluded()
//                        && studentCandidacy.getExecutionDegree() == executionDegree
//                        && studentCandidacy.getExecutionDegree().getExecutionYear() == executionYear
//                        && studentCandidacy.getEntryPhase() != null && studentCandidacy.getEntryPhase().equals(entryPhase)) {
//                    result.add(studentCandidacy);
//                }
//            }
//        }
//
//        return result;
//    }

    public void delete() {
        setPerson(null);

        setRootDomainObject(null);
        setRegistration(null);
        setIngressionType(null);
        setExecutionDegree(null);

        if (super.getPrecedentDegreeInformation() != null) {
            super.getPrecedentDegreeInformation().delete();
        }

        Optional.ofNullable(getCompletedDegreeInformation()).ifPresent(pdi -> pdi.delete());
        Optional.ofNullable(getPreviousDegreeInformation()).ifPresent(pdi -> pdi.delete());

        deleteDomainObject();
    }

//    @Override
//    public boolean isConcluded() {
//        final CandidacySituation activeSituation = getActiveCandidacySituation();
//        return activeSituation != null && (activeSituation.getCandidacySituationType() == CandidacySituationType.REGISTERED
//                || getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.CANCELLED);
//    }
//
//    public boolean cancelCandidacy() {
//        if (!isConcluded()) {
//            new CancelledCandidacySituation(this, this.getPerson());
//            return true;
//        }
//        return false;
//    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getExecutionDegree().getDegreeCurricularPlan();
    }

//    public boolean hasEntryPhase() {
//        return getEntryPhase() != null;
//    }

    public ExecutionYear getExecutionYear() {
        return getExecutionDegree().getExecutionYear();
    }

//    public boolean isFirstCycleCandidacy() {
//        return false;
//    }

//    @Override
//    public String getDescription() {
//        return BundleUtil.getString(Bundle.CANDIDATE, "label.degreeCandidacy") + " - "
//                + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
//                + getExecutionDegree().getExecutionYear().getYear();
//    }

//    @Override
//    protected Set<Operation> getOperations(CandidacySituation candidacySituation) {
//        return Collections.emptySet();
//    }
//
//    @Override
//    protected void moveToNextState(CandidacyOperationType candidacyOperationType, Person person) {
//    }
//
//    @Override
//    public Map<String, Set<String>> getStateMapping() {
//        return null;
//    }
//
//    @Override
//    public String getDefaultState() {
//        return null;
//    }

    @Override
    public void setState(CandidacySituationType state) {
        super.setState(state);
        setStateDate(new DateTime());
    }

    @Override
    public Boolean getFirstTimeCandidacy() {
        return Boolean.TRUE.equals(super.getFirstTimeCandidacy());
    }

//    @Deprecated
//    @Override
//    public PrecedentDegreeInformation getPrecedentDegreeInformation() {
//        throw new UnsupportedOperationException("StudencCandidacy - PrecedentDegreeInformation relation is now deprecated");
//    }
//
//    @Deprecated
//    @Override
//    public void setPrecedentDegreeInformation(PrecedentDegreeInformation precedentDegreeInformation) {
//        throw new UnsupportedOperationException("StudencCandidacy - PrecedentDegreeInformation relation is now deprecated");
//    }

    public boolean migratePDI(boolean force) {

        final AtomicBoolean newCompletedPDI = new AtomicBoolean(false);
        final AtomicBoolean newPreviousPDI = new AtomicBoolean(false);

        final PrecedentDegreeInformation pdi = super.getPrecedentDegreeInformation();
        if (pdi != null) {

            // completedDegreeInformation
            PrecedentDegreeInformation completedDegreeInformation = getCompletedDegreeInformation();
            if (completedDegreeInformation == null) {
                completedDegreeInformation = new PrecedentDegreeInformation();
                completedDegreeInformation.setCompletedStudentCandidacy(this);
                newCompletedPDI.set(true);
            }

            if (newCompletedPDI.get() || force) {
                completedDegreeInformation.setDegreeDesignation(pdi.getDegreeDesignation());
                completedDegreeInformation.setSchoolLevel(pdi.getSchoolLevel());
                completedDegreeInformation.setOtherSchoolLevel(pdi.getOtherSchoolLevel());
                completedDegreeInformation.setCountry(pdi.getCountry());
                completedDegreeInformation.setInstitution(pdi.getInstitution());
                completedDegreeInformation.setLastModifiedDate(pdi.getLastModifiedDate());

                completedDegreeInformation.setConclusionGrade(pdi.getConclusionGrade());
                completedDegreeInformation.setConclusionYear(pdi.getConclusionYear());
                completedDegreeInformation.setInstitutionType(pdi.getInstitutionType());

                completedDegreeInformation.setCycleType(pdi.getCycleType());
                completedDegreeInformation.setCountryHighSchool(pdi.getCountryHighSchool());
                completedDegreeInformation.setDistrict(pdi.getDistrict());
                completedDegreeInformation.setDistrictSubdivision(pdi.getDistrictSubdivision());
            }

            // previousDegreeInformation
            PrecedentDegreeInformation previousDegreeInformation = getPreviousDegreeInformation();
            if (previousDegreeInformation == null) {
                previousDegreeInformation = new PrecedentDegreeInformation();
                previousDegreeInformation.setPreviousStudentCandidacy(this);
                newPreviousPDI.set(true);
            }

            if (newPreviousPDI.get() || force) {
                pdi.copyDeprecatedPreviousDegreeInformationFields(previousDegreeInformation);
                
//                previousDegreeInformation.setDegreeDesignation(pdi.getPrecedentDegreeDesignation());
//                previousDegreeInformation.setSchoolLevel(pdi.getPrecedentSchoolLevel());
//                previousDegreeInformation.setOtherSchoolLevel(pdi.getOtherPrecedentSchoolLevel());
//                previousDegreeInformation.setCountry(pdi.getPrecedentCountry());
//                previousDegreeInformation.setInstitution(pdi.getPrecedentInstitution());
//                previousDegreeInformation.setLastModifiedDate(pdi.getLastModifiedDate());
//
//                previousDegreeInformation.setNumberOfEnrolmentsInPreviousDegrees(pdi.getNumberOfEnrolmentsInPreviousDegrees());
//                previousDegreeInformation.setMobilityProgramDuration(pdi.getMobilityProgramDuration());
            }

        }

        return newCompletedPDI.get() || newPreviousPDI.get();
    }

}
