/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessor;

/**
 * @author jpvl
 */
public class FenixRequestProcessorWithTiles extends RenderersRequestProcessor {

    protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
	HttpSession httpSession = request.getSession(false);
	FenixRequestProcessor.setLocal(request, httpSession);
	return true;
    }

}