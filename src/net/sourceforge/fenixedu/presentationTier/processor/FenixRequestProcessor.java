/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.processor;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.RequestProcessor;

/**
 * @author jpvl
 */
public class FenixRequestProcessor extends RequestProcessor {

    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
	final HttpSession httpSession = request.getSession(false);

	setLocal(request, httpSession);
	try {
	    request.setCharacterEncoding("ISO-8859-1");
	} catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
	}

	return true;
    }

    public static void setLocal(HttpServletRequest request, HttpSession httpSession) {
	Locale locale = (Locale) httpSession.getAttribute(Globals.LOCALE_KEY);
	if (locale == null) {
	    httpSession.setAttribute(Globals.LOCALE_KEY, Locale.getDefault());
	}
	locale = (Locale) request.getAttribute(Globals.LOCALE_KEY);
	if (locale == null) {
	    request.setAttribute(Globals.LOCALE_KEY, Locale.getDefault());
	}
    }

}