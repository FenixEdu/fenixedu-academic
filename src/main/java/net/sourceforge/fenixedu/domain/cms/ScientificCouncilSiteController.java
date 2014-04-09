package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.ScientificCouncilSite;
import net.sourceforge.fenixedu.domain.Site;

public class ScientificCouncilSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        return ScientificCouncilSite.getSite();
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ScientificCouncilSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 0;
    }

}
