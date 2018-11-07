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

package org.fenixedu.academic.domain.accounting;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;
import pt.ist.fenixframework.Atomic;

import static org.fenixedu.academic.domain.PaymentMethodLog.createLog;

public class PaymentMethod extends PaymentMethod_Base {

    public PaymentMethod(String code, LocalizedString description) {
        super();
        setBennu(Bennu.getInstance());
        setCode(code);
        setDescription(description);
        createLog(Bundle.MESSAGING, "log.paymentMethod.created", code, getFormattedDescriptionForLog(description));
    }

    public static PaymentMethod getSibsPaymentMethod() {
        return Bennu.getInstance().getSibsPaymentMethod();
    }

    public static PaymentMethod getCashPaymentMethod() {
        return Bennu.getInstance().getCashPaymentMethod();
    }

    public static List<PaymentMethod> all() {
        return Bennu.getInstance().getPaymentMethodSet().stream().sorted(Comparator.comparing(PaymentMethod::getCode))
                .collect(Collectors.toList());
    }

    @Atomic
    public static void setDefaultPaymentMethods(PaymentMethod defaultCashPaymentMethod, PaymentMethod defaultSibsPaymentMethod) {
        if (getCashPaymentMethod() != defaultCashPaymentMethod || getSibsPaymentMethod() != defaultSibsPaymentMethod) {
            defaultCashPaymentMethod.setCashBennu(Bennu.getInstance());
            defaultSibsPaymentMethod.setSibsBennu(Bennu.getInstance());
            createLog(Bundle.MESSAGING, "log.paymentMethod.defaultsChanged", getCashPaymentMethod().getName(),
                    getSibsPaymentMethod().getName());
        }
    }

    @Atomic
    public static PaymentMethod create(String code, LocalizedString description) {
        return Bennu.getInstance().getPaymentMethodSet().stream().filter(i -> i.getName().equals(code)).findAny()
                .orElseGet(() -> new PaymentMethod(code, description));
    }

    @Atomic
    public void edit(String code, LocalizedString description) {
        if (Bennu.getInstance().getPaymentMethodSet().stream().filter(i -> !i.equals(this))
                .noneMatch(i -> i.getCode().equals(code))) {
            if (!this.getCode().equals(code) || !this.getDescription().equals(description)) {
                createLog(Bundle.MESSAGING, "log.paymentMethod.edited", getCode(), code,
                        getFormattedDescriptionForLog(getDescription()), getFormattedDescriptionForLog(description));
                setCode(code);
                setDescription(description);
            }
        } else {
            throw new DomainException(Optional.of(Bundle.ACCOUNTING), "error.payment.method.already.exists", code);
        }
    }

    @Atomic
    public void delete() {
        createLog(Bundle.MESSAGING, "log.paymentMethod.deleted", getCode(), getFormattedDescriptionForLog(getDescription()));
        setBennu(null);
        setCashBennu(null);
        setSibsBennu(null);
        super.deleteDomainObject();
    }

    public String getName() {
        return getCode();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return getDescription().getContent(locale);
    }

    public String getFormattedDescriptionForLog(LocalizedString description) {
        Set<Locale> localeSet = description.getLocales();
        StringBuilder localizedNamesForLog = new StringBuilder();

        for (Locale locale : localeSet) {
            localizedNamesForLog.append(locale.getLanguage()).append(": ");
            localizedNamesForLog.append(description.getContent(locale));

            if (!locale.equals(localeSet.toArray()[localeSet.size() - 1]))
                localizedNamesForLog.append(", ");
        }

        return localizedNamesForLog.toString();
    }

    public boolean isSibs() {
        return this == getSibsPaymentMethod();
    }

    public boolean isCash() {
        return this == getCashPaymentMethod();
    }

}