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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdIndividualProgramProcessState implements PhdProcessStateType {

    ABANDON,

    CANDIDACY(true),

    NOT_ADMITTED,

    CANCELLED,

    WORK_DEVELOPMENT(true),

    SUSPENDED,

    THESIS_DISCUSSION(true),

    FLUNKED,

    CONCLUDED(true),

    TRANSFERRED;

    private boolean activeState;

    private PhdIndividualProgramProcessState(boolean activeState) {
        this.activeState = activeState;
    }

    private PhdIndividualProgramProcessState() {
        this(false);
    }

    public boolean isActive() {
        return activeState;
    }

    public boolean isPhdActive() {
        return this == WORK_DEVELOPMENT || this == THESIS_DISCUSSION;
    }

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

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public static List<PhdIndividualProgramProcessState> getPossibleNextStates(final PhdIndividualProgramProcess process) {
        PhdIndividualProgramProcessState activeState = process.getActiveState();

        if (activeState == null) {
            return Collections.singletonList(CANDIDACY);
        }

        switch (activeState) {
        case CANDIDACY:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, NOT_ADMITTED, SUSPENDED, FLUNKED,
                    CANCELLED });
        case WORK_DEVELOPMENT:
            if (process.hasThesisProcess()) {
                return Arrays.asList(new PhdIndividualProgramProcessState[] { THESIS_DISCUSSION, NOT_ADMITTED, SUSPENDED,
                        FLUNKED, CANCELLED, TRANSFERRED, ABANDON });
            } else {
                return Arrays.asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED,
                        TRANSFERRED, ABANDON });
            }
        case THESIS_DISCUSSION:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED, CONCLUDED,
                    ABANDON });
        case ABANDON:
        case SUSPENDED:
        case FLUNKED:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { process.getLastActiveState().getType() });
        case NOT_ADMITTED:
        case CANCELLED:
        case CONCLUDED:
            return Collections.emptyList();
        case TRANSFERRED:
            return Collections.singletonList(WORK_DEVELOPMENT);
        default:
            throw new DomainException("error.PhdIndividualProgramProcess.unknown.process.state.types");
        }
    }

    public static List<PhdIndividualProgramProcessState> getPossibleNextStates(final PhdIndividualProgramProcessState stateType) {
        if (stateType == null) {
            return Collections.singletonList(CANDIDACY);
        }

        switch (stateType) {
        case CANDIDACY:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, NOT_ADMITTED, SUSPENDED, FLUNKED,
                    CANCELLED });
        case WORK_DEVELOPMENT:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { THESIS_DISCUSSION, NOT_ADMITTED, SUSPENDED, FLUNKED,
                    CANCELLED, TRANSFERRED });
        case THESIS_DISCUSSION:
            return Arrays
                    .asList(new PhdIndividualProgramProcessState[] { NOT_ADMITTED, SUSPENDED, FLUNKED, CANCELLED, CONCLUDED });
        case SUSPENDED:
        case FLUNKED:
            return Arrays.asList(new PhdIndividualProgramProcessState[] { WORK_DEVELOPMENT, THESIS_DISCUSSION });
        case NOT_ADMITTED:
        case CANCELLED:
            return Collections.emptyList();
        case CONCLUDED:
            return Collections.emptyList();
        case TRANSFERRED:
            return Collections.emptyList();
        default:
            throw new DomainException("error.PhdIndividualProgramProcess.unknown.process.state.types");
        }
    }
}
