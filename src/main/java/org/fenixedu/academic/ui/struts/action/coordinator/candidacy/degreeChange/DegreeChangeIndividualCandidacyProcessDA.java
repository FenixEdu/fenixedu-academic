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
package org.fenixedu.academic.ui.struts.action.coordinator.candidacy.degreeChange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeIndividualCandidacyResultBean;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/caseHandlingDegreeChangeIndividualCandidacyProcess", module = "coordinator",
        formBeanClass = FenixActionForm.class, functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "intro",
                path = "/coordinator/caseHandlingDegreeChangeCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "introduce-candidacy-result", path = "/coordinator/candidacy/degreeChange/introduceCandidacyResult.jsp"),
        @Forward(name = "list-allowed-activities",
                path = "/coordinator/candidacy/degreeChange/listIndividualCandidacyActivities.jsp") })
public class DegreeChangeIndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.degreeChange.DegreeChangeIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward listProcessAllowedActivities(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("seriesGrade", getProcess(request).getCandidacy()
                .getDegreeChangeIndividualCandidacySeriesGradeForDegree(degreeCurricularPlan.getDegree()));
        return super.listProcessAllowedActivities(mapping, form, request, response);
    }

    @Override
    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        final String degreeCurricularPlanOID = DegreeCoordinatorIndex.findDegreeCurricularPlanID(request);
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanOID);
        request.setAttribute("individualCandidacyResultBean", new DegreeChangeIndividualCandidacyResultBean(getProcess(request),
                degreeCurricularPlan.getDegree()));
        return mapping.findForward("introduce-candidacy-result");
    }

}
