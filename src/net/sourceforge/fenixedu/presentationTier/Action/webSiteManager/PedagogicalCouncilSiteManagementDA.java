package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.RequestUtils;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.PedagogicalCouncilProcessor;

public class PedagogicalCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getUserView(request).getPerson().getName();
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
