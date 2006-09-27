package net.sourceforge.fenixedu.presentationTier.Action;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.I18NFilter;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class BaseAuthenticationAction extends FenixAction {

    protected static final boolean useCASAuthentication;

    static {
	useCASAuthentication = PropertiesManager.getBooleanProperty("cas.enabled");
    }

    public final ActionForward execute(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {

	    String remoteHostName = getRemoteHostName(request);
	    final IUserView userView = doAuthentication(form, request, remoteHostName);

	    if (userView.getRoleTypes().isEmpty()) {
		return getAuthenticationFailedForward(mapping, request, "errors.noAuthorization",
			"errors.noAuthorization");
	    }

	    final HttpSession session = request.getSession(false);
	    if (session != null && session.getAttribute("ORIGINAL_REQUEST") != null) {
		return handleSessionRestoreAndGetForward(request, userView, session);
	    } else {
		return handleSessionCreationAndGetForward(mapping, request, userView, session);
	    }
	} catch (ExcepcaoAutenticacao e) {
	    return getAuthenticationFailedForward(mapping, request, "invalidAuthentication",
		    "errors.invalidAuthentication");
	}
    }

    protected abstract IUserView doAuthentication(ActionForm form, HttpServletRequest request,
	    String remoteHostName) throws FenixFilterException, FenixServiceException;

    protected abstract ActionForward getAuthenticationFailedForward(final ActionMapping mapping,
	    final HttpServletRequest request, final String actionKey, final String messageKey);

    private ActionForward handleSessionCreationAndGetForward(ActionMapping mapping,
	    HttpServletRequest request, IUserView userView, final HttpSession session) {
	createNewSession(request, session, userView);

	int numberOfSubApplications = getNumberOfSubApplications(userView.getRoleTypes());
	if (numberOfSubApplications == 1 || !userView.hasRoleType(RoleType.PERSON)) {
	    final Role firstInfoRole = userView.getRoleTypes().isEmpty() ? null : Role
		    .getRoleByRoleType(userView.getRoleTypes().iterator().next());
	    return buildRoleForward(firstInfoRole);
	} else {
	    return mapping.findForward("sucess");
	}

    }

    private ActionForward handleSessionRestoreAndGetForward(HttpServletRequest request,
	    IUserView userView, final HttpSession session) {
	final ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(true);

	final String originalURI = (String) session.getAttribute("ORIGINAL_URI");

	// Set request attributes
	final Map<String, Object> attributeMap = (Map<String, Object>) session
		.getAttribute("ORIGINAL_ATTRIBUTE_MAP");
	final Map<String, Object> parameterMap = (Map<String, Object>) session
		.getAttribute("ORIGINAL_PARAMETER_MAP");

	actionForward.setPath("/redirect.do");

	final HttpSession newSession = createNewSession(request, session, userView);

	newSession.setAttribute("ORIGINAL_URI", originalURI);
	newSession.setAttribute("ORIGINAL_PARAMETER_MAP", parameterMap);
	newSession.setAttribute("ORIGINAL_ATTRIBUTE_MAP", attributeMap);

	return actionForward;
    }

    private HttpSession createNewSession(final HttpServletRequest request, final HttpSession session,
	    final IUserView userView) {
	if (session != null) {
	    session.invalidate();
	}

	final HttpSession newSession = request.getSession(true);

	// Store the UserView into the session and return
	newSession.setAttribute(SessionConstants.U_VIEW, userView);
	newSession.setAttribute(SessionConstants.SESSION_IS_VALID, Boolean.TRUE);

	I18NFilter.setDefaultLocale(request, newSession);

	return newSession;
    }

    /**
         * @param userRoles
         * @return
         */
    private int getNumberOfSubApplications(final Collection<RoleType> roleTypes) {
	final Set<String> subApplications = new HashSet<String>();
	for (final RoleType roleType : roleTypes) {
	    final Role role = Role.getRoleByRoleType(roleType);
	    final String subApplication = role.getPortalSubApplication();
	    if (!subApplications.contains(subApplication) && !subApplication.equals("/teacher")) {
		subApplications.add(subApplication);
	    }
	}
	return subApplications.size();
    }

    /**
         * @param infoRole
         * @return
         */
    private ActionForward buildRoleForward(Role infoRole) {
	ActionForward actionForward = new ActionForward();
	actionForward.setContextRelative(false);
	actionForward.setRedirect(false);
	actionForward.setPath("/dotIstPortal.do?prefix=" + infoRole.getPortalSubApplication() + "&page="
		+ infoRole.getPage());
	return actionForward;
    }

    public static String getRemoteHostName(HttpServletRequest request) {
	String remoteHostName;
	try {
	    remoteHostName = InetAddress.getByName(request.getRemoteAddr()).getHostName();
	} catch (UnknownHostException e) {
	    remoteHostName = request.getRemoteHost();
	}
	return remoteHostName;
    }
}