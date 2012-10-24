package net.sourceforge.fenixedu.presentationTier.Action.gep.a3es;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

@Mapping(module = "gep", path = "/a3es", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "chooseDegreeAndSemesters", path = "/gep/a3es/chooseDegreeAndSemesters.jsp") })
public class A3esDispatchAction extends FenixDispatchAction {

    public ActionForward chooseDegreeAndSemesters(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	A3esBean a3esBean = getRenderedObject();
	if (a3esBean == null) {
	    a3esBean = new A3esBean();
	}
	RenderUtils.invalidateViewState();
	request.setAttribute("a3esBean", a3esBean);
	return mapping.findForward("chooseDegreeAndSemesters");
    }

    public ActionForward exportA3es(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException, IOException {
	A3esBean a3esBean = getRenderedObject();
	if (a3esBean == null) {
	    return chooseDegreeAndSemesters(mapping, actionForm, request, response);
	}
	SpreadsheetBuilder spreadsheetBuilder = a3esBean.getTeacherCurricularInformation();
	response.setContentType("text/plain");
	response.setHeader("Content-disposition", "attachment; filename=a3es.xls");
	spreadsheetBuilder.build(WorkbookExportFormat.EXCEL, response.getOutputStream());
	response.flushBuffer();
	return null;
    }

}