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
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

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

    @Override
    public LocalizedString getPresentationName() {
        return BundleUtil.getLocalizedString(Bundle.ENUMERATION,
                                             getClass().getSimpleName() + ".presentation.name",
                                             getYear().getAcademicCalendarEntry().getTitle().getContent(),
                                             getCycle() == null ? "-" : getCycle().getDescription(),
                                             getDegree().getSigla());
    }

}
