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
package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.pricesManagement;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.PartialRegistrationRegimeRequestPR;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.AcademicAdministrationApplication.AcademicAdminPricesApp;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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
                AcademicAuthorizationGroup.getOfficesForOperation(AccessControl.getPerson(), AcademicOperationType.MANAGE_PRICES);

        for (AdministrativeOffice office : offices) {
            sortedPostingRules.addAll(office.getServiceAgreementTemplate().getActiveVisiblePostingRules());
        }

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
