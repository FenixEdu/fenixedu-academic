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
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.candidacy;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.MasterDegreeOfficeApplication.MasterDegreeDfaApp;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = MasterDegreeDfaApp.class, path = "manage-periods", titleKey = "link.candidacy.dfa.periodsManagement")
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/dfaPeriodsManagement", formBean = "chooseExecutionYearForm")
@Forwards({
        @Forward(name = "chooseExecutionYear",
                path = "/masterDegreeAdministrativeOffice/candidacy/dfa/periodsManagement/chooseExecutionYear.jsp"),
        @Forward(name = "editCandidacyPeriod",
                path = "/masterDegreeAdministrativeOffice/candidacy/dfa/periodsManagement/editCandidacyPeriod.jsp"),
        @Forward(name = "showExecutionDegrees",
                path = "/masterDegreeAdministrativeOffice/candidacy/dfa/periodsManagement/showExecutionDegrees.jsp"),
        @Forward(name = "editRegistrationPeriod",
                path = "/masterDegreeAdministrativeOffice/candidacy/dfa/periodsManagement/editRegistrationPeriod.jsp") })
public class DFAPeriodsManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ((DynaActionForm) form).set("executionYear", ExecutionYear.readCurrentExecutionYear().getExternalId().toString());
        request.setAttribute("executionYears", ExecutionYear.readNotClosedExecutionYears());

        return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward showExecutionDegrees(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        final List<ExecutionDegree> executionDegrees =
                getExecutionYear(request, (DynaActionForm) form).getExecutionDegreesFor(
                        DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA);
        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        request.setAttribute("executionDegrees", executionDegrees);

        return mapping.findForward("showExecutionDegrees");
    }

    private ExecutionYear getExecutionYear(final HttpServletRequest request, final DynaActionForm dynaActionForm) {
        if (!StringUtils.isEmpty(dynaActionForm.getString("executionYear"))) {
            return FenixFramework.getDomainObject(dynaActionForm.getString("executionYear"));
        } else if (request.getParameter("executionYearId") != null) {
            return getDomainObject(request, "executionYearId");
        } else {
            return ExecutionYear.readCurrentExecutionYear();
        }
    }

    public ActionForward prepareEditCandidacyPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionDegree executionDegree = getExecutionDegree(request);

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("candidacyPeriod",
                executionDegree.getDegreeCurricularPlan().getCandidacyPeriod(executionDegree.getExecutionYear()));

        return mapping.findForward("editCandidacyPeriod");

    }

    public ActionForward prepareEditRegistrationPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        final ExecutionDegree executionDegree = getExecutionDegree(request);

        request.setAttribute("executionDegree", executionDegree);
        request.setAttribute("registrationPeriod",
                executionDegree.getDegreeCurricularPlan().getRegistrationPeriod(executionDegree.getExecutionYear()));

        return mapping.findForward("editRegistrationPeriod");

    }

    private ExecutionDegree getExecutionDegree(HttpServletRequest request) {
        return getDomainObject(request, "executionDegreeId");
    }

}
