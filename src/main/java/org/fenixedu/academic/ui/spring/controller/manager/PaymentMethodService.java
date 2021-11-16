/**
 * Copyright © 2018 Instituto Superior Técnico
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

package org.fenixedu.academic.ui.spring.controller.manager;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.PaymentMethodLog;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.stereotype.Service;

import jvstm.Atomic;

/**
 * @author Tiago Pinho (a@fenixedu.org)
 */
@Service
public class PaymentMethodService {

    public List<PaymentMethod> getAllPaymentMethods() {
        return PaymentMethod.all();
    }

    public List<PaymentMethodLog> getAllPaymentMethodsLogs() {
        return Bennu.getInstance().getPaymentMethodLogSet().stream().sorted(PaymentMethodLog.COMPARATOR_BY_WHEN_DATETIME).
                collect(Collectors.toList());
    }

    @Atomic
    public PaymentMethod createPaymentMethod(String code, LocalizedString description, String paymentReferenceFormat,
                                             boolean allowManualUse) {
        return PaymentMethod.create(code, description, paymentReferenceFormat == null ? "" : paymentReferenceFormat, allowManualUse);
    }

    @Atomic
    public void editPaymentMethod(PaymentMethod paymentMethod, String code, LocalizedString description,
            String paymentReferenceFormat, boolean allowManualUse) {
        paymentMethod.edit(code, description, paymentReferenceFormat == null ? "" : paymentReferenceFormat, allowManualUse);
    }

    @Atomic
    public void deletePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethod.delete();
    }

    @Atomic
    public void setDefaultPaymentMethods(PaymentMethod defaultCashPaymentMethod, PaymentMethod defaultSibsPaymentMethod,
            PaymentMethod defaultRefundPaymentMethod) {
        PaymentMethod.setDefaultPaymentMethods(defaultCashPaymentMethod, defaultSibsPaymentMethod, defaultRefundPaymentMethod);
    }

}
