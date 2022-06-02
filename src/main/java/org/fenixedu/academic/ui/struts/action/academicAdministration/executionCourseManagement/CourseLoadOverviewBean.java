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
package org.fenixedu.academic.ui.struts.action.academicAdministration.executionCourseManagement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.spreadsheet.StyledExcelSpreadsheet;
import org.joda.time.Interval;

public class CourseLoadOverviewBean implements Serializable {

    private ExecutionSemester executionSemester;

    public CourseLoadOverviewBean(final ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public CourseLoadOverviewBean() {
        this(ExecutionSemester.readActualExecutionSemester());
    }

    public ExecutionSemester getExecutionSemester() {
        return executionSemester;
    }

    public void setExecutionSemester(final ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public StyledExcelSpreadsheet getInconsistencySpreadsheet() {
        final StyledExcelSpreadsheet spreadsheet =
                new StyledExcelSpreadsheet(BundleUtil.getString(Bundle.ACADEMIC, "label.course.load.inconsistency.filename")
                        + "_" + executionSemester.getExecutionYear().getYear().replace('/', '_') + "_"
                        + executionSemester.getSemester());
        CellStyle normalStyle = spreadsheet.getExcelStyle().getValueStyle();
        normalStyle.setAlignment(HorizontalAlignment.CENTER);

        HSSFWorkbook wb = spreadsheet.getWorkbook();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        HSSFCellStyle redStyle = wb.createCellStyle();
        redStyle.setFont(font);
        redStyle.setAlignment(HorizontalAlignment.CENTER);
        redStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
        redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFCellStyle yellowStyle = wb.createCellStyle();
        yellowStyle.setFont(font);
        yellowStyle.setAlignment(HorizontalAlignment.CENTER);
        yellowStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        spreadsheet.newHeaderRow();
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.department"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.degree"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.executionCourse"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.shift"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.shiftType"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.competenceCourse"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.curricularCourse"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.executionCourse"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.lessonInstances"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.lesson.count"));
        spreadsheet.addHeader(BundleUtil.getString(Bundle.ACADEMIC, "label.load.lessonInstances.count"));

        for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
            for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
                for (final Shift shift : courseLoad.getShiftsSet()) {
                    spreadsheet.newRow();
                    spreadsheet.addCell(getDepartmentString(executionCourse));
                    spreadsheet.addCell(executionCourse.getDegreePresentationString());
                    spreadsheet.addCell(executionCourse.getNameI18N().getContent());
                    spreadsheet.addCell(shift.getNome());
                    spreadsheet.addCell(courseLoad.getType().getFullNameTipoAula());
                    final BigDecimal competenceCourseLoad =
                            new BigDecimal(getCompetenceCourseLoad(courseLoad)).setScale(2, RoundingMode.HALF_EVEN);
                    final BigDecimal curricularCourseLoad =
                            new BigDecimal(getCurricularCourseLoad(courseLoad)).setScale(2, RoundingMode.HALF_EVEN);
                    final BigDecimal executionLoad = courseLoad.getTotalQuantity().setScale(2, RoundingMode.HALF_EVEN);
                    final BigDecimal shiftCourseLoad = getShiftCourseLoad(shift).setScale(2, RoundingMode.HALF_EVEN);
                    if (competenceCourseLoad.signum() < 0) {
                        spreadsheet.addCell(getCompetenceCourseLoadStrings(courseLoad), redStyle);
                    } else {
                        spreadsheet.addCell(competenceCourseLoad);
                    }
                    if (!competenceCourseLoad.equals(curricularCourseLoad) || curricularCourseLoad.signum() < 0) {
                        spreadsheet.addCell(getCurricularCourseLoadString(courseLoad), redStyle);
                    } else {
                        spreadsheet.addCell(curricularCourseLoad);
                    }
                    if (!executionLoad.equals(curricularCourseLoad)) {
                        spreadsheet.addCell(executionLoad, redStyle);
                    } else {
                        spreadsheet.addCell(executionLoad);
                    }
                    if (!shiftCourseLoad.equals(executionLoad)) {
                        if (isLargeDifference(shiftCourseLoad, executionLoad,
                                competenceCourseLoad.divide(new BigDecimal(14), 2, RoundingMode.HALF_EVEN))) {
                            spreadsheet.addCell(shiftCourseLoad, redStyle);
                        } else {
                            spreadsheet.addCell(shiftCourseLoad, yellowStyle);
                        }
                    } else {
                        spreadsheet.addCell(shiftCourseLoad);
                    }
                    spreadsheet.addCell(shift.getAssociatedLessonsSet().size());
                    spreadsheet.addCell(getLessonInstanceCount(shift));
                }
            }
        }

        final HSSFSheet sheet = wb.getSheetAt(0);
        sheet.createFreezePane(0, 1, 0, 1);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);
        sheet.autoSizeColumn(5, true);
        sheet.autoSizeColumn(6, true);
        sheet.autoSizeColumn(7, true);
        sheet.autoSizeColumn(8, true);
        sheet.autoSizeColumn(9, true);

        return spreadsheet;
    }

    private boolean isLargeDifference(final BigDecimal value1, final BigDecimal value2, final BigDecimal unitValue) {
        return value1.subtract(value2).abs().compareTo(unitValue) > 0;
    }

    private String getCompetenceCourseLoadStrings(final CourseLoad courseLoad) {
        return getCurricularCourseLoadString(courseLoad);
    }

    private double getCompetenceCourseLoad(final CourseLoad courseLoad) {
        return getCurricularCourseLoad(courseLoad);
    }

    private String getCurricularCourseLoadString(final CourseLoad courseLoad) {
        final ShiftType shiftType = courseLoad.getType();
        final Set<Double> ds = new HashSet<Double>();
        for (final CurricularCourse curricularCourse : courseLoad.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            final double curricularCourseLoad = getCurricularCourseLoad(curricularCourse, shiftType);
            ds.add(new Double(curricularCourseLoad * 14));
        }
        final StringBuilder builder = new StringBuilder();
        for (final Double d : ds) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(d);
        }
        return builder.toString();
    }

    private double getCurricularCourseLoad(final CourseLoad courseLoad) {
        final ShiftType shiftType = courseLoad.getType();
        double result = -1d;
        for (final CurricularCourse curricularCourse : courseLoad.getExecutionCourse().getAssociatedCurricularCoursesSet()) {
            final double curricularCourseLoad = getCurricularCourseLoad(curricularCourse, shiftType);
            if (result == -1d || result == curricularCourseLoad) {
                result = curricularCourseLoad;
            } else if (result != -1d && result != curricularCourseLoad) {
                return -1d;
            }
        }
        return result * 14;
    }

    private double getCurricularCourseLoad(final CurricularCourse curricularCourse, final ShiftType shiftType) {
        if (shiftType == ShiftType.FIELD_WORK) {
            return curricularCourse.getFieldWorkHours(executionSemester);
        }
        if (shiftType == ShiftType.LABORATORIAL) {
            return curricularCourse.getLaboratorialHours(executionSemester);
        }
        if (shiftType == ShiftType.PRATICA) {
//                return curricularCourse.getPraticalHours();
        }
        if (shiftType == ShiftType.PROBLEMS) {
            return curricularCourse.getProblemsHours(executionSemester);
        }
        if (shiftType == ShiftType.SEMINARY) {
            return curricularCourse.getSeminaryHours(executionSemester);
        }
        if (shiftType == ShiftType.TEORICA) {
            return curricularCourse.getTheoreticalHours(executionSemester);
        }
        if (shiftType == ShiftType.TEORICO_PRATICA) {
//                return curricularCourse.getTheoPratHours();
        }
        if (shiftType == ShiftType.TRAINING_PERIOD) {
            return curricularCourse.getTrainingPeriodHours(executionSemester);
        }
        if (shiftType == ShiftType.TUTORIAL_ORIENTATION) {
            return curricularCourse.getTutorialOrientationHours(executionSemester);
        }
        return 0d;
    }

    private final static BigDecimal MILIS_TO_HOURS_DIVOSOR = new BigDecimal(3600000);

    private BigDecimal getShiftCourseLoad(final Shift shift) {
        BigDecimal result = BigDecimal.ZERO;
        for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
            for (final Interval interval : lesson.getAllLessonIntervals()) {
                final BigDecimal duration = new BigDecimal(interval.toDurationMillis());
                result = result.add(duration.divide(MILIS_TO_HOURS_DIVOSOR));
            }
        }
        return result;
    }

    private Integer getLessonInstanceCount(final Shift shift) {
        int result = 0;
        for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
            result += lesson.getAllLessonIntervals().size();
        }
        return Integer.valueOf(result);
    }

    private Object getDepartmentString(final ExecutionCourse executionCourse) {
        final StringBuilder builder = new StringBuilder();
        for (final Department department : executionCourse.getDepartments()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(department.getAcronym());
        }
        return builder.toString();
    }

}
