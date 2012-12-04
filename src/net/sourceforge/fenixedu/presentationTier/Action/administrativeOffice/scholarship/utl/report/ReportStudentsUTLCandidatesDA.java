package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/reportStudentsUTLCandidates", module = "academicAdminOffice")
@Forwards({

	@Forward(name = "prepare", path = "/academicAdminOffice/scholarship/utl/report/prepare.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.scholarships.utl")),
	@Forward(name = "showReport", path = "/academicAdminOffice/scholarship/utl/report/showReport.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.scholarships.utl")),
	@Forward(name = "viewDetails", path = "/academicAdminOffice/scholarship/utl/report/viewDetails.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.scholarships.utl")),
	@Forward(name = "prepareForOneStudent", path = "/academicAdminOffice/scholarship/utl/report/prepareForOneStudent.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.scholarships.utl")) })

public class ReportStudentsUTLCandidatesDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	ReportStudentsUTLCandidatesBean bean = new ReportStudentsUTLCandidatesBean();
	request.setAttribute("bean", bean);

	return mapping.findForward("prepare");
    }

    public ActionForward showReport(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) throws IOException {
	ReportStudentsUTLCandidatesBean bean = getRenderedObject("bean");

	POIFSFileSystem fs = new POIFSFileSystem(bean.getXlsFile());
	HSSFWorkbook wb = new HSSFWorkbook(fs);
	HSSFSheet sheet = wb.getSheetAt(0);

	if (sheet == null) {
	    addErrorMessage(request, "error", "error.academicAdminOffice.scholarship.utl.report.invalid.spreadsheet",
		    new String[0]);
	    return prepare(mapping, actionForm, request, response);
	}

	ReportStudentsUTLCandidates report = null;

	if (bean.getForFirstYear()) {
	    report = new ReportStudentsUTLCandidatesForFirstYear(bean.getExecutionYear(), sheet);
	} else {
	    report = new ReportStudentsUTLCandidates(bean.getExecutionYear(), sheet);
	}

	request.setAttribute("report", report);

	List<StudentLine> correctStudentLines = new ArrayList<StudentLine>();
	List<StudentLine> erroneousStudentLines = new ArrayList<StudentLine>();

	erroneousStudentLines.addAll(report.getErroneousStudentLines());

	for (StudentLine studentLine : report.getCorrectStudentLines()) {
	    if (studentLine.isAbleToReadAllValues()) {
		correctStudentLines.add(studentLine);
	    } else {
		erroneousStudentLines.add(studentLine);
	    }
	}

	request.setAttribute("correctStudentLines", correctStudentLines);
	request.setAttribute("erroneousStudentLines", erroneousStudentLines);

	return mapping.findForward("showReport");
    }

    public ActionForward viewDetails(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    final HttpServletResponse response) {
	return mapping.findForward("viewDetails");
    }

    public ActionForward exportReport(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	ReportStudentsUTLCandidates report = getRenderedObject("report");

	HSSFWorkbook generateReport = report.generateReport();

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=bolsa_accao_social_utl.xls");
	generateReport.write(response.getOutputStream());

	response.getOutputStream().flush();
	response.flushBuffer();

	return null;
    }

    public ActionForward exportErrors(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	ReportStudentsUTLCandidates report = getRenderedObject("report");

	HSSFWorkbook generateReport = report.generateErrors();

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-Disposition", "attachment; filename=erros_bolsa_accao_social.xls");
	generateReport.write(response.getOutputStream());

	response.getOutputStream().flush();
	response.flushBuffer();

	return null;
    }

    public ActionForward prepareForOneStudent(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ReportStudentsUTLCandidatesBean bean = new ReportStudentsUTLCandidatesBean();

	request.setAttribute("bean", bean);
	return mapping.findForward("prepareForOneStudent");
    }

    public ActionForward showReportForOneStudent(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	ReportStudentsUTLCandidatesBean bean = getRenderedObject("bean");

	ReportStudentsUTLCandidates report = null;
	if (bean.getForFirstYear()) {
	    report = new ReportStudentsUTLCandidatesForOneStudentFirstYear(bean.getExecutionYear(),
		    Student.readStudentByNumber(bean.getStudentNumber()));
	} else {
	    report = new ReportStudentsUTLCandidatesForOneStudent(bean.getExecutionYear(), Student.readStudentByNumber(bean
		    .getStudentNumber()));
	}

	request.setAttribute("report", report);

	List<StudentLine> correctStudentLines = new ArrayList<StudentLine>();
	List<StudentLine> erroneousStudentLines = new ArrayList<StudentLine>();

	erroneousStudentLines.addAll(report.getErroneousStudentLines());

	for (StudentLine studentLine : report.getCorrectStudentLines()) {
	    if (studentLine.isAbleToReadAllValues()) {
		correctStudentLines.add(studentLine);
	    } else {
		erroneousStudentLines.add(studentLine);
	    }
	}

	request.setAttribute("correctStudentLines", correctStudentLines);
	request.setAttribute("erroneousStudentLines", erroneousStudentLines);

	return mapping.findForward("showReport");
    }
}
