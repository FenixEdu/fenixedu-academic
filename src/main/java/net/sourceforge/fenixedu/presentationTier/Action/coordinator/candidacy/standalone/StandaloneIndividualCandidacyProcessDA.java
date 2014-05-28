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
package net.sourceforge.fenixedu.presentationTier.Action.coordinator.candidacy.standalone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.DegreeCoordinatorIndex;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
        path = "/caseHandlingStandaloneIndividualCandidacyProcess",
        module = "coordinator",
        formBeanClass = net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA.StandaloneIndividualCandidacyForm.class,
        functionality = DegreeCoordinatorIndex.class)
@Forwards({
        @Forward(name = "intro",
                path = "/coordinator/caseHandlingStandaloneCandidacyProcess.do?method=listProcessAllowedActivities"),
        @Forward(name = "list-allowed-activities",
                path = "/coordinator/candidacy/standalone/listIndividualCandidacyActivities.jsp") })
public class StandaloneIndividualCandidacyProcessDA extends
        net.sourceforge.fenixedu.presentationTier.Action.candidacy.standalone.StandaloneIndividualCandidacyProcessDA {

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
