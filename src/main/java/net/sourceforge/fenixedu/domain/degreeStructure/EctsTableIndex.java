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

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class EctsTableIndex extends EctsTableIndex_Base {
    public EctsTableIndex(AcademicInterval year) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setYear(year);
    }

    public boolean availableAt(DateTime processingDate) {
        return getProcessingDate() != null && getProcessingDate().isBefore(processingDate);
    }

    public static EctsTableIndex readByYear(AcademicInterval year) {
        for (EctsTableIndex index : Bennu.getInstance().getEctsTableIndexSet()) {
            if (index.getYear().equals(year)) {
                return index;
            }
        }
        return null;
    }

    protected static EctsTableIndex readByYearProcessable(AcademicInterval year, DateTime processingDate) {
        EctsTableIndex index = readByYear(year);
        if (index != null && index.availableAt(processingDate)) {
            return index;
        }
        if (year.getPreviousAcademicInterval() != null) {
            return readByYearProcessable(year.getPreviousAcademicInterval(), processingDate);
        }
        throw new NoEctsComparabilityTableFound(year);
    }

    @Atomic
    public static EctsTableIndex readOrCreateByYear(AcademicInterval year) {
        EctsTableIndex index = readByYear(year);
        return index == null ? new EctsTableIndex(year) : index;
    }

    public EctsCompetenceCourseConversionTable getEnrolmentTableBy(CompetenceCourse competenceCourse) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsCompetenceCourseConversionTable) {
                EctsCompetenceCourseConversionTable ccTable = (EctsCompetenceCourseConversionTable) table;
                if (ccTable.getCompetenceCourse().equals(competenceCourse)) {
                    return ccTable;
                }
            }
        }
        return null;
    }

    public EctsDegreeByCurricularYearConversionTable getEnrolmentTableBy(Degree degree, CurricularYear curricularYear) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsDegreeByCurricularYearConversionTable) {
                EctsDegreeByCurricularYearConversionTable degreeTable = (EctsDegreeByCurricularYearConversionTable) table;
                if (degreeTable.getDegree().equals(degree) && degreeTable.getCurricularYear().equals(curricularYear)) {
                    return degreeTable;
                }
            }
        }
        return null;
    }

    public EctsInstitutionByCurricularYearConversionTable getEnrolmentTableBy(Unit unit, CurricularYear curricularYear,
            CycleType cycleType) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsInstitutionByCurricularYearConversionTable) {
                EctsInstitutionByCurricularYearConversionTable unitTable = (EctsInstitutionByCurricularYearConversionTable) table;
                if (unitTable.getSchool().equals(unit) && unitTable.getCurricularYear().equals(curricularYear)
                        && unitTable.getCycle().equals(cycleType)) {
                    return unitTable;
                }
            }
        }
        return null;
    }

    public EctsInstitutionConversionTable getEnrolmentTableBy(Unit unit) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsInstitutionConversionTable) {
                EctsInstitutionConversionTable unitTable = (EctsInstitutionConversionTable) table;
                return unitTable;
            }
        }
        return null;
    }

    public EctsDegreeGraduationGradeConversionTable getGraduationTableBy(Degree degree, CycleType cycleType) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsDegreeGraduationGradeConversionTable) {
                EctsDegreeGraduationGradeConversionTable degreeTable = (EctsDegreeGraduationGradeConversionTable) table;
                if (degreeTable.getDegree().equals(degree)
                        && (!degree.getDegreeType().isComposite() || degreeTable.getCycle().equals(cycleType))) {
                    return degreeTable;
                }
            }
        }
        return null;
    }

    public EctsCycleGraduationGradeConversionTable getGraduationTableBy(CycleType cycleType) {
        for (EctsConversionTable table : getTableSet()) {
            if (table instanceof EctsCycleGraduationGradeConversionTable) {
                EctsCycleGraduationGradeConversionTable cycleTable = (EctsCycleGraduationGradeConversionTable) table;
                if (cycleTable.getCycle().equals(cycleType)) {
                    return cycleTable;
                }
            }
        }
        return null;
    }

    public static Grade convertGradeToEcts(CurricularCourse curricularCourse, CurriculumLine curriculumLine, Grade grade,
            DateTime processingDate) {
        EctsTableIndex index = readByYearProcessable(curriculumLine.getExecutionYear().getAcademicInterval(), processingDate);
        return index.convertGradeToEcts(curricularCourse, curriculumLine, grade);
    }

    public static Grade convertGradeToEcts(Degree degree, CurriculumLine curriculumLine, Grade grade, DateTime processingDate) {
        EctsTableIndex index = readByYearProcessable(curriculumLine.getExecutionYear().getAcademicInterval(), processingDate);
        return index.convertGradeToEcts(degree, curriculumLine, grade);
    }

    protected Grade convertGradeToEcts(CurricularCourse curricularCourse, CurriculumLine curriculumLine, Grade grade) {
        if (curricularCourse.hasCompetenceCourse()) {
            EctsConversionTable table = getEnrolmentTableBy(curricularCourse.getCompetenceCourse());
            if (table != null) {
                return table.convert(grade);
            }
        }
        return convertGradeToEcts(curricularCourse.getDegree(), curriculumLine, grade);
    }

    protected Grade convertGradeToEcts(Degree degree, CurriculumLine curriculumLine, Grade grade) {
        if (curriculumLine.getParentCycleCurriculumGroup() == null) {
            return convertGradeToEcts(Bennu.getInstance().getInstitutionUnit(), curriculumLine, grade);
        }
        CurricularYear curricularYear =
                CurricularYear.readByYear(curriculumLine.getParentCycleCurriculumGroup()
                        .getCurriculum(curriculumLine.getExecutionYear()).getCurricularYear());
        EctsConversionTable table = getEnrolmentTableBy(degree, curricularYear);
        if (table != null) {
            return table.convert(grade);
        }
        return convertGradeToEcts(Bennu.getInstance().getInstitutionUnit(), curriculumLine, curricularYear, grade);
    }

    protected Grade convertGradeToEcts(Unit unit, CurriculumLine curriculumLine, CurricularYear curricularYear, Grade grade) {
        CycleType cycleType = curriculumLine.getParentCycleCurriculumGroup().getCycleType();
        EctsConversionTable table = getEnrolmentTableBy(unit, curricularYear, cycleType);
        if (table != null) {
            return table.convert(grade);
        }
        throw new NoEctsComparabilityTableFound(curriculumLine);
    }

    protected Grade convertGradeToEcts(Unit unit, CurriculumLine curriculumLine, Grade grade) {
        EctsConversionTable table = getEnrolmentTableBy(unit);
        if (table != null) {
            return table.convert(grade);
        }
        throw new NoEctsComparabilityTableFound(curriculumLine);
    }

    public static EctsGraduationGradeConversionTable getGraduationGradeConversionTable(Degree degree, CycleType cycleType,
            AcademicInterval year, DateTime processingDate) {
        EctsTableIndex index = readByYearProcessable(year, processingDate);
        return index.getGraduationGradeConversionTable(degree, cycleType, year);
    }

    protected EctsGraduationGradeConversionTable getGraduationGradeConversionTable(Degree degree, CycleType cycleType,
            AcademicInterval year) {
        EctsGraduationGradeConversionTable table = getGraduationTableBy(degree, cycleType);
        if (table != null) {
            return table;
        }
        return getGraduationGradeConversionTable(Bennu.getInstance().getInstitutionUnit(), cycleType, year);
    }

    protected EctsGraduationGradeConversionTable getGraduationGradeConversionTable(Unit institutionUnit, CycleType cycleType,
            AcademicInterval year) {
        EctsGraduationGradeConversionTable table = getGraduationTableBy(cycleType);
        if (table != null) {
            return table;
        }
        throw new NoEctsComparabilityTableFound(year, cycleType);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.degreeStructure.EctsConversionTable> getTable() {
        return getTableSet();
    }

    @Deprecated
    public boolean hasAnyTable() {
        return !getTableSet().isEmpty();
    }

    @Deprecated
    public boolean hasYear() {
        return getYear() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasProcessingDate() {
        return getProcessingDate() != null;
    }

}
