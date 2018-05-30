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
package org.fenixedu.academic.domain.degreeStructure;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

public abstract class EctsCurricularYearConversionTable extends EctsCurricularYearConversionTable_Base {
    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    protected void init(AcademicInterval year, CurricularYear curricularYear, EctsComparabilityTable table) {
        super.init(year, table);
        setCurricularYear(curricularYear);
    }

    @Override
    public void delete() {
        setCurricularYear(null);
        super.delete();
    }


}
