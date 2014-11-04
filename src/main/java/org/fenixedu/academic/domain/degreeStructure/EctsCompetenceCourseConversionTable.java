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

import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;

public class EctsCompetenceCourseConversionTable extends EctsCompetenceCourseConversionTable_Base {

    protected EctsCompetenceCourseConversionTable(CompetenceCourse competenceCourse, AcademicInterval year,
            EctsComparabilityTable table) {
        super();
        init(year, table);
        setCompetenceCourse(competenceCourse);
    }

    protected Bennu getRootDomainObject() {
        return getCompetenceCourse().getRootDomainObject();
    }

    @Override
    public DomainObject getTargetEntity() {
        return getCompetenceCourse();
    }

    @Atomic
    public static void createConversionTable(CompetenceCourse competence, AcademicInterval year, String[] table) {
        EctsCompetenceCourseConversionTable conversion = EctsTableIndex.readOrCreateByYear(year).getEnrolmentTableBy(competence);
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null) {
            new EctsCompetenceCourseConversionTable(competence, year, ectsTable);
        }
    }

    @Override
    public CurricularYear getCurricularYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CycleType getCycle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EctsComparabilityPercentages getPercentages() {
        return new NullEctsComparabilityPercentages();
    }

    @Override
    public void delete() {
        setCompetenceCourse(null);
        super.delete();
    }

}
