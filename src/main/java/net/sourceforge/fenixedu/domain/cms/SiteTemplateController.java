package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;

public abstract class SiteTemplateController {

    public abstract Site selectSiteForPath(String[] parts);

    public abstract Class<? extends Site> getControlledClass();

    public abstract int getTrailingPath(Site site, String[] parts);

    public static SiteTemplateController controllerForType(Class<? extends SiteTemplateController> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not instantiate custom SiteTemplateController", e);
        }
    }
}
