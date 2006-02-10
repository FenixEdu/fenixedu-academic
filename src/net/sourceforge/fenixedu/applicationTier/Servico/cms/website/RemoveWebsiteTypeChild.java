package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.Content;
import net.sourceforge.fenixedu.domain.cms.website.WebsiteType;

public class RemoveWebsiteTypeChild extends Service {

    public void run(WebsiteType websiteType, Content child) {
        websiteType.removeMandatoryContents(child);
    }
}
