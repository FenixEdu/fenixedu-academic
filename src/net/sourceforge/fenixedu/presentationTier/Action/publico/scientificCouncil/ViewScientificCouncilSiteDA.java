package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificCouncil;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.contents.MetaDomainObjectPortal;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

public class ViewScientificCouncilSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
	MetaDomainObjectPortal portal = (MetaDomainObjectPortal) MetaDomainObject.getMeta(ScientificCouncilSite.class)
		.getAssociatedPortal();
	try {
	    return RequestUtils.absoluteURL(request, portal.getNormalizedName().getContent()).toString();
	} catch (MalformedURLException e) {
	    return "";
	}
    }

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return presentation(mapping, form, request, response);
    }

}
