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
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

public enum PhdIndividualProgramCollaborationType {

    CMU,

    UT_AUSTIN,

    MIT,

    FCT,

    EPFL(false, false),

    NONE,

    WITH_SUPERVISION(true),

    ERASMUS_MUNDUS(false),

    OTHER(true);

    private boolean needExtraInformation;
    private boolean generateCandidacyDebt;

    private PhdIndividualProgramCollaborationType(final boolean needExtraInformation, final boolean generateCandidacyDebt) {
        this.needExtraInformation = needExtraInformation;
        this.generateCandidacyDebt = generateCandidacyDebt;
    }

    private PhdIndividualProgramCollaborationType() {
        this(false, true);
    }

    private PhdIndividualProgramCollaborationType(final boolean needExtraInformation) {
        this(needExtraInformation, true);
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return BundleUtil.getString(Bundle.ENUMERATION, locale, getQualifiedName());
    }

    private String getQualifiedName() {
        return PhdIndividualProgramCollaborationType.class.getSimpleName() + "." + name();
    }

    public boolean needExtraInformation() {
        return needExtraInformation;
    }

    public boolean generateCandidacyDebt() {
        return generateCandidacyDebt;
    }

    public static Collection<PhdIndividualProgramCollaborationType> valuesAsList() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }

}
