package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

public class UnitAnnouncementsDA extends UnitSiteAnnouncementManagement {

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
	return mapping.getPath() + ".do";
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
	StringBuilder builder = new StringBuilder();

	addExtraParameter(request, builder, "siteId");
	addExtraParameter(request, builder, "tabularVersion");
	addExtraParameter(request, builder, "oid");

	return builder.toString();
    }

}
