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
package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdMeetingSchedulingProcessStateType implements PhdProcessStateType {

    WAITING_FIRST_THESIS_MEETING_REQUEST,

    WAITING_FIRST_THESIS_MEETING_SCHEDULE,

    WITHOUT_THESIS_MEETING_REQUEST,

    WAITING_THESIS_MEETING_SCHEDULE;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    @Override
    public String getLocalizedName(Locale locale) {
        return BundleUtil.getString(Bundle.PHD, locale, getQualifiedName());
    }

    private String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public static List<PhdMeetingSchedulingProcessStateType> getPossibleNextStates(final PhdMeetingSchedulingProcess process) {
        PhdMeetingSchedulingProcessStateType activeState = process.getActiveState();
        return getPossibleNextStates(activeState);
    }

    public static List<PhdMeetingSchedulingProcessStateType> getPossibleNextStates(final PhdMeetingSchedulingProcessStateType type) {

        if (type == null) {
            return Arrays.asList(new PhdMeetingSchedulingProcessStateType[] { WAITING_FIRST_THESIS_MEETING_REQUEST,
                    WITHOUT_THESIS_MEETING_REQUEST });
        }

        switch (type) {
        case WAITING_FIRST_THESIS_MEETING_REQUEST:
            return Collections.singletonList(WAITING_FIRST_THESIS_MEETING_SCHEDULE);
        case WAITING_FIRST_THESIS_MEETING_SCHEDULE:
            return Collections.singletonList(WITHOUT_THESIS_MEETING_REQUEST);
        case WITHOUT_THESIS_MEETING_REQUEST:
            return Collections.singletonList(WAITING_THESIS_MEETING_SCHEDULE);
        case WAITING_THESIS_MEETING_SCHEDULE:
            return Collections.singletonList(WITHOUT_THESIS_MEETING_REQUEST);
        }

        return Collections.EMPTY_LIST;
    }
}
