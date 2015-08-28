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

package org.fenixedu.academic.ui.struts.action.student.candidacy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.installments.InstallmentForFirstTimeStudents;
import org.fenixedu.academic.domain.accounting.paymentCodes.InstallmentPaymentCode;
import org.fenixedu.academic.domain.candidacy.CandidacySummaryFile;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.student.ReadStudentTimeTable;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapping(path = "/firstTimeCandidacyDocuments", module = "student")
@Forwards({ @Forward(name = "showCandidacyDetails", path = "/student/candidacy/showCandidacyDetails.jsp"),
        @Forward(name = "printAllDocuments", path = "/student/candidacy/printAllDocuments.jsp") })
public class FirstTimeEnrolmentDocuments extends FenixDispatchAction {

    private static final Logger logger = LoggerFactory.getLogger(FirstTimeEnrolmentDocuments.class);

    public ActionForward generateDocuments(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {
        StudentCandidacy candidacy = getDomainObject(request, "candidacyID");
        request.setAttribute("candidacy", candidacy);
        request.setAttribute("registration", candidacy.getRegistration());
        request.setAttribute("executionYear", candidacy.getExecutionDegree().getExecutionYear());
        request.setAttribute("person", candidacy.getRegistration().getPerson());
        request.setAttribute("campus", candidacy.getRegistration().getCampus().getName());
        request.setAttribute("administrativeOfficeFeeAndInsurancePaymentCode",
                administrativeOfficeFeeAndInsurancePaymentCode(candidacy.getAvailablePaymentCodesSet()));
        request.setAttribute("installmentPaymentCodes", installmmentPaymentCodes(candidacy.getAvailablePaymentCodesSet()));
        request.setAttribute("totalGratuityPaymentCode", totalGratuityPaymentCode(candidacy.getAvailablePaymentCodesSet()));
        request.setAttribute("firstInstallmentEndDate",
                calculateFirstInstallmentEndDate(candidacy.getRegistration(), candidacy.getAvailablePaymentCodesSet()));
        request.setAttribute("sibsEntityCode", FenixEduAcademicConfiguration.getConfiguration().getSibsEntityCode());

        final List<InfoShowOccupation> infoLessons = ReadStudentTimeTable.run(candidacy.getRegistration(), null);
        request.setAttribute("infoLessons", infoLessons);

        return mapping.findForward("printAllDocuments");
    }

    public ActionForward showCandidacyDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixServiceException {

        final StudentCandidacy candidacy = getDomainObject(request, "candidacyID");
        request.setAttribute("candidacy", candidacy);
        return mapping.findForward("showCandidacyDetails");
    }

    public ActionForward showSummaryFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        CandidacySummaryFile file = ((StudentCandidacy) getDomainObject(request, "candidacyID")).getSummaryFile();

        response.reset();
        try {
            response.getOutputStream().write(file.getContent());
            response.setContentLength(file.getContent().length);
            response.setContentType("application/pdf");
            response.flushBuffer();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private Object totalGratuityPaymentCode(Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (PaymentCodeType.GRATUITY_FIRST_INSTALLMENT.equals(paymentCode.getType())
                    && !(paymentCode instanceof InstallmentPaymentCode)) {
                return paymentCode;
            }
        }

        return null;
    }

    private Object installmmentPaymentCodes(Collection<PaymentCode> availablePaymentCodes) {
        List<PaymentCode> installmentPaymentCodes = new ArrayList<PaymentCode>();

        CollectionUtils.select(availablePaymentCodes, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                PaymentCode paymentCode = (PaymentCode) arg0;

                if (paymentCode instanceof InstallmentPaymentCode) {
                    return true;
                }

                return false;
            }
        }, installmentPaymentCodes);

        Collections.sort(installmentPaymentCodes, new BeanComparator("code"));

        return installmentPaymentCodes;
    }

    private Object administrativeOfficeFeeAndInsurancePaymentCode(Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE.equals(paymentCode.getType())) {
                return paymentCode;
            }
        }

        return null;
    }

    private YearMonthDay calculateFirstInstallmentEndDate(final Registration registration,
            Collection<PaymentCode> availablePaymentCodes) {
        for (PaymentCode paymentCode : availablePaymentCodes) {
            if (!paymentCode.isInstallmentPaymentCode()) {
                continue;
            }

            InstallmentPaymentCode installmentPaymentCode = (InstallmentPaymentCode) paymentCode;
            if (!installmentPaymentCode.getInstallment().isForFirstTimeStudents()) {
                continue;
            }

            InstallmentForFirstTimeStudents firstInstallment =
                    (InstallmentForFirstTimeStudents) installmentPaymentCode.getInstallment();
            return registration.getStartDate().plusDays(firstInstallment.getNumberOfDaysToStartApplyingPenalty());
        }

        return null;
    }
}
