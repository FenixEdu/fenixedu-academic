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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.coordinator.DegreeCoordinatorIndex;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

@Mapping(
        path = "/caseHandlingStandaloneIndividualCandidacyProcess",
        module = "coordinator",
        formBeanClass = org.fenixedu.academic.ui.struts.action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA.StandaloneIndividualCandidacyForm.class,
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "intro",
                path = "/coordinator/caseHandlingStandaloneCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/coordinator/candidacy/standalone/listIndividualCandidacyActivities.jsp") })
public class StandaloneIndividualCandidacyProcessDA extends
        org.fenixedu.academic.ui.struts.action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        DegreeCoordinatorIndex.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward fillCandidacyInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward searchCurricularCourseByDegreePostBack(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward addCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward removeCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeEditCandidacyPersonalInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeEditCandidacyPersonalInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeEditCandidacyInformationInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeEditCandidacyInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeIntroduceCandidacyResultInvalid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeIntroduceCandidacyResult(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward prepareExecuteCreateRegistration(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

    @Override
    public ActionForward executeCreateRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        throw new RuntimeException("not allowed");
    }

}
