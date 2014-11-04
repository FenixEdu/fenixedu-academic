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
/*
 * MasterDegreeCandidate.java
 *
 * Created on 17 de Outubro de 2002, 9:53
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.studentCurricularPlan.Specialization;
import org.fenixedu.academic.util.SituationName;
import org.fenixedu.academic.util.State;

import org.fenixedu.bennu.core.domain.Bennu;

public class MasterDegreeCandidate extends MasterDegreeCandidate_Base {

    /***************************************************************************
     * PRIVATE METHODS *
     **************************************************************************/

    public MasterDegreeCandidate() {
        super();
        setRootDomainObject(Bennu.getInstance());
        this.setHasGuider(false);
        this.setCourseAssistant(false);
    }

    public MasterDegreeCandidate(Person person, ExecutionDegree executionDegree, Integer candidateNumber,
            Specialization specialization, String majorDegree, String majorDegreeSchool, Integer majorDegreeYear, Double average,
            Teacher guider, Boolean hasGuider, Boolean courseAssistant, String coursesToAssist) {
        this();
        this.setPerson(person);
        this.setExecutionDegree(executionDegree);
        this.setCandidateNumber(candidateNumber);
        this.setSpecialization(specialization);
        this.setMajorDegree(majorDegree);
        this.setMajorDegreeSchool(majorDegreeSchool);
        this.setMajorDegreeYear(majorDegreeYear);
        this.setAverage(average);
        this.setGuider(guider);
        this.setHasGuider(hasGuider);
        this.setCourseAssistant(courseAssistant);
        this.setCoursesToAssist(coursesToAssist);
    }

    public CandidateSituation getActiveCandidateSituation() {
        for (CandidateSituation candidateSituation : getSituationsSet()) {
            if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
                return candidateSituation;
            }
        }
        return null;
    }

    public boolean hasCandidateSituationWith(final SituationName situationName) {
        final State candidateSituationState = new State(State.ACTIVE);
        for (final CandidateSituation candidateSituation : this.getSituationsSet()) {
            if (candidateSituation.getSituation().equals(situationName)
                    && candidateSituation.getValidation().equals(candidateSituationState)) {
                return true;
            }
        }
        return false;
    }

    public static Set<MasterDegreeCandidate> readByExecutionDegreeOrSpecializationOrCandidateNumberOrSituationName(
            final ExecutionDegree executionDegree, final Specialization specialization, final Integer candidateNumber,
            final SituationName situationName) {

        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : Bennu.getInstance().getMasterDegreeCandidatesSet()) {
            if (executionDegree != null && masterDegreeCandidate.getExecutionDegree() != executionDegree) {
                continue;
            }
            if (specialization != null && masterDegreeCandidate.getSpecialization() != specialization) {
                continue;
            }
            if (candidateNumber != null && !masterDegreeCandidate.getCandidateNumber().equals(candidateNumber)) {
                continue;
            }
            if (situationName != null && !masterDegreeCandidate.hasCandidateSituationWith(situationName)) {
                continue;
            }
            result.add(masterDegreeCandidate);
        }
        return result;
    }

    public String getPrecedentDegreeDescription() {
        StringBuilder description = new StringBuilder();
        if (getMajorDegree() != null) {
            description.append(getMajorDegree());
        }
        description.append(" - ");
        if (getMajorDegreeYear() != null) {
            description.append(getMajorDegreeYear());
        }
        description.append(" - ");
        if (getMajorDegreeSchool() != null) {
            description.append(getMajorDegreeSchool());
        }
        return description.toString();
    }

    public static Integer generateCandidateNumberForSpecialization(ExecutionDegree executionDegree, Specialization specialization) {
        int maxCandidateNumber = 0;
        for (final MasterDegreeCandidate masterDegreeCandidate : executionDegree.getMasterDegreeCandidatesSet()) {
            if (masterDegreeCandidate.getSpecialization() == specialization && masterDegreeCandidate.getCandidateNumber() != null) {
                maxCandidateNumber = Math.max(maxCandidateNumber, masterDegreeCandidate.getCandidateNumber());
            }
        }
        return Integer.valueOf(++maxCandidateNumber);
    }

    public static List<CandidateSituation> getCandidateSituationsInSituation(ExecutionDegree executionDegree,
            List<SituationName> situationNames) {
        List<CandidateSituation> result = new ArrayList<CandidateSituation>();

        for (MasterDegreeCandidate candidate : executionDegree.getMasterDegreeCandidatesSet()) {
            for (CandidateSituation situation : candidate.getSituationsSet()) {

                if (situation.getValidation().getState() == null || situation.getValidation().getState() != State.ACTIVE) {
                    continue;
                }

                if (situationNames != null && !situationNames.contains(situation.getSituation())) {
                    continue;
                }

                result.add(situation);
            }
        }

        return result;
    }

    public static MasterDegreeCandidate getMasterDegreeCandidateBySpecializationAndCandidateNumber(
            ExecutionDegree executionDegree, Specialization specialization, Integer candidateNumber) {
        for (final MasterDegreeCandidate masterDegreeCandidate : executionDegree.getMasterDegreeCandidatesSet()) {
            if (masterDegreeCandidate.getSpecialization() == specialization
                    && masterDegreeCandidate.getCandidateNumber().equals(candidateNumber)) {
                return masterDegreeCandidate;
            }
        }
        return null;
    }

    public static Set<MasterDegreeCandidate> readMasterDegreeCandidates(DegreeCurricularPlan degreeCurricularPlan) {
        if (degreeCurricularPlan instanceof EmptyDegreeCurricularPlan) {
            return Collections.emptySet();
        }
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
            result.addAll(executionDegree.getMasterDegreeCandidatesSet());
        }
        return result;
    }

    public static Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySpecialization(
            DegreeCurricularPlan degreeCurricularPlan, Specialization specialization) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates(degreeCurricularPlan)) {
            if (masterDegreeCandidate.getSpecialization() == specialization) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public static Set<MasterDegreeCandidate> readMasterDegreeCandidatesBySituatioName(DegreeCurricularPlan degreeCurricularPlan,
            SituationName situationName) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates(degreeCurricularPlan)) {
            if (masterDegreeCandidate.hasCandidateSituationWith(situationName)) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public static Set<MasterDegreeCandidate> readMasterDegreeCandidatesByCourseAssistant(
            DegreeCurricularPlan degreeCurricularPlan, boolean courseAssistant) {
        final Set<MasterDegreeCandidate> result = new HashSet<MasterDegreeCandidate>();
        for (final MasterDegreeCandidate masterDegreeCandidate : readMasterDegreeCandidates(degreeCurricularPlan)) {
            if (masterDegreeCandidate.getCourseAssistant() == courseAssistant) {
                result.add(masterDegreeCandidate);
            }
        }
        return result;
    }

    public static MasterDegreeCandidate getMasterDegreeCandidate(StudentCurricularPlan studentCurricularPlan) {
        if (studentCurricularPlan.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
            if (studentCurricularPlan.getEnrolmentsSet().size() > 0) {
                ExecutionDegree firstExecutionDegree =
                        studentCurricularPlan.getDegreeCurricularPlan().getExecutionDegreeByYear(
                                studentCurricularPlan.getFirstExecutionPeriod().getExecutionYear());
                for (final MasterDegreeCandidate candidate : studentCurricularPlan.getPerson().getMasterDegreeCandidatesSet()) {
                    if (candidate.getExecutionDegree() == firstExecutionDegree) {
                        return candidate;
                    }
                }
            } else if (studentCurricularPlan.getPerson().getMasterDegreeCandidatesSet().size() == 1) {
                return studentCurricularPlan.getPerson().getMasterDegreeCandidatesSet().iterator().next();
            }
        }
        return null;
    }

    public static MasterDegreeCandidate getMasterDegreeCandidateByExecutionDegree(Person person, ExecutionDegree executionDegree) {
        for (final MasterDegreeCandidate masterDegreeCandidate : person.getMasterDegreeCandidatesSet()) {
            if (masterDegreeCandidate.getExecutionDegree() == executionDegree) {
                return masterDegreeCandidate;
            }
        }
        return null;
    }

}
