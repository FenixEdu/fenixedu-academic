package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.degreeChange;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.CoordinatedDegreeInfo;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/caseHandlingDegreeChangeCandidacyProcess", module = "coordinator",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/coordinator/candidacy/mainCandidacyProcess.jsp", tileProperties = @Tile(
        title = "private.coordinator.management.courses.applicationprocesses.coursechange"))

})
public class DegreeChangeCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    List<ExecutionInterval> readExecutionIntervalFilteredByCoordinatorTeam(final HttpServletRequest request) {
        final List<ExecutionInterval> returnExecutionIntervals = new ArrayList<ExecutionInterval>();

        final List<ExecutionInterval> executionIntervals =
                ExecutionInterval.readExecutionIntervalsWithCandidacyPeriod(getCandidacyPeriodType());

        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        for (ExecutionInterval interval : executionIntervals) {
            final ExecutionYear executionYear =
                    (interval instanceof ExecutionYear) ? (ExecutionYear) interval : ((ExecutionSemester) interval)
                            .getExecutionYear();
            final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);

            for (Coordinator coordinator : executionDegree.getCoordinatorsList()) {
                if (coordinator.getPerson() == AccessControl.getPerson()) {
                    returnExecutionIntervals.add(interval);
                    break;
                }
            }
        }

        return returnExecutionIntervals;
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        List<IndividualCandidacyProcess> processes = process.getChildProcesses();
        List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses =
                new ArrayList<IndividualCandidacyProcess>();
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        for (IndividualCandidacyProcess child : processes) {
            if (((DegreeChangeIndividualCandidacyProcess) child).getCandidacy().getSelectedDegree() == degreeCurricularPlan
                    .getDegree()) {
                selectedDegreesIndividualCandidacyProcesses.add(child);
            }
        }

        return selectedDegreesIndividualCandidacyProcesses;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = CoordinatedDegreeInfo.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return AbstractDomainObject.fromExternalId(degreeCurricularPlanOID);
        }

        return null;
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = readExecutionIntervalFilteredByCoordinatorTeam(request);
            if (executionIntervals.size() == 1) {
                setCandidacyProcessInformation(request, getCandidacyProcess(request, executionIntervals.get(0)));
            } else {
                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", executionIntervals);
            }
        } else {
            setCandidacyProcessInformation(request, getCandidacyProcess(request, getExecutionInterval(request)));
        }
    }

}
