package net.sourceforge.fenixedu.renderers.plugin;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;

/**
 * @author cfgi
 */
public interface ExceptionHandler {
    public ActionForward processException(HttpServletRequest request, ActionForward input, Exception e);
}
