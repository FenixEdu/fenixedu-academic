/*
 * LogOffAction.java
 * 
 * 
 * Created on 09 de Dezembro de 2002, 16:37
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.User;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.servlets.filters.I18NFilter;

public class LogOffAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward result = null;

        final String serverName = request.getServerName();
        final CasConfig casConfig = FenixWebFramework.getConfig().getCasConfig(serverName);

        if (casConfig != null && casConfig.isCasEnabled()) {
            if (request.getParameter("logoutFromCAS") != null && request.getParameter("logoutFromCAS").equals("true")) {
                killSession(request);
                result = mapping.findForward("showBlankPage");
            } else {
                result = getCasLogoutActionForward(casConfig);
                killSession(request);
            }
        } else {
            killSession(request);
            result = mapping.findForward("showLoginPage");
        }
        // this way, we always put the locale as the default we want
        I18NFilter.setDefaultLocale(request, request.getSession());
        return result;
    }

    private ActionForward getCasLogoutActionForward(CasConfig casConfig) {
        ActionForward actionForward = new ActionForward();

        actionForward.setRedirect(true);
        actionForward.setPath(casConfig.getCasLogoutUrl());

        return actionForward;
    }

    public static void killSession(final HttpServletRequest request) {
        final IUserView userView = UserView.getUser();
        if (userView != null) {
            final Person person = userView.getPerson();
            if (person != null) {
                final User user = person.getUser();
                user.logout();
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
