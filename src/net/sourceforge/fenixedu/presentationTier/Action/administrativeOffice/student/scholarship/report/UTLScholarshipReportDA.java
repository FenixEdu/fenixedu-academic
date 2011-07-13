package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student.scholarship.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/student/scholarship/report/utlScholarshipReport", module = "academicAdminOffice")
@Forwards({ 
    	@Forward(name = "list", path = "/academicAdminOffice/student/scholarship/report/utl/list.jsp"),
	@Forward(name = "create", path = "/academicAdminOffice/student/scholarship/report/utl/create.jsp"),
	@Forward(name = "view", path = "/academicAdminOffice/student/scholarship/report/utl/view.jsp"),
	@Forward(name = "search", path = "/academicAdminOffice/student/scholarship/report/utl/search.jsp"),
	@Forward(name = "viewUTLScholarshipFromRegistration", path = "/academicAdminOffice/student/scholarship/report/utl/viewUTLScholarshipFromRegistration.jsp") })
public class UTLScholarshipReportDA extends FenixDispatchAction {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("list");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("create");
    }

    public ActionForward createInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("create");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("list");
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("view");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
	return mapping.findForward("cancel");
    }

    public ActionForward prepareSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("search");
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	return mapping.findForward("search");
    }

    public ActionForward viewResultsOnRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Registration registration = getDomainObject(request, "registrationId");

	UTLScholarshipReportBeanFromRegistration bean = new UTLScholarshipReportBeanFromRegistration(registration);

	request.setAttribute("utlScholarshipBean", bean);
	request.setAttribute("registration", registration);

	return mapping.findForward("viewUTLScholarshipFromRegistration");
    }

    public ActionForward downloadRegistrationResultsSpreadsheet(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Registration registration = getDomainObject(request, "registrationId");

	UTLScholarshipReportBeanFromRegistration bean = new UTLScholarshipReportBeanFromRegistration(registration);

	Spreadsheet spreadsheet = bean.buildSpreadsheet();

	response.setContentType("application/vnd.ms-excel");
	response.setHeader("filename", "bolsa_accao_social_utl_" + registration.getNumber() + ".xls");
	spreadsheet.exportToXLSSheet(response.getOutputStream());

	return null;
    }
}
