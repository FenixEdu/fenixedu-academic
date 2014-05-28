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
package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Shift;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class CourseLoadReportFile extends CourseLoadReportFile_Base {

    public CourseLoadReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de tipos de aula e carga horária";
    }

    @Override
    public String getDescription() {
        return getJobName() + " no formato " + getType().toUpperCase();
    }

    @Override
    protected String getPrefix() {
        return "carga_horaria";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {

        spreadsheet.setHeader("semestre");
        spreadsheet.setHeader("id execution course");
        spreadsheet.setHeader("id turno");
        spreadsheet.setHeader("nome turno");
        spreadsheet.setHeader("tipo aula");
        spreadsheet.setHeader("carga horas aula inseridas");
        spreadsheet.setHeader("horas aula sistema");
        spreadsheet.setHeader("total turnos");
        spreadsheet.setHeader("OID execucao disciplina");

        for (ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriods()) {
            for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCourses()) {

                for (CourseLoad courseLoad : executionCourse.getCourseLoads()) {

                    for (Shift shift : courseLoad.getShifts()) {

                        if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
                            continue;
                        }

                        Row row = spreadsheet.addRow();
                        row.setCell(executionSemester.getSemester());
                        row.setCell(executionCourse.getExternalId());
                        row.setCell(shift.getExternalId());
                        row.setCell(shift.getNome());
                        row.setCell(courseLoad.getType().name());
                        row.setCell(courseLoad.getTotalQuantity() != null ? courseLoad.getTotalQuantity().toPlainString()
                                .replace('.', ',') : StringUtils.EMPTY);
                        row.setCell(shift.getTotalHours() != null ? shift.getTotalHours().toPlainString().replace('.', ',') : StringUtils.EMPTY);
                        row.setCell(courseLoad.getShiftsSet().size());
                        row.setCell(String.valueOf(executionCourse.getOid()));

                    }
                }
            }
        }

    }

}
