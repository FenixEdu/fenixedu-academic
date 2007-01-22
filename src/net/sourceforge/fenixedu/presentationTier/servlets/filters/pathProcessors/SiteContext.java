package net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors;

import net.sourceforge.fenixedu.domain.Site;

public interface SiteContext {

    public Site getSite();
    public String getSiteBasePath();
    
}
