package net.sourceforge.fenixedu.presentationTier.Action.phd;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.plugin.ConfigurationReader;
import pt.utl.ist.fenix.tools.util.i18n.Language;

abstract public class PhdProcessDA extends FenixDispatchAction {

    static protected final Pattern AREA_CODE_REGEX = Pattern.compile("\\d{4}-\\d{3}");
    static protected final String PHD_RESOURCES = "resources.PhdResources";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Process process = getProcess(request);
	if (process != null) {
	    request.setAttribute("processId", process.getExternalId());
	    request.setAttribute("process", process);
	}

	final Person loggedPerson = getLoggedPerson(request);
	if (loggedPerson != null) {
	    request.setAttribute("alertMessagesToNotify", loggedPerson.getUnreadedPhdAlertMessages());
	}

	return super.execute(mapping, actionForm, request, response);
    }

    /**
     * First read value from request attribute to allow overriding of processId
     * value
     */
    protected Process getProcess(HttpServletRequest request) {
	final String processIdAttribute = (String) request.getAttribute("processId");
	return (Process) DomainObject.fromExternalId(processIdAttribute != null ? processIdAttribute : (String) request
		.getParameter("processId"));

    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activity, Object activityParameter,
	    HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward) {
	return executeActivity(activity, activityParameter, request, mapping, errorForward, sucessForward, null);
    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activityClass, Object activityParameter,
	    HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward, String sucessMessage,
	    String... sucessMessageArgs) {

	try {

	    ExecuteProcessActivity.run(getProcess(request), activityClass.getSimpleName(), activityParameter);

	    if (!StringUtils.isEmpty(sucessMessage)) {
		addSuccessMessage(request, sucessMessage, sucessMessageArgs);
	    }

	    return mapping.findForward(sucessForward);

	} catch (DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());

	    return mapping.findForward(errorForward);
	}
    }

    protected void addErrorMessage(HttpServletRequest request, String key, String... args) {
	addActionMessage("error", request, key, args);
    }

    protected void addSuccessMessage(HttpServletRequest request, String key, String... args) {
	addActionMessage("success", request, key, args);
    }

    protected void addWarningMessage(HttpServletRequest request, String key, String... args) {
	addActionMessage("warning", request, key, args);
    }

    protected boolean validateAreaCodeAndAreaOfAreaCode(HttpServletRequest request, final Person person, Country country,
	    String areaCode, String areaOfAreaCode) {

	if (person != null && person.hasRole(RoleType.EMPLOYEE)) {
	    return true;
	}

	if (country.isDefaultCountry() && !StringUtils.isEmpty(areaCode) && !AREA_CODE_REGEX.matcher(areaCode).matches()) {
	    addErrorMessage(request, "error.areaCode.invalidFormat.for.national.address");

	    return false;
	}

	if (!StringUtils.isEmpty(areaCode) && StringUtils.isEmpty(areaOfAreaCode)) {
	    addErrorMessage(request, "error.areaOfAreaCode.is.required.if.areaCode.is.specified");

	    return false;
	}

	return true;
    }

    protected void setIsEmployeeAttributeAndMessage(HttpServletRequest request, Person person) {
	if (person != null && person.hasRole(RoleType.EMPLOYEE)) {
	    request.setAttribute("isEmployee", true);
	    addWarningMessage(request, "message.employee.data.must.be.updated.in.human.resources.section");
	} else {
	    request.setAttribute("isEmployee", false);
	}
    }

    protected String getMessageFromResource(final String key, Object ... args) {
	return MessageFormat.format(ResourceBundle.getBundle(PHD_RESOURCES, Language.getLocale()).getString(key), args);
    }

}
