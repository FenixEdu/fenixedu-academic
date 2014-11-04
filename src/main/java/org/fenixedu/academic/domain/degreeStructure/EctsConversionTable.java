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

import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;

public abstract class EctsConversionTable extends EctsConversionTable_Base implements IEctsConversionTable {
    protected void init(AcademicInterval year, EctsComparabilityTable table) {
        setYear(year);
        setEctsTable(table);
        for (EctsTableIndex index : Bennu.getInstance().getEctsTableIndexSet()) {
            if (index.getYear().equals(year)) {
                setIndex(index);
                return;
            }
        }
        setIndex(new EctsTableIndex(year));
    }

    public Grade convert(Grade grade) {
        switch (grade.getGradeScale()) {
        case TYPE20:
            return Grade.createGrade(getEctsTable().convert(grade.getIntegerValue()), GradeScale.TYPEECTS);
        case TYPEAP:
            return Grade.createGrade(GradeScale.NA, GradeScale.TYPEECTS);
        case TYPEAPT:
            return grade;
        default:
            throw new DomainException("error.ects.unable.to.convert.grade");
        }
    }

    public void delete() {
        setIndex(null);
        deleteDomainObject();
    }

}
