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

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EctsLabelDegreeReportFile extends EctsLabelDegreeReportFile_Base {

    public EctsLabelDegreeReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para ECTS LABEL Cursos";
    }

    @Override
    protected String getPrefix() {
        return "ectsLabel_Cursos";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {

        createEctsLabelDegreesHeader(spreadsheet);

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                        addEctsLabelDegreeRow(spreadsheet, degreeCurricularPlan, getExecutionYear());
                    }
                }
            }
        }
    }

    private void createEctsLabelDegreesHeader(final Spreadsheet spreadsheet) {
        spreadsheet.setHeaders(new String[] {

        "Nome",

        "Nome Inglês",

        "Tipo Curso",

        "Duração em anos",

        "Duração em Semanas de Estudo",

        "Créditos ECTS",

        "Requisitos de Ingresso",

        "Requisitos de Ingresso (inglês)",

        "Objectivos Educacionais",

        "Objectivos Educacionais (inglês)",

        "Acesso a um nível superior de estudos",

        "Acesso a um nível superior de estudos (inglês)",

        "Normas e Regulamentos",

        "Normas e Regulamentos (inglês)",

        "Coordenador",

        "Contactos",

        "Contactos (inglês)"

        });
    }

    private String getResponsibleCoordinatorNames(final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear) {
        final StringBuilder builder = new StringBuilder();
        final ExecutionDegree executionDegree = degreeCurricularPlan.getExecutionDegreeByYear(executionYear);
        final Iterator<Coordinator> coordinators = executionDegree.getResponsibleCoordinators().iterator();
        while (coordinators.hasNext()) {
            builder.append(coordinators.next().getPerson().getName()).append(coordinators.hasNext() ? ", " : "");
        }
        return builder.toString();
    }

    private void addEctsLabelDegreeRow(final Spreadsheet spreadsheet, final DegreeCurricularPlan degreeCurricularPlan,
            final ExecutionYear executionYear) {

        final Row row = spreadsheet.addRow();
        final Degree degree = degreeCurricularPlan.getDegree();

        row.setCell(normalize(degree.getNameFor(executionYear).getContent(MultiLanguageString.pt)));
        row.setCell(normalize(degree.getNameFor(executionYear).getContent(MultiLanguageString.en)));

        row.setCell(degree.getDegreeType().getLocalizedName());
        row.setCell(degree.getDegreeType().getYears());
        row.setCell(degree.getDegreeType().getYears() * 40);
        row.setCell(degree.getEctsCredits());

        final DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);
        if (degreeInfo != null) {
            row.setCell(normalize(degreeInfo.getDesignedFor(MultiLanguageString.pt)));
            row.setCell(normalize(degreeInfo.getDesignedFor(MultiLanguageString.en)));
            row.setCell(normalize(degreeInfo.getObjectives(MultiLanguageString.pt)));
            row.setCell(normalize(degreeInfo.getObjectives(MultiLanguageString.en)));
            row.setCell(normalize(degreeInfo.getProfessionalExits(MultiLanguageString.pt)));
            row.setCell(normalize(degreeInfo.getProfessionalExits(MultiLanguageString.en)));
            row.setCell(normalize(degreeInfo.getOperationalRegime(MultiLanguageString.pt)));
            row.setCell(normalize(degreeInfo.getOperationalRegime(MultiLanguageString.en)));
            row.setCell(normalize(getResponsibleCoordinatorNames(degreeCurricularPlan, executionYear)));
            row.setCell(normalize(degreeInfo.getAdditionalInfo(MultiLanguageString.pt)));
            row.setCell(normalize(degreeInfo.getAdditionalInfo(MultiLanguageString.en)));
        }
    }

}
