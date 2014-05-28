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

import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.space.WrittenEvaluationSpaceOccupation;

import org.fenixedu.spaces.domain.Space;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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
        spreadsheet.setHeader("OID da Execução da Disciplina");
        spreadsheet.setHeader("Nome da Disciplina");
        spreadsheet.setHeader("OID Avaliação");
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
                        row.setCell(executionCourse.getExternalId());
                        row.setCell(executionCourse.getName());
                        row.setCell(writtenEvaluation.getExternalId());
                        row.setCell(writtenEvaluation instanceof Exam ? "Exame" : "Teste");
                        row.setCell(writtenEvaluation.getDayDateYearMonthDay().toString("yyyy-MM-dd"));
                        row.setCell(writtenEvaluation.getBeginningDateTime().toString("HH:mm"));
                        row.setCell(writtenEvaluation.getEndDateTime().toString("HH:mm"));
                        final StringBuilder rooms = new StringBuilder();
                        int examVacancy = 0;
                        int normalVacancy = 0;
                        for (final WrittenEvaluationSpaceOccupation roomOccupation : writtenEvaluation
                                .getWrittenEvaluationSpaceOccupations()) {
                            final Space room = roomOccupation.getRoom();
                            if (rooms.length() > 0) {
                                rooms.append(", ");
                            }
                            rooms.append(room.getName());
                            examVacancy += room.<Integer> getMetadata("examCapacity").orElse(0).intValue();
                            normalVacancy += room.getAllocatableCapacity().intValue();
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
