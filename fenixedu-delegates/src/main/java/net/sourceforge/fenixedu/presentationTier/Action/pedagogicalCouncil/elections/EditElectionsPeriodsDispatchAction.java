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
package org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.elections;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.pedagogicalCouncil.elections.DeleteDelegateCandidacyPeriod;
import org.fenixedu.academic.service.services.pedagogicalCouncil.elections.DeleteDelegateVotingPeriod;
import org.fenixedu.academic.service.services.pedagogicalCouncil.elections.EditDelegateCandidacyPeriod;
import org.fenixedu.academic.service.services.pedagogicalCouncil.elections.EditDelegateVotingPeriod;
import org.fenixedu.academic.dto.pedagogicalCouncil.elections.ElectionPeriodBean;
import org.fenixedu.academic.domain.elections.DelegateElection;
import org.fenixedu.academic.domain.elections.YearDelegateElection;
import org.fenixedu.academic.ui.struts.action.pedagogicalCouncil.PedagogicalCouncilApp.PedagogicalDelegateElectionsEntryPoint;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "pedagogicalCouncil", path = "/editElectionsPeriods",
        functionality = PedagogicalDelegateElectionsEntryPoint.class)
@Forwards(value = {
        @Forward(name = "createEditVotingPeriods", path = "/pedagogicalCouncil/elections/createEditVotingPeriods.jsp"),
        @Forward(name = "createEditCandidacyPeriods", path = "/pedagogicalCouncil/elections/createEditCandidacyPeriods.jsp") })
public class EditElectionsPeriodsDispatchAction extends ElectionsPeriodsManagementDispatchAction {

    private ActionForward prepareEditYearDelegateElectionPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response, DelegateElection election, YearMonthDay startDate,
            YearMonthDay endDate) throws Exception {
        final String forwardTo = (String) getFromRequest(request, "forwardTo");

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setDegree(election.getDegree());
        bean.setDegreeType(election.getDegree().getDegreeType());
        bean.setStartDate(startDate);
        bean.setEndDate(endDate);

        bean.setCurricularYear(((YearDelegateElection) election).getCurricularYear());

        bean.setElection(election);

        request.setAttribute("forwardTo", forwardTo);
        request.setAttribute("electionPeriodBean", bean);
        request.setAttribute("editElectionBean", bean);

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward prepareEditYearDelegateCandidacyPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DelegateElection election = FenixFramework.getDomainObject((String) getFromRequest(request, "selectedPeriod"));

        YearMonthDay startDate = election.getCandidacyStartDate();
        YearMonthDay endDate = election.getCandidacyEndDate();

        return prepareEditYearDelegateElectionPeriod(mapping, actionForm, request, response, election, startDate, endDate);
    }

    public ActionForward prepareEditYearDelegateVotingPeriod(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final DelegateElection election = FenixFramework.getDomainObject((String) getFromRequest(request, "selectedPeriod"));

        YearMonthDay startDate = election.getLastVotingStartDate();
        YearMonthDay endDate = election.getLastVotingEndDate();

        return prepareEditYearDelegateElectionPeriod(mapping, actionForm, request, response, election, startDate, endDate);
    }

    public ActionForward prepareEditYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String forwardTo = (String) getFromRequest(request, "forwardTo");
        final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");
        request.setAttribute("selectedDegrees", selectedDegrees);

        ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
        RenderUtils.invalidateViewState("electionPeriodBean");

        if (selectedDegrees != null) {
            request.setAttribute("selectedDegrees", selectedDegrees);
            request.setAttribute("editElectionBean", bean);
        }

        request.setAttribute("forwardTo", forwardTo);
        request.setAttribute("electionPeriodBean", bean);

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward prepareDeleteYearDelegateElectionsPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String forwardTo = (String) getFromRequest(request, "forwardTo");
        final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");

        ElectionPeriodBean bean = (ElectionPeriodBean) getFromRequest(request, "electionPeriodBean");
        RenderUtils.invalidateViewState("electionPeriodBean");

        if (selectedDegrees != null) {
            request.setAttribute("selectedDegrees", selectedDegrees);
            request.setAttribute("deleteElectionBean", bean);
        }

        if (selectedDegrees == null && request.getParameter("deleteVotingPeriod") != null) {
            request.setAttribute("deleteElectionBean", bean);
        }

        request.setAttribute("forwardTo", forwardTo);
        request.setAttribute("electionPeriodBean", bean);

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward editYearDelegateCandidacyPeriods(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");

        ElectionPeriodBean editElectionBean = (ElectionPeriodBean) getFromRequest(request, "editElectionBean");
        RenderUtils.invalidateViewState("editElectionBean");
        RenderUtils.invalidateViewState("electionPeriodBean");

        request.setAttribute("forwardTo", "createEditCandidacyPeriods");
        request.setAttribute("electionPeriodBean", editElectionBean);

        if (request.getParameter("deleteVotingPeriod") != null) {
            return prepareDeleteYearDelegateElectionsPeriods(mapping, actionForm, request, response);
        }

        if (request.getParameter("delete") != null) {
            if (selectedDegrees == null) {
                DeleteDelegateCandidacyPeriod.run(editElectionBean);
            } else {
                for (String degreeOID : selectedDegrees) {
                    DeleteDelegateCandidacyPeriod.run(editElectionBean, degreeOID);
                }
            }

            editElectionBean.setCurricularYear(null);
            editElectionBean.setStartDate(null);
            editElectionBean.setEndDate(null);
            editElectionBean.setDegree(null);
            editElectionBean.setElection(null);

            return selectDegreeType(mapping, actionForm, request, response);
        }

        if (selectedDegrees == null) {
            EditDelegateCandidacyPeriod.run(editElectionBean);
        } else {
            for (String degreeOID : selectedDegrees) {
                EditDelegateCandidacyPeriod.run(editElectionBean, degreeOID);
            }
        }

        editElectionBean.setCurricularYear(null);
        editElectionBean.setStartDate(null);
        editElectionBean.setEndDate(null);
        editElectionBean.setDegree(null);
        editElectionBean.setElection(null);

        return selectDegreeType(mapping, actionForm, request, response);
    }

    public ActionForward editYearDelegateVotingPeriods(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final List<String> selectedDegrees = (List<String>) getCheckboxesValues(request, "selectedDegrees");

        ElectionPeriodBean editElectionBean = (ElectionPeriodBean) getFromRequest(request, "editElectionBean");
        RenderUtils.invalidateViewState("editElectionBean");
        RenderUtils.invalidateViewState("electionPeriodBean");

        request.setAttribute("forwardTo", "createEditVotingPeriods");
        request.setAttribute("electionPeriodBean", editElectionBean);

        if (request.getParameter("deleteVotingPeriod") != null) {
            return prepareDeleteYearDelegateElectionsPeriods(mapping, actionForm, request, response);
        }

        if (request.getParameter("delete") != null) {
            if (selectedDegrees == null) {
                DeleteDelegateVotingPeriod.run(editElectionBean);
            } else {
                for (String degreeOID : selectedDegrees) {
                    DeleteDelegateVotingPeriod.run(editElectionBean, degreeOID);
                }
            }

            editElectionBean.setCurricularYear(null);
            editElectionBean.setStartDate(null);
            editElectionBean.setEndDate(null);
            editElectionBean.setDegree(null);
            editElectionBean.setElection(null);

            return selectDegreeType(mapping, actionForm, request, response);
        }

        if (selectedDegrees == null) {
            EditDelegateVotingPeriod.run(editElectionBean);
        } else {
            for (String degreeOID : selectedDegrees) {
                EditDelegateVotingPeriod.run(editElectionBean, degreeOID);
            }
        }

        editElectionBean.setCurricularYear(null);
        editElectionBean.setStartDate(null);
        editElectionBean.setEndDate(null);
        editElectionBean.setDegree(null);
        editElectionBean.setElection(null);

        return selectDegreeType(mapping, actionForm, request, response);
    }

}
