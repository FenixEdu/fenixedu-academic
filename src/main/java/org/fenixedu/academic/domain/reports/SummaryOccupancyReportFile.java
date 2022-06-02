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

import org.fenixedu.academic.domain.CourseLoad;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.LessonInstance;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

public class SummaryOccupancyReportFile extends SummaryOccupancyReportFile_Base {

    public SummaryOccupancyReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de Presencas em Aulas com base em Sumarios";
    }

    @Override
    protected String getPrefix() {
        return "summaryOccupancy";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("Ano Lectivo");
        spreadsheet.setHeader("Semestre");
        spreadsheet.setHeader("Código Execução Disciplina");
        spreadsheet.setHeader("Nome da Disciplina");
        spreadsheet.setHeader("Tipo do Turno");
        spreadsheet.setHeader("Turno");
        spreadsheet.setHeader("ID Docente");
        spreadsheet.setHeader("Nome do Docente");
        spreadsheet.setHeader("Horario");
        spreadsheet.setHeader("Sala");
        spreadsheet.setHeader("Numero Presencas");
        for (final ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriodsSet()) {
            for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                for (final Summary summary : executionCourse.getAssociatedSummariesSet()) {
                    final Row row = spreadsheet.addRow();
                    row.setCell(getExecutionYear().getYear());
                    row.setCell(executionSemester.getSemester());
                    row.setCell(GepReportFile.getExecutionCourseCode(executionCourse));
                    row.setCell(executionCourse.getNameI18N().getContent());
                    final LessonInstance lessonInstance = summary.getLessonInstance();
                    final CourseLoad courseLoad = lessonInstance == null ? null : lessonInstance.getCourseLoad();
                    final ShiftType shiftType = courseLoad == null ? null : courseLoad.getType();
                    row.setCell(shiftType == null ? "" : shiftType.getName());
                    final Shift shift = summary.getShift();
                    row.setCell(shift == null ? "" : shift.getNome());
                    row.setCell(getTeacherId(summary));
                    row.setCell(getTeacherName(summary));
                    row.setCell(getSchedule(summary));
                    final Space room = summary.getRoom();
                    row.setCell(room == null ? "" : room.getName());
                    row.setCell(summary.getStudentsNumber());
                }
            }
        }
    }

    private String getSchedule(final Summary summary) {
        final LessonInstance lessonInstance = summary.getLessonInstance();
        if (lessonInstance != null) {
            final DateTime begin = lessonInstance.getBeginDateTime();
            final DateTime end = lessonInstance.getEndDateTime();
            return begin.toString("yyyy-MM-dd HH:mm - ") + end.toString("yyyy-MM-dd HH:mm");
        }
        final DateTime summaryDateTime = summary.getSummaryDateTime();
        return summaryDateTime.toString("yyyy-MM-dd HH:mm");
    }

    private String getTeacherName(final Summary summary) {
        final Professorship professorship = summary.getProfessorship();
        if (professorship != null) {
            return professorship.getPerson().getName();
        }
        final Teacher teacher = summary.getTeacher();
        if (teacher != null) {
            return teacher.getPerson().getName();
        }
        return summary.getTeacherName();
    }

    private String getTeacherId(Summary summary) {
        final Professorship professorship = summary.getProfessorship();
        if (professorship != null) {
            return professorship.getPerson().getUsername();
        }
        final Teacher teacher = summary.getTeacher();
        if (teacher != null) {
            return teacher.getPerson().getUsername();
        }
        return null;
    }

}
