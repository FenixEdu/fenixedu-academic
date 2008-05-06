package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;

public class PedagogicalCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request) {
        return getUserView(request).getPerson().getName();
    }

}
