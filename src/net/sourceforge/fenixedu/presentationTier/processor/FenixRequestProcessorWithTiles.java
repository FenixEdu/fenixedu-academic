/*
 * Created on 23/Mar/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessor;

/**
 * @author jpvl
 */
public class FenixRequestProcessorWithTiles extends RenderersRequestProcessor {

	@Override
	protected boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}

}