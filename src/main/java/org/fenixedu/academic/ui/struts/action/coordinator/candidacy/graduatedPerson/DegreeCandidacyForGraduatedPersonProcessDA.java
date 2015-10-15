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
package org.fenixedu.academic.ui.struts.action.coordinator.candidacy.graduatedPerson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonIndividualProcess;
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingDegreeCandidacyForGraduatedPersonProcess", module = "coordinator",
        formBeanClass = DegreeCandidacyForGraduatedPersonProcessDA.DegreeCandidacyForGraduatedPersonProcessForm.class,
        functionality = DegreeCoordinatorIndex.class)
@Forwards({ @Forward(name = "intro", path = "/coordinator/candidacy/graduatedPerson/mainCandidacyProcess.jsp"),
        @Forward(name = "view-candidacy-results", path = "/coordinator/candidacy/graduatedPerson/viewCandidacyResults.jsp") })
public class DegreeCandidacyForGraduatedPersonProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.graduatedPerson.DegreeCandidacyForGraduatedPersonProcessDA {

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

            for (Coordinator coordinator : executionDegree.getCoordinatorsListSet()) {
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
        Collection<IndividualCandidacyProcess> processes = process.getChildProcessesSet();
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
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    private void setCandidacyProcessInformation(final ActionForm actionForm,
            final DegreeCandidacyForGraduatedPersonProcess process) {
        final DegreeCandidacyForGraduatedPersonProcessForm form = (DegreeCandidacyForGraduatedPersonProcessForm) actionForm;
        form.setSelectedProcessId(process.getExternalId());
        form.setExecutionIntervalId(process.getCandidacyExecutionInterval().getExternalId());
    }

    @Override
    protected void setStartInformation(ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        if (!hasExecutionInterval(request)) {
            final List<ExecutionInterval> executionIntervals = readExecutionIntervalFilteredByCoordinatorTeam(request);
            request.setAttribute("executionIntervals", executionIntervals);

            if (executionIntervals.size() == 1) {
                final ExecutionInterval executionInterval = executionIntervals.iterator().next();
                final List<DegreeCandidacyForGraduatedPersonProcess> candidacyProcesses =
                        getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    return;
                }
            }

            request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
            request.setAttribute("executionIntervals", executionIntervals);

        } else {
            final ExecutionInterval executionInterval = getExecutionInterval(request);
            final DegreeCandidacyForGraduatedPersonProcess candidacyProcess = getCandidacyProcess(request, executionInterval);

            if (candidacyProcess != null) {
                setCandidacyProcessInformation(request, candidacyProcess);
                setCandidacyProcessInformation(actionForm, getProcess(request));
            } else {
                final List<DegreeCandidacyForGraduatedPersonProcess> candidacyProcesses =
                        getCandidacyProcesses(executionInterval);

                if (candidacyProcesses.size() == 1) {
                    setCandidacyProcessInformation(request, candidacyProcesses.iterator().next());
                    setCandidacyProcessInformation(actionForm, getProcess(request));
                    request.setAttribute("candidacyProcesses", candidacyProcesses);
                    return;
                }

                request.setAttribute("canCreateProcess", canCreateProcess(getProcessType().getName()));
                request.setAttribute("executionIntervals", readExecutionIntervalFilteredByCoordinatorTeam(request));
            }
            request.setAttribute("candidacyProcesses", getCandidacyProcesses(executionInterval));
        }
    }

}
