package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.homepage.Homepage;

public class ShowResearchResultInHomePage extends ShowResearchResult {

    @Override
    protected void putSiteOnRequest(HttpServletRequest request) {
        Site site = SiteMapper.getSite(request);
        if (site instanceof Homepage) {
            request.setAttribute("homepage", site);
        }
    }

}
