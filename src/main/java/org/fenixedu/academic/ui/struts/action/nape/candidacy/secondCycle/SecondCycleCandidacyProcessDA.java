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
package org.fenixedu.academic.ui.struts.action.nape.candidacy.secondCycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacyProcess;
import org.fenixedu.academic.ui.struts.action.nape.NapeApplication.NapeCandidaciesApp;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = NapeCandidaciesApp.class, path = "second-cycle", titleKey = "title.application.name.secondCycle.short")
@Mapping(path = "/caseHandlingSecondCycleCandidacyProcess", module = "nape",
        formBeanClass = SecondCycleCandidacyProcessDA.SecondCycleCandidacyProcessForm.class)
@Forwards({
        @Forward(name = "intro", path = "/nape/candidacy/secondCycle/mainCandidacyProcess.jsp"),
        @Forward(name = "view-child-process-with-missing-required-documents",
                path = "/candidacy/secondCycle/viewChildProcessWithMissingRequiredDocuments.jsp") })
public class SecondCycleCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.secondCycle.SecondCycleCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setChooseDegreeBean(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        setCandidacyProcessInformation(request, getProcess(request));
        setCandidacyProcessInformation(form, getProcess(request));
        request.setAttribute("candidacyProcesses", getCandidacyProcesses(getProcess(request).getCandidacyExecutionInterval()));
        return introForward(mapping);
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(final CandidacyProcess process, HttpServletRequest request) {
        Collection<IndividualCandidacyProcess> processes = process.getChildProcessesSet();
        List<IndividualCandidacyProcess> selectedDegreesIndividualCandidacyProcesses =
                new ArrayList<IndividualCandidacyProcess>();
        Degree selectedDegree = getChooseDegreeBean(request).getDegree();

        for (IndividualCandidacyProcess child : processes) {
            if ((selectedDegree == null)
                    || ((SecondCycleIndividualCandidacyProcess) child).getCandidacy().getSelectedDegreesSet()
                            .contains(selectedDegree)) {
                selectedDegreesIndividualCandidacyProcesses.add(child);
            }
        }

        return selectedDegreesIndividualCandidacyProcesses;
    }

}
