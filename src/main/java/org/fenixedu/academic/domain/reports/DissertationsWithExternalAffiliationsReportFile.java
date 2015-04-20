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

import java.io.IOException;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;
import org.fenixedu.academic.domain.thesis.ThesisParticipationType;
import org.fenixedu.academic.ui.struts.action.coordinator.thesis.ThesisPresentationState;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

public class DissertationsWithExternalAffiliationsReportFile extends DissertationsWithExternalAffiliationsReportFile_Base {

    public DissertationsWithExternalAffiliationsReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de dissertações com afiliações externas";
    }

    @Override
    protected String getPrefix() {
        return "dissertações com afiliações externas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
        list(spreadsheet, getExecutionYear());
    }

    private void list(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
        spreadsheet.setName("Dissertacoes " + executionYear.getYear().replace("/", ""));
        spreadsheet.setHeader("Numero aluno");
        spreadsheet.setHeader("Nome aluno");
        spreadsheet.setHeader("Tipo Curso");
        spreadsheet.setHeader("Curso");
        spreadsheet.setHeader("Sigla Curso");
        spreadsheet.setHeader("Tese");
        spreadsheet.setHeader("Estado da tese");
        spreadsheet.setHeader("Affiliacao Orientador");
        spreadsheet.setHeader("Distribuicao Creditos Orientador");
        spreadsheet.setHeader("Affiliacao Corientador");
        spreadsheet.setHeader("Distribuicao Creditos Corientador");

        for (final Thesis thesis : getRootDomainObject().getThesesSet()) {
            final Enrolment enrolment = thesis.getEnrolment();
            final ExecutionSemester executionPeriod = enrolment.getExecutionPeriod();
            if (executionPeriod.getExecutionYear() == executionYear) {
                final ThesisPresentationState thesisPresentationState =
                        ThesisPresentationState.getThesisPresentationState(thesis);;

                final Degree degree = enrolment.getStudentCurricularPlan().getDegree();
                final DegreeType degreeType = degree.getDegreeType();

                final Row row = spreadsheet.addRow();
                row.setCell(thesis.getStudent().getNumber().toString());
                row.setCell(thesis.getStudent().getPerson().getName());
                row.setCell(degreeType.getName().getContent());
                row.setCell(degree.getPresentationName());
                row.setCell(degree.getSigla());
                row.setCell(thesis.getTitle().getContent());
                row.setCell(thesisPresentationState.getName());

                addTeacherRows(thesis, row, ThesisParticipationType.ORIENTATOR);
                addTeacherRows(thesis, row, ThesisParticipationType.COORIENTATOR);
            }
        }
    }

    protected void addTeacherRows(final Thesis thesis, final Row row, final ThesisParticipationType thesisParticipationType) {
        final StringBuilder oasb = new StringBuilder();
        final StringBuilder odsb = new StringBuilder();
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getAllParticipants(thesisParticipationType)) {
            if (oasb.length() > 0) {
                oasb.append(" ");
            }
            final String affiliation = thesisEvaluationParticipant.getAffiliation();
            if (affiliation != null) {
                oasb.append(affiliation);
            } else {
                oasb.append("--");
            }
            if (odsb.length() > 0) {
                odsb.append(" ");
            }
            final double credistDistribution = getCreditsDistribution(thesisEvaluationParticipant);
            odsb.append(Double.toString(credistDistribution));
        }
        row.setCell(oasb.toString());
        row.setCell(odsb.toString());
    }

    private double getCreditsDistribution(ThesisEvaluationParticipant thesisEvaluationParticipant) {
        Thesis thesis = thesisEvaluationParticipant.getThesis();

        if (!thesis.hasCredits()) {
            return 0;
        }
        return thesisEvaluationParticipant.getPercentageDistribution();
    }

}
