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
package org.fenixedu.academic.domain.accounting;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

public class PaymentMethod extends PaymentMethod_Base {
    public PaymentMethod(String code, LocalizedString description) {
        setBennu(Bennu.getInstance());
        setCode(code);
        setDescription(description);
    }

    public static PaymentMethod getSibsPaymentMethod() {
        return Bennu.getInstance().getSibsPaymentMethod();
    }

    public static PaymentMethod getCashPaymentMethod() {
        return Bennu.getInstance().getCashPaymentMethod();
    }

    public static List<PaymentMethod> all() {
        return Bennu.getInstance().getPaymentMethodSet().stream().sorted(Comparator.comparing
                (PaymentMethod::getCode)).collect(Collectors.toList());
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


    public void delete() {
        setBennu(null);
        setCashBennu(null);
        setSibsBennu(null);
        super.deleteDomainObject();
    }

    public boolean isSibs() {
        return this == getSibsPaymentMethod();
    }

    public boolean isCash() {
        return this == getCashPaymentMethod();
    }
}