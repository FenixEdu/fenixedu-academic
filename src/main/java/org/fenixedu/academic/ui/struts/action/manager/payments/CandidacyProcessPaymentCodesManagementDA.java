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
package org.fenixedu.academic.ui.struts.action.manager.payments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPaymentsApp;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

@StrutsFunctionality(app = ManagerPaymentsApp.class, path = "candidacy-payment-codes", titleKey = "title.candidacy.payment.codes")
@Mapping(path = "/candidacyPaymentCodes", module = "manager")
@Forwards({ @Forward(name = "index", path = "/manager/payments/candidacyProcess/index.jsp"),
        @Forward(name = "create", path = "/manager/payments/candidacyProcess/create.jsp"),
        @Forward(name = "showNewPaymentCodes", path = "/manager/payments/candidacyProcess/showNewPaymentCodes.jsp") })
public class CandidacyProcessPaymentCodesManagementDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward index(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        Integer over23Size =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(PaymentCodeType.OVER_23_INDIVIDUAL_CANDIDACY_PROCESS,
                        new YearMonthDay()).size();

        Integer externalDegreeCandidacyForGraduatedPersonSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new YearMonthDay())
                        .size();
        Integer internalDegreeCandidacyForGraduatedPersonSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new YearMonthDay())
                        .size();

        Integer externalDegreeChangeSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new YearMonthDay()).size();
        Integer internalDegreeChangeSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new YearMonthDay()).size();

        Integer externalDegreeTransferSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new YearMonthDay()).size();
        Integer internalDegreeTransferSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new YearMonthDay()).size();

        Integer secondCycleSize =
                IndividualCandidacyPaymentCode.getAvailablePaymentCodes(
                        PaymentCodeType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS, new YearMonthDay()).size();

        Integer phdSize = IndividualCandidacyPaymentCode.getAvailablePaymentCodes(PaymentCodeType.PHD_PROGRAM_CANDIDACY_PROCESS, new YearMonthDay()).size();
        
        request.setAttribute("over23Size", over23Size);
        request.setAttribute("externalDegreeCandidacyForGraduatedPersonSize", externalDegreeCandidacyForGraduatedPersonSize);
        request.setAttribute("internalDegreeCandidacyForGraduatedPersonSize", internalDegreeCandidacyForGraduatedPersonSize);
        request.setAttribute("externalDegreeChangeSize", externalDegreeChangeSize);
        request.setAttribute("internalDegreeChangeSize", internalDegreeChangeSize);
        request.setAttribute("externalDegreeTransferSize", externalDegreeTransferSize);
        request.setAttribute("internalDegreeTransferSize", internalDegreeTransferSize);
        request.setAttribute("secondCycleSize", secondCycleSize);
        request.setAttribute("phdSize", phdSize);

        return mapping.findForward("index");
    }

    public ActionForward prepareCreatePaymentCodes(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        String typeString = request.getParameter("type");
        PaymentCodeType type = PaymentCodeType.valueOf(typeString);

        CandidacyProcessPaymentCodeBean bean = new CandidacyProcessPaymentCodeBean(type);
        request.setAttribute("bean", bean);

        return mapping.findForward("create");
    }

    public ActionForward createPaymentCodesInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        CandidacyProcessPaymentCodeBean bean = getRenderedObject("bean");
        request.setAttribute("bean", bean);

        return mapping.findForward("create");
    }

    public ActionForward createPaymentCodes(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CandidacyProcessPaymentCodeBean bean = getRenderedObject("bean");

        List<IndividualCandidacyPaymentCode> paymentCodes =
                IndividualCandidacyPaymentCode.createPaymentCodes(bean.getType(), bean.getBeginDate(), bean.getEndDate(),
                        new Money(bean.getMinAmount()), new Money(bean.getMaxAmount()), bean.getNumberOfPaymentCodes());

        request.setAttribute("newPaymentCodes", paymentCodes);

        return mapping.findForward("showNewPaymentCodes");

    }
}
