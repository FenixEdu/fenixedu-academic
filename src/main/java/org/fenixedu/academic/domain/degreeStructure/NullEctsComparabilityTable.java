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
package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class NullEctsComparabilityTable extends EctsComparabilityTable {
    private static final long serialVersionUID = 117805162304348863L;

    NullEctsComparabilityTable() {
        super(new char[0]);
    }

    @Override
    public String convert(int grade) {
        return GradeScale.NA;
    }

    @Override
    public String getPrintableFormat() {
        return BundleUtil.getString(Bundle.GEP, "label.ects.table.nullPrintFormat");
    }

    @Override
    public String toString() {
        return "";
    }
}
