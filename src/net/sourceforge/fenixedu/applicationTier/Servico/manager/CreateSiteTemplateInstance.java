package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.SiteTemplate;

/**
 * Searches for the template for a certain website type. If the template already
 * exists then it's returned. Otherwise a new SiteTemplate is created and that
 * new instance is returned.
 * 
 * @author cfgi
 */
public class CreateSiteTemplateInstance extends Service {

    public SiteTemplate run(Class type) {
        SiteTemplate site = SiteTemplate.getTemplateForType(type);
        
        if (site != null) {
            return site;
        }
        else {
            return new SiteTemplate(type);
        }
    }
    
}
