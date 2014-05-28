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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.graduatedPerson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess", module = "coordinator",
        formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class, functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "intro", path = "/coordinator/candidacy/mainCandidacyProcess.jsp"),
        @Forward(name = "view-candidacy-results", path = "/coordinator/candidacy/graduatedPerson/viewCandidacyResults.jsp") })
public class DegreeCandidacyForGraduatedPersonProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
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
    protected List<CandidacyDegreeBean> createCandidacyDegreeBeans(HttpServletRequest request) {
        final DegreeCandidacyForGraduatedPersonProcess process = getProcess(request);
        final List<CandidacyDegreeBean> candidacyDegreeBeans = new ArrayList<CandidacyDegreeBean>();
        for (final DegreeCandidacyForGraduatedPersonIndividualProcess child : process
                .getAcceptedDegreeCandidacyForGraduatedPersonIndividualCandidacies()) {
            candidacyDegreeBeans.add(new DegreeCandidacyForGraduatedPersonDegreeBean(child));
        }
        Collections.sort(candidacyDegreeBeans);
        return candidacyDegreeBeans;
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        Collection<IndividualCandidacyProcess> processes = process.getChildProcesses();
        List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses =
                new ArrayList<IndividualCandidacyProcess>();
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);

        for (IndividualCandidacyProcess child : processes) {
            if (((DegreeCandidacyForGraduatedPersonIndividualProcess) child).getCandidacy().getSelectedDegree() == degreeCurricularPlan
                    .getDegree()) {
                selectedDegreesIndividualCandidacyProcesses.add(child);
            }
        }

        return selectedDegreesIndividualCandidacyProcesses;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

        if (degreeCurricularPlanOID != null) {
            return FenixFramework.getDomainObject(degreeCurricularPlanOID);
        }

        return null;
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = readExecutionIntervalFilteredByCoordinatorTeam(request);
            if (executionIntervals.size() == 1) {
                setCandidacyProcessInformation(request, getCandidacyProcess(request, executionIntervals.iterator().next()));
            } else {
                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", executionIntervals);
            }
        } else {
            setCandidacyProcessInformation(request, getCandidacyProcess(request, getExecutionInterval(request)));
        }
    }

}
