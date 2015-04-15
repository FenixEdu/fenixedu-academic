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
package org.fenixedu.academic.ui.renderers.legacy;

import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixWebFramework.rendererExtensions.LocalizedStringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MultiLanguageStringInputRenderer extends LocalizedStringInputRenderer {

    @Override
    protected Converter getConverter() {
        return new MultiLanguageStringConverter();
    }

    @Override
    protected LocalizedString getLocalized(Object object) {
        if (object instanceof MultiLanguageString) {
            return ((MultiLanguageString) object).toLocalizedString();
        } else {
            return super.getLocalized(object);
        }
    }

    public static class MultiLanguageStringConverter extends LocalizedStringConverter {
        @Override
        protected Object processLocalized(LocalizedString mls) {
            return MultiLanguageString.fromLocalizedString(mls);
        }
    }

}
