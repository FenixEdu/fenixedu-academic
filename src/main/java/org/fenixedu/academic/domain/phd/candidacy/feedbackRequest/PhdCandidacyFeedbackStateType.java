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
package org.fenixedu.academic.domain.phd.candidacy.feedbackRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.phd.PhdProcessStateType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdCandidacyFeedbackStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_FEEDBACK,

    CONCLUDED;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
    }

    private String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public static List<PhdCandidacyFeedbackStateType> getPossibleNextStates(PhdCandidacyFeedbackStateType type) {

        if (type == null) {
            return Collections.singletonList(NEW);
        }

        switch (type) {
        case NEW:
            return Collections.singletonList(WAITING_FOR_FEEDBACK);
        case WAITING_FOR_FEEDBACK:
            return Collections.singletonList(CONCLUDED);
        }

        return Collections.EMPTY_LIST;
    }
}
