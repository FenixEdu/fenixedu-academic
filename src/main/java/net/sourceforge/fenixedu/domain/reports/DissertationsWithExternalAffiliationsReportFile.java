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

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType;
import net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

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
                row.setCell(degreeType.getLocalizedName());
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
            final double credistDistribution = thesisEvaluationParticipant.getCreditsDistribution();
            odsb.append(Double.toString(credistDistribution));
        }
        row.setCell(oasb.toString());
        row.setCell(odsb.toString());
    }

    private Set<String> getAffiliations(final Thesis thesis) {
        final Set<String> affiliations = new TreeSet<String>();
        for (final ThesisEvaluationParticipant thesisEvaluationParticipant : thesis.getParticipationsSet()) {
            final ThesisParticipationType thesisParticipationType = thesisEvaluationParticipant.getType();
            if (thesisParticipationType == ThesisParticipationType.ORIENTATOR
                    || thesisParticipationType == ThesisParticipationType.COORIENTATOR) {
                final String affiliation = thesisEvaluationParticipant.getAffiliation();
                if (affiliation != null) {
                    affiliations.add(affiliation);
                }
            }
        }
        return affiliations;
    }

}
