package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;

public class ScientificCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
	return getUserView(request).getPerson().getName();
    }

}
