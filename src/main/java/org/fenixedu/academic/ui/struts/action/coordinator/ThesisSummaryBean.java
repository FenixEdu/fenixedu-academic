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
package org.fenixedu.academic.ui.struts.action.coordinator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState;

public class ThesisSummaryBean extends SummaryBean {
    private final Map<ThesisPresentationState, Integer> thesisCount = new LinkedHashMap<ThesisPresentationState, Integer>();
    private final Set<ThesisPresentationState> thesisExcludedStates = new HashSet<ThesisPresentationState>();;

    public ThesisSummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
        super(executionDegree, degreeCurricularPlan);
        thesisExcludedStates.add(ThesisPresentationState.UNKNOWN);
        initThesis();
    }

    private void initThesis() {
        thesisCount.clear();
        for (ThesisPresentationState state : ThesisPresentationState.values()) {
            if (!thesisExcludedStates.contains(state)) {
                thesisCount.put(state, 0);
            }
        }
        setThesisCount();
    }

    private void setThesisCount() {
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        ExecutionYear executionYear = getExecutionDegree().getExecutionYear().getNextExecutionYear();
        for (Enrolment enrolment : degreeCurricularPlan.getDissertationEnrolments(executionYear)) {
            ThesisPresentationState state = ThesisPresentationState.getThesisPresentationState(enrolment.getThesis());
            if (!thesisExcludedStates.contains(state)) {
                incThesis(state);
            }
        }
    }

    private void incThesis(ThesisPresentationState thesisPresentationState) {
        thesisCount.put(thesisPresentationState, thesisCount.get(thesisPresentationState) + 1);
    }

    public Map<ThesisPresentationState, Integer> getThesisCount() {
        return thesisCount;
    }
}
