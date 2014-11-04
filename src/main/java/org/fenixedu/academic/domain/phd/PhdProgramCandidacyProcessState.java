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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdProgramCandidacyProcessState implements PhdProcessStateType {

    PRE_CANDIDATE,

    STAND_BY_WITH_MISSING_INFORMATION,

    STAND_BY_WITH_COMPLETE_INFORMATION,

    PENDING_FOR_COORDINATOR_OPINION,

    REJECTED,

    WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION,

    RATIFIED_BY_SCIENTIFIC_COUNCIL,

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

    public static List<PhdProgramCandidacyProcessState> getPossibleNextStates(final PhdProgramCandidacyProcess process) {
        PhdProgramCandidacyProcessState type = process.getActiveState();
        return getPossibleNextStates(type);
    }

    public static List<PhdProgramCandidacyProcessState> getPossibleNextStates(final PhdProgramCandidacyProcessState type) {
        if (type == null) {
            return Arrays.asList(PRE_CANDIDATE, STAND_BY_WITH_MISSING_INFORMATION, STAND_BY_WITH_COMPLETE_INFORMATION);
        }

        switch (type) {
        case PRE_CANDIDATE:
            return Arrays.asList(STAND_BY_WITH_COMPLETE_INFORMATION, PENDING_FOR_COORDINATOR_OPINION, REJECTED);
        case STAND_BY_WITH_MISSING_INFORMATION:
            return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED);
        case STAND_BY_WITH_COMPLETE_INFORMATION:
            return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED);
        case PENDING_FOR_COORDINATOR_OPINION:
            return Arrays.asList(WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION, REJECTED);
        case WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION:
            return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION, REJECTED, RATIFIED_BY_SCIENTIFIC_COUNCIL);
        case RATIFIED_BY_SCIENTIFIC_COUNCIL:
            return Arrays.asList(CONCLUDED, REJECTED);
        case CONCLUDED:
            return Arrays.asList(RATIFIED_BY_SCIENTIFIC_COUNCIL, REJECTED);
        case REJECTED:
            return Arrays.asList(PENDING_FOR_COORDINATOR_OPINION);
        }

        return Collections.emptyList();
    }

}
