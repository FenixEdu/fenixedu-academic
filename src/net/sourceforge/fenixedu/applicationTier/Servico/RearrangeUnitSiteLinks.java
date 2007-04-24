package net.sourceforge.fenixedu.applicationTier.Servico;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;

public class RearrangeUnitSiteLinks extends Service {

    public void run(UnitSite site, Boolean top, List<UnitSiteLink> links) {
        if (top) {
            site.setTopLinksOrder(links);
        }
        else  {
            site.setFooterLinksOrder(links);
        }
    }
    
}
