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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.CandidacyAttributionType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup;

public class CandidaciesSummaryBean extends SummaryBean {
    private final Map<CandidacyAttributionType, Integer> candidaciesCount = new HashMap<CandidacyAttributionType, Integer>();
    private int candidatesWithoutDissertationEnrolment;
    private final Set<FinalDegreeWorkGroup> groups;

    public CandidaciesSummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
        super(executionDegree, degreeCurricularPlan);
        groups = getExecutionDegree().getScheduling().getAssociatedFinalDegreeWorkGroups();
        initCandidacies();
    }

    private void initCandidacies() {
        candidaciesCount.clear();
        candidatesWithoutDissertationEnrolment = 0;
        for (CandidacyAttributionType type : CandidacyAttributionType.values()) {
            candidaciesCount.put(type, 0);
        }
        setCandidatesCount();
    }

    private void incCandidacies(CandidacyAttributionType type) {
        Integer count = candidaciesCount.get(type);
        candidaciesCount.put(type, count + 1);
        Integer countTotal = candidaciesCount.get(CandidacyAttributionType.TOTAL);
        candidaciesCount.put(CandidacyAttributionType.TOTAL, countTotal + 1);
    }

    private void setCandidatesCount() {
        for (FinalDegreeWorkGroup group : groups) {
            if (group.isAttributed() && FinalDegreeWorkGroup.WITHOUT_DISSERTATION_PREDICATE.evaluate(group)) {
                candidatesWithoutDissertationEnrolment++;
            }
            if (group.isValid()) {
                incCandidacies(group.getCandidacyAttributionStatus());
            }
        }
    }

    public Integer getAttributedByCoordinatorCandidaciesCount() {
        return candidaciesCount.get(CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR);
    }

    public Integer getAttributedCandidaciesCount() {
        return candidaciesCount.get(CandidacyAttributionType.ATTRIBUTED);
    }

    public Integer getAttributedNotConfirmedCandidaciesCount() {
        return candidaciesCount.get(CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED);
    }

    public Integer getNotAttributedCandidaciesCount() {
        return candidaciesCount.get(CandidacyAttributionType.NOT_ATTRIBUTED);
    }

    public Integer getTotalCandidaciesCount() {
        return candidaciesCount.get(CandidacyAttributionType.TOTAL);
    }

    public CandidacyAttributionType getTotalCandidaciesKey() {
        return CandidacyAttributionType.TOTAL;
    }

    public CandidacyAttributionType getAttributedByCoordinatorCandidaciesKey() {
        return CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR;
    }

    public CandidacyAttributionType getAttributedCandidaciesKey() {
        return CandidacyAttributionType.ATTRIBUTED;
    }

    public CandidacyAttributionType getAttributedNotConfirmedCandidaciesKey() {
        return CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED;
    }

    public CandidacyAttributionType getNotAttributedCandidaciesKey() {
        return CandidacyAttributionType.NOT_ATTRIBUTED;
    }

    private String getDescription(CandidacyAttributionType status) {
        return status.getDescription(candidaciesCount.get(status));
    }

    public String getTotalCandidaciesLink() {
        return getDescription(CandidacyAttributionType.TOTAL);
    }

    public String getAttributedByCoordinatorCandidaciesLink() {
        return getDescription(CandidacyAttributionType.ATTRIBUTED_BY_CORDINATOR);
    }

    public String getAttributedCandidaciesLink() {
        return getDescription(CandidacyAttributionType.ATTRIBUTED);
    }

    public String getAttributedNotConfirmedCandidaciesLink() {
        return getDescription(CandidacyAttributionType.ATTRIBUTED_NOT_CONFIRMED);
    }

    public String getNotAttributedCandidaciesLink() {
        return getDescription(CandidacyAttributionType.NOT_ATTRIBUTED);
    }

    public int getCandidatesWithoutDissertationEnrolment() {
        return candidatesWithoutDissertationEnrolment;
    }

}
