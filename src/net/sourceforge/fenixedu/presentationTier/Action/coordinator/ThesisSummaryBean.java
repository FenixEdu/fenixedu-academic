package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;

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
