package net.sourceforge.fenixedu.presentationTier.Action.gep.a3es;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

@Mapping(module = "gep", path = "/a3es", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "listProcesses", path = "/gep/a3es/listProcesses.jsp") })
public class A3esDispatchAction extends FenixDispatchAction {
	public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("process", new A3ESDegreeProcess());
		request.setAttribute("selection", true);
		return mapping.findForward("listProcesses");
	}

	public ActionForward select(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		A3ESDegreeProcess process = getRenderedObject("process");
		process.initialize();
		request.setAttribute("process", process);
		return mapping.findForward("listProcesses");
	}

	public ActionForward uploadCompetenceCourses(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		A3ESDegreeProcess process = getRenderedObject("process");
		request.setAttribute("process", process);
		request.setAttribute("output", process.uploadCompetenceCourses());
		return mapping.findForward("listProcesses");
	}

	public ActionForward uploadTeacherCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		A3ESDegreeProcess process = getRenderedObject("process");
		request.setAttribute("process", process);
		request.setAttribute("output", process.uploadTeacherCurriculum());
		return mapping.findForward("listProcesses");
	}

	public ActionForward exportTeacherCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		A3ESDegreeProcess process = getRenderedObject("process");
		SpreadsheetBuilder spreadsheetBuilder = process.exportTeacherCurriculum();
		response.setContentType("text/plain");
		response.setHeader("Content-disposition", "attachment; filename=a3es.xls");
		spreadsheetBuilder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
		response.flushBuffer();
		return null;
	}
}