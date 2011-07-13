package net.sourceforge.fenixedu.presentationTier.Action.messaging.profileReport;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.log.profiling.ModuleBean;
import net.sourceforge.fenixedu.dataTransferObject.log.profiling.ServerBean;
import net.sourceforge.fenixedu.domain.log.profiling.ProfileReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/profileReport", module = "messaging")
@Forwards( { @Forward(name = "viewReports", path = "/messaging/profiling/viewReports.jsp") })
public class ProfileReportDispatchAction extends FenixDispatchAction {

    public ActionForward viewReports(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	VariantBean bean = new VariantBean();
	bean.setLocalDate(new LocalDate());
	LocalDate localDate = getDate(request);

	if (localDate != null) {
	    bean.setLocalDate(localDate);
	    ProfileReport report = getReportFor(localDate);
	    if (report != null) {
		request.setAttribute("servers", report.getServers());
	    } else {
		request.setAttribute("servers", Collections.EMPTY_LIST);
	    }
	}

	request.setAttribute("date", bean);
	return mapping.findForward("viewReports");
    }

    public ActionForward viewModules(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String serverName = request.getParameter("serverName");
	LocalDate date = getDate(request);

	if (date != null) {
	    List<ModuleBean> modules = getModulesFor(serverName, date);
	    request.setAttribute("modules", modules);

	}
	return mapping.findForward("viewReports");
    }

    public ActionForward viewRequests(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String serverName = request.getParameter("serverName");
	String moduleName = request.getParameter("moduleName");

	LocalDate date = getDate(request);

	if (date != null) {
	    List<ModuleBean> requests = getRequestsFor(serverName, moduleName, date);
	    request.setAttribute("requests", requests);

	}
	return mapping.findForward("viewReports");
    }

    private List<ModuleBean> getRequestsFor(String serverName, String moduleName, LocalDate date) {
	ProfileReport report = getReportFor(date);
	ModuleBean bean = report.getReportForServerAndModule(serverName, moduleName);
	return bean != null ? bean.getRequests() : Collections.EMPTY_LIST;
    }

    private List<ModuleBean> getModulesFor(String serverName, LocalDate date) {
	ProfileReport report = getReportFor(date);
	ServerBean bean = report.getReportForServer(serverName);
	return bean != null ? bean.getModules() : Collections.EMPTY_LIST;
    }

    private LocalDate getDate(HttpServletRequest request) {
	IViewState viewState = RenderUtils.getViewState("date");
	LocalDate localDate = null;
	if (viewState != null) {
	    localDate = (LocalDate) viewState.getMetaObject().getObject();
	} else {
	    String localDateString = request.getParameter("date");
	    if (localDateString != null) {
		int year = Integer.parseInt(localDateString.substring(0, 4));
		int month = Integer.parseInt(localDateString.substring(5, 7));
		int day = Integer.parseInt(localDateString.substring(8, 10));
		localDate = new LocalDate(year, month, day);
	    }
	}
	return localDate;
    }

    private ProfileReport getReportFor(LocalDate localDate) {
	for (ProfileReport report : rootDomainObject.getProfilingLogs()) {
	    if (report.getDate().equals(localDate)) {
		return report;
	    }
	}
	return null;
    }

}
