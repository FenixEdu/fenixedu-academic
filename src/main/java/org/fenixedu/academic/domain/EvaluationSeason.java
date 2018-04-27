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
package org.fenixedu.academic.domain;

import java.util.stream.Stream;

import org.fenixedu.commons.i18n.LocalizedString;

public class EvaluationSeason extends EvaluationSeason_Base implements Comparable<EvaluationSeason> {

    public EvaluationSeason() {
        super();
        setEvaluationConfiguration(EvaluationConfiguration.getInstance());
    }

    public EvaluationSeason(LocalizedString acronym, LocalizedString name, boolean normal, boolean improvement,
            boolean specialAuthorization, boolean special) {
        this();
        setAcronym(acronym);
        setName(name);
        setNormal(normal);
        setImprovement(improvement);
        setSpecialAuthorization(specialAuthorization);
        setSpecial(special);
    }

    public boolean isNormal() {
        return getNormal();
    }

    public boolean isSpecial() {
        return getSpecial();
    }

    public boolean isImprovement() {
        return getImprovement();
    }

    public boolean isSpecialAuthorization() {
        return getSpecialAuthorization();
    }

    /**
     * @deprecated Hazardous behaviour.
     *             Random EvaluationSeason may be returned if multiple EvaluationSeasons are created with this property
     */
    @Deprecated
    public static EvaluationSeason readNormalSeason() {
        return all().filter(EvaluationSeason::isNormal).findAny().orElse(null);
    }

    public static Stream<EvaluationSeason> readNormalSeasons() {
        return all().filter(EvaluationSeason::isNormal);
    }

    /**
     * @deprecated Hazardous behaviour.
     *             Random EvaluationSeason may be returned if multiple EvaluationSeasons are created with this property
     */
    @Deprecated
    public static EvaluationSeason readSpecialSeason() {
        return all().filter(EvaluationSeason::isSpecial).findAny().orElse(null);
    }

    public static Stream<EvaluationSeason> readSpecialSeasons() {
        return all().filter(EvaluationSeason::isSpecial);
    }

    /**
     * @deprecated Hazardous behaviour.
     *             Random EvaluationSeason may be returned if multiple EvaluationSeasons are created with this property
     */
    @Deprecated
    public static EvaluationSeason readImprovementSeason() {
        return all().filter(EvaluationSeason::isImprovement).findAny().orElse(null);
    }

    public static Stream<EvaluationSeason> readImprovementSeasons() {
        return all().filter(EvaluationSeason::isImprovement);
    }

    /**
     * @deprecated Hazardous behaviour.
     *             Random EvaluationSeason may be returned if multiple EvaluationSeasons are created with this property
     */
    @Deprecated
    public static EvaluationSeason readSpecialAuthorization() {
        return all().filter(EvaluationSeason::isSpecialAuthorization).findAny().orElse(null);
    }

    public static Stream<EvaluationSeason> readSpecialAuthorizations() {
        return all().filter(EvaluationSeason::isSpecialAuthorization);
    }

    public static Stream<EvaluationSeason> all() {
        return EvaluationConfiguration.getInstance().getEvaluationSeasonSet().stream();
    }

    public Stream<OccupationPeriod> getExamPeriods(ExecutionDegree executionDegree, ExecutionSemester semester) {
        return executionDegree.getPeriodReferences(null, semester == null ? null : semester.getSemester(), null)
                .filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod);
    }

    public boolean isGradeSubmissionAvailable(CurricularCourse curricularCourse, ExecutionSemester semester) {
        final ExecutionDegree executionDegree = curricularCourse.getExecutionDegreeFor(semester.getExecutionYear());
        return executionDegree.getPeriodReferences(null, semester == null ? null : semester.getSemester(), null)
                .filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod).anyMatch(o -> o.getPeriodInterval().containsNow());
    }

    public Stream<OccupationPeriod> getGradeSubmissionPeriods(ExecutionDegree executionDegree, ExecutionSemester semester) {
        return executionDegree.getPeriodReferences(null, semester == null ? null : semester.getSemester(), null)
                .filter(r -> r.getEvaluationSeasonSet().contains(this)).distinct()
                .map(OccupationPeriodReference::getOccupationPeriod);
    }

    @Override
    public int compareTo(EvaluationSeason o) {
        return getExternalId().compareTo(o.getExternalId());
    }

}
