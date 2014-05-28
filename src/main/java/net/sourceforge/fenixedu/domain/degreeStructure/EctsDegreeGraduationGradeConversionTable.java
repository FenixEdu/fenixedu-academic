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

public class EctsDegreeGraduationGradeConversionTable extends EctsDegreeGraduationGradeConversionTable_Base {

    public static class ConversionTableCycleIsRequiredInIntegratedMasterDegree extends RuntimeException {
        private static final long serialVersionUID = 4018675993933248380L;
    }

    public static class ConversionTableCycleIsOnlyRequiredInIntegratedMasterDegree extends RuntimeException {
        private static final long serialVersionUID = -257788220757706395L;
    }

    protected EctsDegreeGraduationGradeConversionTable(Degree degree, AcademicInterval year, CycleType cycle,
            EctsComparabilityTable table, EctsComparabilityPercentages percentages) {
        super();
        init(year, cycle, table, percentages);
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
    public static void createConversionTable(Degree degree, AcademicInterval year, CycleType cycle, String[] table,
            String[] percentages) {
        EctsDegreeGraduationGradeConversionTable conversion =
                EctsTableIndex.readOrCreateByYear(year).getGraduationTableBy(degree, cycle);
        if (degree.getDegreeType().isComposite()) {
            if (cycle == null) {
                throw new ConversionTableCycleIsRequiredInIntegratedMasterDegree();
            }
        } else {
            if (cycle != null) {
                throw new ConversionTableCycleIsOnlyRequiredInIntegratedMasterDegree();
            }
        }
        EctsComparabilityTable ectsTable = EctsComparabilityTable.fromStringArray(table);
        EctsComparabilityPercentages ectsPercentages = EctsComparabilityPercentages.fromStringArray(percentages);
        if (conversion != null) {
            conversion.delete();
        }
        if (ectsTable != null && ectsPercentages != null) {
            new EctsDegreeGraduationGradeConversionTable(degree, year, cycle, ectsTable, ectsPercentages);
        }
    }

    @Override
    public CurricularYear getCurricularYear() {
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
