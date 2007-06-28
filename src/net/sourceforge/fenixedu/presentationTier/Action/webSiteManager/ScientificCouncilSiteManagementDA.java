package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ScientificCouncilProcessor;

import org.apache.struts.util.RequestUtils;

public class ScientificCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getUserView(request).getPerson().getName();
    }

	@Override
	protected String getDirectLinkContext(HttpServletRequest request) {
        try {
            return RequestUtils.absoluteURL(request, ScientificCouncilProcessor.getScientificCouncilPath()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
	}

}
