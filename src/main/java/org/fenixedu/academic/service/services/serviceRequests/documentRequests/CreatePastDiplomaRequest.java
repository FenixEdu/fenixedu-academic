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
package org.fenixedu.academic.service.services.serviceRequests.documentRequests;

import org.fenixedu.academic.domain.accounting.PaymentMode;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.PastDegreeDiplomaRequestEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.serviceRequests.AcademicServiceRequestBean;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.service.factoryExecutors.DocumentRequestCreator;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;

public class CreatePastDiplomaRequest {

    @Atomic
    public static PastDiplomaRequest create(DocumentRequestCreator bean) {
        if (bean.getRegistration().getStartDate().isAfter(bean.getPastRequestDate())) {
            throw new DomainException("DiplomaRequest.cannot.request.before.registration.start");
        }

        bean.setRequestDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
        PastDiplomaRequest request = createPastDiplomaRequest(bean);
        request.getActiveSituation().setSituationDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
        createSituations(request, bean);
        return request;
    }

    private static PastDiplomaRequest createPastDiplomaRequest(DocumentRequestCreator bean) {
        PastDiplomaRequest request = new PastDiplomaRequest();
        request.init(bean);
        return request;
    }

    private static void createSituations(PastDiplomaRequest request, DocumentRequestCreateBean bean) {
        if (!bean.getRegistration().isRegistrationConclusionProcessed()) {
            throw new DomainException("DiplomaRequest.diploma.cannot.be.concluded");
        }

        LocalDate latestDate = bean.getPastRequestDate();
        if (bean.getPastPaymentDate() == null) {
            bean.setPastPaymentDate(latestDate);
        } else {
            latestDate = (latestDate.compareTo(bean.getPastPaymentDate()) < 0) ? bean.getPastPaymentDate() : latestDate;
        }
        if (bean.getPastEmissionDate() == null) {
            bean.setPastEmissionDate(latestDate);
        } else {
            latestDate = (latestDate.compareTo(bean.getPastEmissionDate()) < 0) ? bean.getPastEmissionDate() : latestDate;
        }
        if (bean.getPastDispatchDate() == null) {
            bean.setPastDispatchDate(latestDate);
        }

        createPaymentSituation(request, bean);
        process(request, bean.getPastRequestDate());
        request.setNumberOfPages(1);
        send(request, bean.getPastRequestDate());
        receive(request, bean.getPastRequestDate());
        conclude(request, bean.getPastEmissionDate());
        deliver(request, bean.getPastDispatchDate());
    }

    private static void createPaymentSituation(PastDiplomaRequest request, DocumentRequestCreateBean bean) {
    }

    private static boolean isPayed(DocumentRequestCreateBean bean) {
        return bean.getPastPaymentAmount() != null && bean.getPastPaymentAmount().isPositive();
    }

    private static AccountingTransactionDetailDTO createTransactionDetailDTO(DocumentRequestCreateBean bean) {
        return new AccountingTransactionDetailDTO(bean.getPastPaymentDate().toDateTimeAtStartOfDay(), PaymentMode.CASH);
    }

    private static void process(PastDiplomaRequest request, LocalDate requestDate) {
        editSituation(request, AcademicServiceRequestSituationType.PROCESSING, requestDate.toDateTimeAtStartOfDay()
                .plusMinutes(1));
    }

    private static void send(PastDiplomaRequest request, LocalDate requestDate) {
        editSituation(request, AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, requestDate.toDateTimeAtStartOfDay()
                .plusMinutes(2));
    }

    private static void receive(PastDiplomaRequest request, LocalDate requestDate) {
        editSituation(request, AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY, requestDate
                .toDateTimeAtStartOfDay().plusMinutes(3));
    }

    private static void conclude(PastDiplomaRequest request, LocalDate emissionDate) {
        editSituation(request, AcademicServiceRequestSituationType.CONCLUDED, emissionDate.toDateTimeAtStartOfDay()
                .plusMinutes(4));
    }

    private static void deliver(PastDiplomaRequest request, LocalDate dispatchDate) {
        editSituation(request, AcademicServiceRequestSituationType.DELIVERED, dispatchDate.toDateTimeAtStartOfDay()
                .plusMinutes(5));
    }

    private static void editSituation(PastDiplomaRequest request, AcademicServiceRequestSituationType situationType,
            DateTime situationDate) {
        final AcademicServiceRequestBean bean = new AcademicServiceRequestBean(situationType, AccessControl.getPerson());
        bean.setFinalSituationDate(situationDate);
        bean.setJustification("-");
        request.edit(bean);
    }

}
