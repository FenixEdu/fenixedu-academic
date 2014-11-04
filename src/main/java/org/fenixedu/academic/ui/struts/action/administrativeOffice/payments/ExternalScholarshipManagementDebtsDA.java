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
package org.fenixedu.academic.ui.struts.action.administrativeOffice.payments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.accounting.PaymentMode;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.debts.ExternalScholarshipPhdGratuityContribuitionEvent;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityEvent;
import org.fenixedu.academic.domain.phd.debts.PhdGratuityExternalScholarshipExemption;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.phd.academicAdminOffice.PhdIndividualProgramProcessDA;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/fctDebts", module = "academicAdministration", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "selectPhdStudent", path = "/academicAdminOffice/payments/selectPhdStudent.jsp"),
        @Forward(name = "showScolarship", path = "/academicAdminOffice/payments/showScolarship.jsp") })
public class ExternalScholarshipManagementDebtsDA extends FenixDispatchAction {

    public static class AmountBean implements Serializable {
        private Money value;
        private DateTime paymentDate;

        public Money getValue() {
            return value;
        }

        public void setValue(Money value) {
            this.value = value;
        }

        public DateTime getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(DateTime paymentDate) {
            this.paymentDate = paymentDate;
        }
    }

    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
        final String processIdAttribute = (String) request.getAttribute("processId");
        return FenixFramework.getDomainObject(processIdAttribute != null ? processIdAttribute : (String) request
                .getParameter("processId"));
    }

    public ActionForward viewDebtsForProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        PhdIndividualProgramProcess process = getProcess(request);
        ArrayList<PhdGratuityExternalScholarshipExemption> list = new ArrayList<PhdGratuityExternalScholarshipExemption>();

        for (Event event : process.getPerson().getEventsSet()) {
            if (event instanceof PhdGratuityEvent) {
                for (Exemption exemption : event.getExemptionsSet()) {
                    if (exemption instanceof PhdGratuityExternalScholarshipExemption) {
                        PhdGratuityExternalScholarshipExemption phdExemption =
                                (PhdGratuityExternalScholarshipExemption) exemption;
                        list.add(phdExemption);
                    }
                }
            }
        }

        request.setAttribute("person", process.getPerson());
        request.setAttribute("debts", list);
        return mapping.findForward("selectPhdStudent");
    }

    public ActionForward prepareLiquidation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String exemptionId = request.getParameter("exemptiontId");
        PhdGratuityExternalScholarshipExemption exemption =
                (PhdGratuityExternalScholarshipExemption) FenixFramework.getDomainObject(exemptionId);
        request.setAttribute("processId", ((PhdGratuityEvent) exemption.getEvent()).getPhdIndividualProgramProcess()
                .getExternalId());
        request.setAttribute("exemption", exemption);
        request.setAttribute("person", exemption.getEvent().getPerson());
        AmountBean bean = new AmountBean();
        bean.setValue(exemption.getAmoutStillMissing());
        request.setAttribute("bean", bean);
        return mapping.findForward("showScolarship");
    }

    @Atomic
    public ActionForward liquidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        String exemptionId = request.getParameter("externalId");
        PhdGratuityExternalScholarshipExemption exemption =
                (PhdGratuityExternalScholarshipExemption) FenixFramework.getDomainObject(exemptionId == null ? (String) request
                        .getAttribute("externalId") : exemptionId);
        ExternalScholarshipPhdGratuityContribuitionEvent event = exemption.getExternalScholarshipPhdGratuityContribuitionEvent();
        AmountBean bean = getRenderedObject("bean");
        List<EntryDTO> list = new ArrayList<EntryDTO>();
        list.add(new EntryDTO(EntryType.EXTERNAL_SCOLARSHIP_PAYMENT, event, bean.getValue()));
        event.process(Authenticate.getUser(), list, new AccountingTransactionDetailDTO(bean.getPaymentDate(), PaymentMode.CASH));

        PhdGratuityEvent gratuityEvent = (PhdGratuityEvent) exemption.getEvent();
        PhdIndividualProgramProcess process = gratuityEvent.getPhdIndividualProgramProcess();
        request.setAttribute("processId", process.getExternalId());

        return viewDebtsForProcess(mapping, form, request, response);
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String exemptionId = request.getParameter("externalId");
        PhdGratuityExternalScholarshipExemption exemption =
                (PhdGratuityExternalScholarshipExemption) FenixFramework.getDomainObject(exemptionId == null ? (String) request
                        .getAttribute("externalId") : exemptionId);
        PhdGratuityEvent event = (PhdGratuityEvent) exemption.getEvent();
        PhdIndividualProgramProcess process = event.getPhdIndividualProgramProcess();
        request.setAttribute("processId", process.getExternalId());
        return viewDebtsForProcess(mapping, form, request, response);
    }
}
