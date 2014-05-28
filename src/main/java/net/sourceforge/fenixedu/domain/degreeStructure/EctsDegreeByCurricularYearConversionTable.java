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

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsDegreeByCurricularYearConversionTable extends EctsDegreeByCurricularYearConversionTable_Base {

    protected EctsDegreeByCurricularYearConversionTable(Degree degree, AcademicInterval year, CurricularYear curricularYear,
            EctsComparabilityTable table) {
        super();
        init(year, curricularYear, table);
        setDegree(degree);
    }

    protected Bennu getRootDomainObject() {
        return getDegree().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getDegree();
    }

    @Atomic
    public static void createConversionTable(Degree degree, AcademicInterval year, CurricularYear curricularYear, String[] table) {
        EctsDegreeByCurricularYearConversionTable conversion =
                EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(degree, curricularYear);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsDegreeByCurricularYearConversionTable(degree, year, curricularYear, ectsTable);
        }
    }

    @Override
    public CycleType getCycle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        setDegree(null);
        super.delete();
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

}
