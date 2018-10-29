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

package org.fenixedu.academic.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

/**
 * @author Tiago Pinho (a@fenixedu.org)
 */
public class PaymentMethodLog extends PaymentMethodLog_Base {

    public PaymentMethodLog(String description) {
        super();
        setRootDomainObjectPaymentMethodLog(Bennu.getInstance());
        setDescription(description);
    }

    public static PaymentMethodLog createPaymentMethodLog(String description) {
        return new PaymentMethodLog(description);
    }

    public static PaymentMethodLog createLog(String bundle, String key, String... args) {
        final String label = generateLabelDescription(bundle, key, args);
        return createPaymentMethodLog(label);
    }

    protected static String generateLabelDescription(String bundle, String key, String... args) {
        return BundleUtil.getString(bundle, key, args);
    }

}
