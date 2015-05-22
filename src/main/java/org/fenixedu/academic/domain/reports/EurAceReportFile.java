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
package org.fenixedu.academic.domain.reports;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

public class EurAceReportFile extends EurAceReportFile_Base {

    public EurAceReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para EUR-ACE";
    }

    @Override
    protected String getPrefix() {
        return "eurAce";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        setDegreeHeaders(spreadsheet);
        spreadsheet.setHeader("Nome disciplina");
        spreadsheet.setHeader("Id do docente");
        spreadsheet.setHeader("Código disciplina execucao");

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
                            if (checkExecutionYear(getExecutionYear(), curricularCourse)) {
                                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                                    if (checkExecutionYear(getExecutionYear(), executionCourse)) {
                                        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                                            if (professorship.hasTeacher()) {
                                                final Teacher teacher = professorship.getTeacher();
                                                final Row row = spreadsheet.addRow();
                                                setDegreeCells(row, degree);
                                                row.setCell(curricularCourse.getName());
                                                row.setCell(teacher.getPerson().getUsername());
                                                row.setCell(GepReportFile.getExecutionCourseCode(executionCourse));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
