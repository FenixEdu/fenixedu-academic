package net.sourceforge.fenixedu.presentationTier.Action.publico.pedagogicalCouncil;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.PedagogicalCouncilProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class ViewPedagogicalSiteDA extends UnitSiteVisualizationDA {

	@Override
	protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return presentation(mapping, form, request, response);
	}

	@Override
	protected String getDirectLinkContext(HttpServletRequest request) {
        try {
            return RequestUtils.absoluteURL(request, PedagogicalCouncilProcessor.getPedagogicalCouncilPath()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
	}
	
}
