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
package org.fenixedu.academic.ui.struts.action.academicAdministration.pricesManagement;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.ui.struts.action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPricesApp;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

@StrutsFunctionality(app = AcademicAdminPricesApp.class, path = "manage", titleKey = "label.pricesManagement",
        accessGroup = "academic(MANAGE_PRICES)")
@Mapping(path = "/pricesManagement", module = "academicAdministration")
@Forwards({ @Forward(name = "viewPrices", path = "/academicAdminOffice/pricesManagement/viewPrices.jsp"),
        @Forward(name = "editPrice", path = "/academicAdminOffice/pricesManagement/editPrice.jsp") })
public class PricesManagementDispatchAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewPrices(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        final SortedSet<PostingRule> sortedPostingRules = new TreeSet<PostingRule>(PostingRule.COMPARATOR_BY_EVENT_TYPE);

        final Set<AdministrativeOffice> offices =
                AcademicAccessRule.getOfficesAccessibleToFunction(AcademicOperationType.MANAGE_PRICES, Authenticate.getUser())
                        .collect(Collectors.toSet());

        for (AdministrativeOffice office : offices) {
            sortedPostingRules.addAll(office.getServiceAgreementTemplate().getActiveVisiblePostingRules());
        }
        
        sortedPostingRules.addAll(Bennu.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate().getActiveVisiblePostingRules());

        request.setAttribute("postingRules", sortedPostingRules);

        return mapping.findForward("viewPrices");
    }

    public ActionForward prepareEditPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("executionYearBean", new ExecutionYearBean(ExecutionYear.readCurrentExecutionYear()));
        request.setAttribute("postingRule", getDomainObject(request, "postingRuleId"));
        return mapping.findForward("editPrice");
    }

    public ActionForward changeExecutionYearPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExecutionYearBean executionYearBean = (ExecutionYearBean) getObjectFromViewState("executionYearBean");

        PostingRule postingRule =
                PartialRegistrationRegimeRequestPR
                        .readMostRecentPostingRuleForExecutionYear(executionYearBean.getExecutionYear());

        request.setAttribute("postingRule", postingRule);
        request.setAttribute("executionYearBean", executionYearBean);

        return mapping.findForward("editPrice");
    }

    public static class ExecutionYearBean implements java.io.Serializable {

        private static final long serialVersionUID = 1L;

        private ExecutionYear executionYear;

        public ExecutionYearBean(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }

        public ExecutionYear getExecutionYear() {
            return executionYear;
        }

        public void setExecutionYear(ExecutionYear executionYear) {
            this.executionYear = executionYear;
        }
    }
}
