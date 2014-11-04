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
package org.fenixedu.academic.ui.struts.action.coordinator.candidacy.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.CandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.IndividualCandidacyProcess;
import org.fenixedu.academic.domain.candidacyProcess.standalone.StandaloneIndividualCandidacyProcess;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(
        path = "/caseHandlingStandaloneCandidacyProcess",
        module = "coordinator",
        formBeanClass = org.fenixedu.academic.ui.struts.action.candidacy.standalone.StandaloneCandidacyProcessDA.StandaloneCandidacyProcessForm.class,
        functionality = DegreeCoordinatorIndex.class)
@Forwards(@Forward(name = "intro", path = "/coordinator/candidacy/standalone/mainCandidacyProcess.jsp"))
public class StandaloneCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.standalone.StandaloneCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward prepareCreateNewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteSendToCoordinator(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeSendToCoordinator(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecutePrintCandidacies(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        throw new RuntimeException("not allowed");
    }

    @Override
    protected List<IndividualCandidacyProcess> getChildProcesses(CandidacyProcess process, HttpServletRequest request) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan(request);
        final List<IndividualCandidacyProcess> result = new ArrayList(super.getChildProcesses(process, request));
        for (final Iterator<IndividualCandidacyProcess> i = result.iterator(); i.hasNext();) {
            final StandaloneIndividualCandidacyProcess individualCandidacyProcess =
                    (StandaloneIndividualCandidacyProcess) i.next();
            if (!matchesDegree(degreeCurricularPlan, individualCandidacyProcess)) {
                i.remove();
            }
        }
        return result;
    }

    private boolean matchesDegree(final DegreeCurricularPlan degreeCurricularPlan,
            final StandaloneIndividualCandidacyProcess individualCandidacyProcess) {
        if (degreeCurricularPlan == null) {
            return true;
        }
        for (final CurricularCourse curricularCourse : individualCandidacyProcess.getCurricularCourses()) {
            final Degree degree = curricularCourse.getDegree();
            if (degree == degreeCurricularPlan.getDegree()) {
                return true;
            }
        }
        return false;
    }

    private DegreeCurricularPlan getDegreeCurricularPlan(final HttpServletRequest request) {
        final String param = request.getParameter("degreeCurricularPlanID");
        return (DegreeCurricularPlan) (param == null || param.isEmpty() ? null : FenixFramework.getDomainObject(param));
    }
}
