package net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdIndividualProgramProcess", module = "coordinator")
@Forwards(extend = "phd.navLocal", value = {

	@Forward(name = "manageProcesses", path = "/phd/coordinator/manageProcesses.jsp"),

	@Forward(name = "viewProcess", path = "/phd/coordinator/viewProcess.jsp"),

	@Forward(name = "viewAlertMessages", path = "/phd/coordinator/viewAlertMessages.jsp"),

	@Forward(name = "viewProcessAlertMessages", path = "/phd/coordinator/viewProcessAlertMessages.jsp")

})
public class PhdIndividualProgramProcessDA extends PhdProcessDA {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final PhdIndividualProgramProcess process = getProcess(request);

	if (process != null) {
	    request.setAttribute("processAlertMessagesToNotify", process.getUnreadedAlertMessagesFor(getLoggedPerson(request)));

	}

	return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward manageProcesses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SearchPhdIndividualProgramProcessBean searchBean = (SearchPhdIndividualProgramProcessBean) getObjectFromViewState("searchProcessBean");

	if (searchBean == null) {
	    searchBean = new SearchPhdIndividualProgramProcessBean();
	    searchBean.setExecutionYear(ExecutionYear.readCurrentExecutionYear());
	    searchBean.setPhdPrograms(getManagedPhdPrograms(request));
	}

	request.setAttribute("searchProcessBean", searchBean);

	request.setAttribute("processes", PhdIndividualProgramProcess.search(searchBean));

	return mapping.findForward("manageProcesses");
    }

    public ActionForward viewProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward("viewProcess");
    }

    @Override
    protected PhdIndividualProgramProcess getProcess(HttpServletRequest request) {
	return (PhdIndividualProgramProcess) super.getProcess(request);
    }

    protected DegreeCurricularPlan getDegreeCurricularPlan(HttpServletRequest request) {
	return rootDomainObject.readDegreeCurricularPlanByOID(getIntegerFromRequest(request, "degreeCurricularPlanID"));
    }

    // Alerts Management
    public ActionForward viewAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getLoggedPerson(request).getPhdAlertMessages());

	return mapping.findForward("viewAlertMessages");
    }

    public ActionForward markAlertMessageAsReaded(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	getAlertMessage(request).markAsReaded(getLoggedPerson(request));

	boolean globalMessagesView = StringUtils.isEmpty(request.getParameter("global"))
		|| request.getParameter("global").equals("true") ? true : false;

	return globalMessagesView ? viewAlertMessages(mapping, form, request, response) : viewProcessAlertMessages(mapping, form,
		request, response);
    }

    private PhdAlertMessage getAlertMessage(HttpServletRequest request) {
	return getDomainObject(request, "alertMessageId");
    }

    public ActionForward viewProcessAlertMessages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("alertMessages", getProcess(request).getAlertMessagesFor(getLoggedPerson(request)));

	return mapping.findForward("viewProcessAlertMessages");
    }

    private Set<PhdProgram> getManagedPhdPrograms(HttpServletRequest request) {
	final Set<PhdProgram> result = new HashSet<PhdProgram>();

	for (final Coordinator coordinator : getLoggedPerson(request).getCoordinators()) {
	    if (coordinator.getExecutionDegree().getDegree().hasPhdProgram()) {
		result.add(coordinator.getExecutionDegree().getDegree().getPhdProgram());
	    }
	}

	return result;
    }

    // End of Alerts Management
}
