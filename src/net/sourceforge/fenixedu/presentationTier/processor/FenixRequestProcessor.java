/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.processor;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu._development.PropertiesManager;

import org.apache.struts.action.RequestProcessor;

/**
 * @author jpvl
 */
public class FenixRequestProcessor extends RequestProcessor {

    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
	try {
	    request.setCharacterEncoding(PropertiesManager.DEFAULT_CHARSET);
	} catch (UnsupportedEncodingException e1) {
	    e1.printStackTrace();
	}

	return true;
    }

}