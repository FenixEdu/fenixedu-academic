package net.sourceforge.fenixedu.presentationTier.Action.publicRelationsOffice;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport;
import net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.util.report.Spreadsheet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

@Mapping(path = "/studentReports", module = "publicRelations")
@Forwards( {
	@Forward(name = "search", path = "publicRelationsOffice-studentReportsSearch")
})
public class StudentReportsDA extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	StudentReportPredicate studentReportPredicate = (StudentReportPredicate) getRenderedObject();
	if (studentReportPredicate == null) {
	    studentReportPredicate = new StudentReportPredicate();
	}
	request.setAttribute("studentReportPredicate", studentReportPredicate);
	return mapping.findForward("search");
    }

    public ActionForward download(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final String executionYearIdString = request.getParameter("executionYearId");
	final Integer executionYearId = Integer.valueOf(executionYearIdString);
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);

	final String degreeTypeString = request.getParameter("degreeType");
	final DegreeType degreeType = degreeTypeString == null ? null : DegreeType.valueOf(degreeTypeString);

	final String concludedString = request.getParameter("concluded");
	final boolean concluded = Boolean.parseBoolean(concludedString);

	final String activeString = request.getParameter("active");
	final boolean active = Boolean.parseBoolean(activeString);

	final StudentReportPredicate studentReportPredicate = new StudentReportPredicate();
	studentReportPredicate.setExecutionYear(executionYear);
	studentReportPredicate.setDegreeType(degreeType);
	studentReportPredicate.setConcluded(concluded);
	studentReportPredicate.setActive(active);

	final Spreadsheet spreadsheet = GenerateStudentReport.generateReport(studentReportPredicate);

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition", "attachment; filename=listagem.xls");

	final ServletOutputStream writer = response.getOutputStream();
	spreadsheet.exportToXLSSheet(writer);	
	writer.flush();
	response.flushBuffer();

	return null;
    }

}
