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
package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport;
import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.commons.i18n.I18N;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;

public class PublicRelationsStudentListQueueJob extends PublicRelationsStudentListQueueJob_Base {

    public PublicRelationsStudentListQueueJob(ExecutionYear executionYear, DegreeType degreeType, Boolean concluded,
            Boolean active) {
        super();
        setExecutionYear(executionYear);
        setDegreeType(degreeType);
        setActive(active);
        setConcluded(concluded);
    }

    @Override
    public String getDescription() {
        return "Listagem de Alunos para Relações públicas";
    }

    @Override
    public String getFilename() {
        return "listagem_" + getRequestDate().toString("yyyy_MM_dd_HH_mm") + ".xls";
    }

    @Override
    public QueueJobResult execute() throws Exception {
        I18N.setLocale(Locale.getDefault());
        final ExecutionYear executionYear = getExecutionYear();
        final DegreeType degreeType = getDegreeType();
        final boolean concluded = getConcluded();
        final boolean active = getActive();

        final StudentReportPredicate studentReportPredicate = new StudentReportPredicate();
        studentReportPredicate.setExecutionYear(executionYear);
        studentReportPredicate.setDegreeType(degreeType);
        studentReportPredicate.setConcluded(concluded);
        studentReportPredicate.setActive(active);

        final Spreadsheet spreadsheet = GenerateStudentReport.generateReport(studentReportPredicate);

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("application/vnd.ms-excel");
        final ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        spreadsheet.exportToXLSSheet(byteArrayOS);
        queueJobResult.setContent(byteArrayOS.toByteArray());
        return queueJobResult;
    }

    @Deprecated
    public boolean hasActive() {
        return getActive() != null;
    }

    @Deprecated
    public boolean hasConcluded() {
        return getConcluded() != null;
    }

    @Deprecated
    public boolean hasDegreeType() {
        return getDegreeType() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

}
