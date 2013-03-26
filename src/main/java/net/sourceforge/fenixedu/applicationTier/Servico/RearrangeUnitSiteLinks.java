package net.sourceforge.fenixedu.applicationTier.Servico;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLink;

public class RearrangeUnitSiteLinks extends FenixService {

    public void run(UnitSite site, Boolean top, List<UnitSiteLink> links) {
        if (top) {
            site.setTopLinksOrder(links);
        } else {
            site.setFooterLinksOrder(links);
        }
    }

}
