package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ChangeLocale extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String newLanguage = request.getParameter("newLanguage");
        final String newCountry = request.getParameter("newCountry");

        request.getSession(false).setAttribute(Action.LOCALE_KEY, new Locale(newLanguage, newCountry));
        request.getSession(false).setAttribute(Globals.LOCALE_KEY, new Locale(newLanguage, newCountry));

        final String windowLocation = request.getParameter("windowLocation");

        ActionForward actionForward = new ActionForward();

        actionForward.setContextRelative(true);

        actionForward.setName(windowLocation);
        actionForward.setPath(windowLocation);
        actionForward.setRedirect(true);

        return actionForward;
    }

}