package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.MetaDomainObject;
import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;

import org.apache.struts.util.RequestUtils;

public class PedagogicalCouncilSiteManagementDA extends CustomUnitSiteManagementDA {

    @Override
    protected String getAuthorNameForFile(HttpServletRequest request, Item item) {
        return getUserView(request).getPerson().getName();
    }

	@Override
	protected String getDirectLinkContext(HttpServletRequest request) {
        
	   PedagogicalCouncilSite site = PedagogicalCouncilSite.getSite();
	   MetaDomainObject metaDomainObject = MetaDomainObject.getMeta(site.getClass());
	   
	   try {
            return RequestUtils.absoluteURL(request, metaDomainObject.getAssociatedPortal().getName().getContent() + "/" + site.getUnit().getName()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
	}

}
