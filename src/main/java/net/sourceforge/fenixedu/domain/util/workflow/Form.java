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
package net.sourceforge.fenixedu.domain.util.workflow;

import java.io.Serializable;
import java.util.List;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public abstract class Form implements Serializable {

    public abstract List<LabelFormatter> validate();

    public abstract String getFormName();

    public String getSchemaName() {
        return getClass().getSimpleName();
    }

    public String getFormDescription() {
        return "";
    }

    public boolean isInput() {
        return true;
    }
}