package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import pt.ist.fenixframework.FenixFramework;

public class UnitSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        if (parts.length > 0) {
            return FenixFramework.getDomainObject(parts[0]);
        }
        return null;
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return UnitSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
