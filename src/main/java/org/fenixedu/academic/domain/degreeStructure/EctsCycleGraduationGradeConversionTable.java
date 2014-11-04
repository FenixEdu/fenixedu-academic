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
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsCycleGraduationGradeConversionTable extends EctsCycleGraduationGradeConversionTable_Base {

    protected EctsCycleGraduationGradeConversionTable(Unit institution, AcademicInterval year, CycleType type,
            EctsComparabilityTable table, EctsComparabilityPercentages percentages) {
        super();
        init(year, type, table, percentages);
        setSchool(institution);
    }

    protected Bennu getRootDomainObject() {
        return getSchool().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getSchool();
    }

    @Atomic
    public static void createConversionTable(Unit institution, AcademicInterval year, CycleType type, String[] table,
            String[] percentages) {
        EctsCycleGraduationGradeConversionTable conversion = EctsTableIndex.readOrCreateByYear(year).getGraduationTableBy(type);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        EctsComparabilityPercentages ectsPercentages = EctsComparabilityPercentages.fromStringArray(percentages);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null && ectsPercentages != null) {
            new EctsCycleGraduationGradeConversionTable(institution, year, type, ectsTable, ectsPercentages);
        }
    }

    @Override
    public CurricularYear getCurricularYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() {
        setSchool(null);
        super.delete();
    }

}
