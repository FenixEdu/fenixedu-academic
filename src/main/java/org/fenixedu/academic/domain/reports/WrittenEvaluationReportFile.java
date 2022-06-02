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

import org.fenixedu.academic.domain.Evaluation;
import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;
import org.fenixedu.spaces.domain.Space;

public class WrittenEvaluationReportFile extends WrittenEvaluationReportFile_Base {

    public WrittenEvaluationReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de Avaliacoes Escritas";
    }

    @Override
    protected String getPrefix() {
        return "writtenEvaluations";
    }

    @Override
    public void renderReport(final Spreadsheet spreadsheet) throws Exception {
        spreadsheet.setHeader("Ano Lectivo");
        spreadsheet.setHeader("Semestre");
        spreadsheet.setHeader("Código Execução Disciplina");
        spreadsheet.setHeader("Nome da Disciplina");
        spreadsheet.setHeader("Código Avaliação");
        spreadsheet.setHeader("Tipo de Avaliação");
        spreadsheet.setHeader("Data Avaliação");
        spreadsheet.setHeader("Hora de Início");
        spreadsheet.setHeader("Hora de Fim");
        spreadsheet.setHeader("Salas");
        spreadsheet.setHeader("Capacidade Exame");
        spreadsheet.setHeader("Capacidade Normal");
        spreadsheet.setHeader("Número de Inscritos");

        for (final ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriodsSet()) {
            for (final ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                    if (evaluation instanceof WrittenEvaluation) {
                        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                        final Row row = spreadsheet.addRow();
                        row.setCell(getExecutionYear().getYear());
                        row.setCell(executionSemester.getSemester());
                        row.setCell(GepReportFile.getExecutionCourseCode(executionCourse));
                        row.setCell(executionCourse.getNameI18N().getContent());
                        row.setCell(GepReportFile.getWrittenEvaluationCode(writtenEvaluation));
                        row.setCell(writtenEvaluation instanceof Exam ? "Exame" : "Teste");
                        row.setCell(writtenEvaluation.getDayDateYearMonthDay().toString("yyyy-MM-dd"));
                        row.setCell(writtenEvaluation.getBeginningDateTime().toString("HH:mm"));
                        row.setCell(writtenEvaluation.getEndDateTime().toString("HH:mm"));
                        final StringBuilder rooms = new StringBuilder();
                        int examVacancy = 0;
                        int normalVacancy = 0;
                        for (final WrittenEvaluationSpaceOccupation roomOccupation : writtenEvaluation
                                .getWrittenEvaluationSpaceOccupationsSet()) {
                            final Space room = roomOccupation.getRoom();
                            if (rooms.length() > 0) {
                                rooms.append(", ");
                            }
                            rooms.append(room.getName());
                            examVacancy += room.<Integer>getMetadata("examCapacity").orElse(0);
                            normalVacancy += room.getAllocatableCapacity();
                        }
                        row.setCell(rooms.toString());
                        row.setCell(examVacancy);
                        row.setCell(normalVacancy);

                        if (writtenEvaluation.getEnrollmentBeginDayDateYearMonthDay() == null) {
                            row.setCell(" ");
                        } else {
                            final int enrolmentCount = writtenEvaluation.getWrittenEvaluationEnrolmentsSet().size();
                            row.setCell(enrolmentCount);
                        }
                    }
                }
            }
        }
    }

}
