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
/*
 * Created on Apr 15, 2005
 */
package org.fenixedu.academic.domain.person;

import java.util.Locale;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum IDDocumentType implements IPresentableEnum {

    IDENTITY_CARD,

    PASSPORT,

    FOREIGNER_IDENTITY_CARD,

    NATIVE_COUNTRY_IDENTITY_CARD,

    NAVY_IDENTITY_CARD,

    AIR_FORCE_IDENTITY_CARD,

    OTHER,

    MILITARY_IDENTITY_CARD,

    EXTERNAL,

    CITIZEN_CARD,

    RESIDENCE_AUTHORIZATION,
    
    EU_PERMANENT_RESIDENCE_CARD,
    
    EU_REGISTRATION_CERTIFICATE;

    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedNameI18N().getContent();
    }

    public String getLocalizedName(final Locale locale) {
        return getLocalizedNameI18N().getContent(locale);
    }

    public LocalizedString getLocalizedNameI18N() {
        return BundleUtil.getLocalizedString(Bundle.ENUMERATION, name());
    }
}
