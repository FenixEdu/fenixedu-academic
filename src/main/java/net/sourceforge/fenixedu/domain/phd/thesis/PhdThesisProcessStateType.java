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
package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdThesisProcessStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_JURY_CONSTITUTION,

    JURY_WAITING_FOR_VALIDATION,

    JURY_VALIDATED,

    WAITING_FOR_JURY_REPORTER_FEEDBACK,

    WAITING_FOR_THESIS_MEETING_SCHEDULING,

    WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING,

    THESIS_DISCUSSION_DATE_SCHECULED,

    WAITING_FOR_THESIS_RATIFICATION,

    WAITING_FOR_FINAL_GRADE,

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

    public static List<PhdThesisProcessStateType> getPossibleNextStates(final PhdThesisProcess process) {
        PhdThesisProcessStateType currentState = process.getActiveState();

        return getPossibleNextStates(currentState);
    }

    public static List<PhdThesisProcessStateType> getPossibleNextStates(final PhdThesisProcessStateType type) {
        if (type == null) {
            return Collections.singletonList(NEW);
        }

        switch (type) {
        case NEW:
            return Arrays.asList(WAITING_FOR_JURY_CONSTITUTION, WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case WAITING_FOR_JURY_CONSTITUTION:
            return Arrays.asList(JURY_WAITING_FOR_VALIDATION, JURY_VALIDATED, WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case JURY_WAITING_FOR_VALIDATION:
            return Arrays.asList(JURY_VALIDATED, WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case JURY_VALIDATED:
            return Collections.singletonList(WAITING_FOR_JURY_REPORTER_FEEDBACK);
        case WAITING_FOR_JURY_REPORTER_FEEDBACK:
            return Collections.singletonList(WAITING_FOR_THESIS_MEETING_SCHEDULING);
        case WAITING_FOR_THESIS_MEETING_SCHEDULING:
            return Collections.singletonList(WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING);
        case WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING:
            return Collections.singletonList(THESIS_DISCUSSION_DATE_SCHECULED);
        case THESIS_DISCUSSION_DATE_SCHECULED:
            return Collections.singletonList(WAITING_FOR_THESIS_RATIFICATION);
        case WAITING_FOR_THESIS_RATIFICATION:
            return Collections.singletonList(WAITING_FOR_FINAL_GRADE);
        case WAITING_FOR_FINAL_GRADE:
            return Collections.singletonList(CONCLUDED);
        }

        return Collections.EMPTY_LIST;
    }

}
