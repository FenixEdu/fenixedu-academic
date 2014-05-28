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
package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ManagerPaymentsApp;
import net.sourceforge.fenixedu.util.Money;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

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

        request.setAttribute("over23Size", over23Size);
        request.setAttribute("externalDegreeCandidacyForGraduatedPersonSize", externalDegreeCandidacyForGraduatedPersonSize);
        request.setAttribute("internalDegreeCandidacyForGraduatedPersonSize", internalDegreeCandidacyForGraduatedPersonSize);
        request.setAttribute("externalDegreeChangeSize", externalDegreeChangeSize);
        request.setAttribute("internalDegreeChangeSize", internalDegreeChangeSize);
        request.setAttribute("externalDegreeTransferSize", externalDegreeTransferSize);
        request.setAttribute("internalDegreeTransferSize", internalDegreeTransferSize);
        request.setAttribute("secondCycleSize", secondCycleSize);

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

    private final static Map<PaymentCodeType, Money> MINIMUM_MONEY_MAP = new HashMap<PaymentCodeType, Money>();
    private final static Map<PaymentCodeType, Money> MAXIMUM_MONEY_MAP = new HashMap<PaymentCodeType, Money>();

    static {
        MINIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new Money(
                "140.00"));
        MINIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new Money(
                "50.00"));

        MINIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));
        MINIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("50.00"));

        MINIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));
        MINIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new Money("50.00"));

        MINIMUM_MONEY_MAP.put(PaymentCodeType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("100.00"));

        MINIMUM_MONEY_MAP.put(PaymentCodeType.OVER_23_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));

        /* Maximum */

        MAXIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new Money(
                "140.00"));
        MAXIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS, new Money(
                "50.00"));

        MAXIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));
        MAXIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("50.00"));

        MAXIMUM_MONEY_MAP.put(PaymentCodeType.EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));
        MAXIMUM_MONEY_MAP.put(PaymentCodeType.INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS, new Money("50.00"));

        //so that students can apply and pay for more than one degree
        MAXIMUM_MONEY_MAP.put(PaymentCodeType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS, new Money("3500.00"));

        MAXIMUM_MONEY_MAP.put(PaymentCodeType.OVER_23_INDIVIDUAL_CANDIDACY_PROCESS, new Money("140.00"));

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
                        MINIMUM_MONEY_MAP.get(bean.getType()), MAXIMUM_MONEY_MAP.get(bean.getType()),
                        bean.getNumberOfPaymentCodes());

        request.setAttribute("newPaymentCodes", paymentCodes);

        return mapping.findForward("showNewPaymentCodes");

    }
}
