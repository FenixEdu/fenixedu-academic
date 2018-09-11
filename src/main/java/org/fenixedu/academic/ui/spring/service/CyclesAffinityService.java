package org.fenixedu.academic.ui.spring.service;

import org.fenixedu.academic.domain.AffinityCycleCourseGroupLog;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;

import java.util.*;
import java.util.stream.Collectors;

import static org.fenixedu.academic.domain.AffinityCycleCourseGroupLog.createLog;

@Service
public class CyclesAffinityService {

    public List<CycleCourseGroup> getAllFirstCycles() {
        return DegreeCurricularPlan.readBolonhaDegreeCurricularPlans().stream().sorted(DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME).map(DegreeCurricularPlan::getFirstCycleCourseGroup).filter(Objects::nonNull).collect(
                Collectors.toList());
    }

    public List<CycleCourseGroup> getSecondCycleDegreesWithAffinity(final CycleCourseGroup firstCycle) {
        return firstCycle.getDestinationAffinitiesSet().stream().
                sorted(Comparator.comparing(CycleCourseGroup::getParentDegreeCurricularPlan, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME)).collect(Collectors.toList());
    }

    public List<CycleCourseGroup> getSecondCycleDegreesWithoutAffinity(final CycleCourseGroup firstCycle, final List<CycleCourseGroup> affinities) {

        return firstCycle.getAllPossibleAffinities().stream().
                filter(c -> !affinities.contains(c)).sorted(Comparator.comparing(CycleCourseGroup::getParentDegreeCurricularPlan, DegreeCurricularPlan.COMPARATOR_BY_PRESENTATION_NAME)).
                collect(Collectors.toList());
    }

    public List<CycleCourseGroup> getSecondCycleDegreesWithoutAffinity(final CycleCourseGroup firstCycle) {
        return getSecondCycleDegreesWithoutAffinity(firstCycle, getSecondCycleDegreesWithAffinity(firstCycle));
    }


    @Atomic
    public void addDestinationAffinity(CycleCourseGroup firstCycle, CycleCourseGroup secondCycle) {
        firstCycle.addDestinationAffinities(secondCycle);
        createLog(Bundle.MESSAGING, "log.affinity.added", firstCycle.getParentDegreeCurricularPlan().getPresentationName(), secondCycle.getParentDegreeCurricularPlan().getPresentationName());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void deleteDestinationAffinity(CycleCourseGroup firstCycle, CycleCourseGroup secondCycle) {
        firstCycle.removeDestinationAffinities(secondCycle);
        createLog(Bundle.MESSAGING, "log.affinity.removed", firstCycle.getParentDegreeCurricularPlan().getPresentationName(), secondCycle.getParentDegreeCurricularPlan().getPresentationName());
    }

    public List<AffinityCycleCourseGroupLog> getAffinityLogs() {
        return Bennu.getInstance().getAffinityCycleCourseGroupLogSet().stream().sorted(AffinityCycleCourseGroupLog.COMPARATOR_BY_WHEN_DATETIME).
                collect(Collectors.toList());
    }

}
