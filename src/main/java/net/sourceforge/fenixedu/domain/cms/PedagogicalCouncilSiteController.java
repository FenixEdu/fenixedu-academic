package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.PedagogicalCouncilSite;
import net.sourceforge.fenixedu.domain.Site;

public class PedagogicalCouncilSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        return PedagogicalCouncilSite.getSite();
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return PedagogicalCouncilSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 0;
    }

}
