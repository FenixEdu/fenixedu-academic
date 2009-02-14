package net.sourceforge.fenixedu.domain;

import java.io.ByteArrayOutputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport;
import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.report.Spreadsheet;

public class PublicRelationsStudentListQueueJob extends PublicRelationsStudentListQueueJob_Base {
    
    public PublicRelationsStudentListQueueJob(ExecutionYear executionYear, DegreeType degreeType, Boolean concluded,
	    Boolean active) {
	super();
	setExecutionYear(executionYear);
	setDegreeType(degreeType);
	setActive(active);
	setConcluded(concluded);
    }

    public String getDescription() {
	return "Listagem de Alunos para Relações públicas";
    }

    public String getFilename() {
	return "listagem_" + getRequestDate().toString("yyyy_MM_dd_HH_mm") + ".xls";
    }

    public void execute() throws Exception {
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

	ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
	spreadsheet.exportToXLSSheet(byteArrayOS);
	setContentType("application/vnd.ms-excel");
	setContent(new ByteArray(byteArrayOS.toByteArray()));

    }
}