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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.commons.spreadsheet.Spreadsheet;

public class DissertationsProposalsReportFile extends DissertationsProposalsReportFile_Base {

    public DissertationsProposalsReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de propostas de dissertações com afiliações externas";
    }

    @Override
    protected String getPrefix() {
        return "propostas de dissertações com afiliações externas";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws IOException {
        listProposals(spreadsheet, getExecutionYear());
    }

    private void listProposals(Spreadsheet spreadsheet, final ExecutionYear executionYear) throws IOException {
        spreadsheet.setName("Propostas " + executionYear.getNextYearsYearString().replace("/", ""));
        spreadsheet.setHeader("Cursos");
        spreadsheet.setHeader("Siglas dos Cursos");
        spreadsheet.setHeader("Proposta Tese");
        spreadsheet.setHeader("Aluno atribuido");
        spreadsheet.setHeader("Distribuicao Creditos Orientador");
        spreadsheet.setHeader("Distribuicao Creditos Corientador");
        spreadsheet.setHeader("Empresa do Acompanhante");
        spreadsheet.setHeader("Local de Realização");
    }

}
