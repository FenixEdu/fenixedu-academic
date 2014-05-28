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

package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

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
        for (CandidateSituation candidateSituation : getSituations()) {
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

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CandidateSituation> getSituations() {
        return getSituationsSet();
    }

    @Deprecated
    public boolean hasAnySituations() {
        return !getSituationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CandidateEnrolment> getCandidateEnrolments() {
        return getCandidateEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyCandidateEnrolments() {
        return !getCandidateEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasSpecializationArea() {
        return getSpecializationArea() != null;
    }

    @Deprecated
    public boolean hasSubstituteOrder() {
        return getSubstituteOrder() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasMajorDegree() {
        return getMajorDegree() != null;
    }

    @Deprecated
    public boolean hasMajorDegreeYear() {
        return getMajorDegreeYear() != null;
    }

    @Deprecated
    public boolean hasSpecialization() {
        return getSpecialization() != null;
    }

    @Deprecated
    public boolean hasCoursesToAssist() {
        return getCoursesToAssist() != null;
    }

    @Deprecated
    public boolean hasCandidateNumber() {
        return getCandidateNumber() != null;
    }

    @Deprecated
    public boolean hasCourseAssistant() {
        return getCourseAssistant() != null;
    }

    @Deprecated
    public boolean hasHasGuider() {
        return getHasGuider() != null;
    }

    @Deprecated
    public boolean hasGivenCredits() {
        return getGivenCredits() != null;
    }

    @Deprecated
    public boolean hasGivenCreditsRemarks() {
        return getGivenCreditsRemarks() != null;
    }

    @Deprecated
    public boolean hasAverage() {
        return getAverage() != null;
    }

    @Deprecated
    public boolean hasGuider() {
        return getGuider() != null;
    }

    @Deprecated
    public boolean hasMajorDegreeSchool() {
        return getMajorDegreeSchool() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
