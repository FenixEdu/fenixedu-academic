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

import java.util.Iterator;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.commons.spreadsheet.Spreadsheet;
import org.fenixedu.commons.spreadsheet.Spreadsheet.Row;

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

        row.setCell(normalize(degree.getNameFor(executionYear).getContent(org.fenixedu.academic.util.LocaleUtils.PT)));
        row.setCell(normalize(degree.getNameFor(executionYear).getContent(org.fenixedu.academic.util.LocaleUtils.EN)));

        row.setCell(degree.getDegreeType().getName().getContent());
        row.setCell(degreeCurricularPlan.getDurationInYears());
        row.setCell(degreeCurricularPlan.getDurationInYears() * 40);
        row.setCell(degree.getEctsCredits());

        final DegreeInfo degreeInfo = degree.getMostRecentDegreeInfo(executionYear);
        if (degreeInfo != null) {
            row.setCell(normalize(degreeInfo.getDesignedFor(org.fenixedu.academic.util.LocaleUtils.PT)));
            row.setCell(normalize(degreeInfo.getDesignedFor(org.fenixedu.academic.util.LocaleUtils.EN)));
            row.setCell(normalize(degreeInfo.getObjectives(org.fenixedu.academic.util.LocaleUtils.PT)));
            row.setCell(normalize(degreeInfo.getObjectives(org.fenixedu.academic.util.LocaleUtils.EN)));
            row.setCell(normalize(degreeInfo.getProfessionalExits(org.fenixedu.academic.util.LocaleUtils.PT)));
            row.setCell(normalize(degreeInfo.getProfessionalExits(org.fenixedu.academic.util.LocaleUtils.EN)));
            row.setCell(normalize(degreeInfo.getOperationalRegime(org.fenixedu.academic.util.LocaleUtils.PT)));
            row.setCell(normalize(degreeInfo.getOperationalRegime(org.fenixedu.academic.util.LocaleUtils.EN)));
            row.setCell(normalize(getResponsibleCoordinatorNames(degreeCurricularPlan, executionYear)));
            row.setCell(normalize(degreeInfo.getAdditionalInfo(org.fenixedu.academic.util.LocaleUtils.PT)));
            row.setCell(normalize(degreeInfo.getAdditionalInfo(org.fenixedu.academic.util.LocaleUtils.EN)));
        }
    }

}
