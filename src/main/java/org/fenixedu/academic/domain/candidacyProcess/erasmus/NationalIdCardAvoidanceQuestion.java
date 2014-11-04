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
package org.fenixedu.academic.domain.candidacyProcess.erasmus;

import java.util.Locale;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum NationalIdCardAvoidanceQuestion {
    UNANSWERED, NOT_OWNER_ELECTRONIC_ID_CARD, ELECTRONIC_ID_CARD_CODES_UNKNOWN, COUNTRY_NOT_LISTED_IN_FENIX_AUTHENTICATION,
    ELECTRONIC_ID_CARD_SUBMISSION_AVAILABILITY_UNKNOWN, ELECTRONIC_ID_CARD_SUBMISSION_TRUST_LACK, OTHER_REASON;

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.CANDIDATE, locale, getQualifiedName());
    }

    public String getQualifiedName() {
        return NationalIdCardAvoidanceQuestion.class.getSimpleName() + "." + name();
    }

    public String getName() {
        return name();
    }
}
