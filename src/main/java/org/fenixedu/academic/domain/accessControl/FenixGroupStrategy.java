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
package org.fenixedu.academic.domain.accessControl;

import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.GroupStrategy;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class FenixGroupStrategy extends GroupStrategy {
    private static final long serialVersionUID = 4080161353335180912L;

    @Override
    public String getPresentationName() {
        final String name =
                BundleUtil.getString(getPresentationNameBundle(), getPresentationNameKey(), getPresentationNameKeyArgs());
        return name != null ? name : getExpression();
    }

    public String getPresentationNameBundle() {
        return Bundle.GROUP;
    }

    public String getPresentationNameKey() {
        return "label.name." + getClass().getSimpleName();
    }

    public String[] getPresentationNameKeyArgs() {
        return new String[0];
    }
}
