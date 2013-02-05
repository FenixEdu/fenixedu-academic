package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport;
import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        Language.setLocale(Language.getDefaultLocale());
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

}