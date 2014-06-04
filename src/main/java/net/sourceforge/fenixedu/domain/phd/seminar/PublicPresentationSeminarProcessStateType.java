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
package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PublicPresentationSeminarProcessStateType implements PhdProcessStateType {

    WAITING_FOR_COMMISSION_CONSTITUTION,

    COMMISSION_WAITING_FOR_VALIDATION,

    COMMISSION_VALIDATED,

    PUBLIC_PRESENTATION_DATE_SCHEDULED,

    REPORT_WAITING_FOR_VALIDATION,

    REPORT_VALIDATED,

    EXEMPTED;

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

    public static List<PublicPresentationSeminarProcessStateType> getPossibleNextStates(PublicPresentationSeminarProcess process) {
        PublicPresentationSeminarProcessStateType activeState = process.getActiveState();

        return getPossibleNextStates(activeState);
    }

    public static List<PublicPresentationSeminarProcessStateType> getPossibleNextStates(
            final PublicPresentationSeminarProcessStateType type) {

        if (type == null) {
            return Collections.singletonList(WAITING_FOR_COMMISSION_CONSTITUTION);
        }

        switch (type) {
        case WAITING_FOR_COMMISSION_CONSTITUTION:
            return Arrays.asList(new PublicPresentationSeminarProcessStateType[] { COMMISSION_WAITING_FOR_VALIDATION, EXEMPTED });
        case COMMISSION_WAITING_FOR_VALIDATION:
            return Arrays.asList(new PublicPresentationSeminarProcessStateType[] { COMMISSION_VALIDATED,
                    WAITING_FOR_COMMISSION_CONSTITUTION });
        case COMMISSION_VALIDATED:
            return Collections.singletonList(PUBLIC_PRESENTATION_DATE_SCHEDULED);
        case PUBLIC_PRESENTATION_DATE_SCHEDULED:
            return Collections.singletonList(REPORT_WAITING_FOR_VALIDATION);
        case REPORT_WAITING_FOR_VALIDATION:
            return Collections.singletonList(REPORT_VALIDATED);
        case EXEMPTED:
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

}
