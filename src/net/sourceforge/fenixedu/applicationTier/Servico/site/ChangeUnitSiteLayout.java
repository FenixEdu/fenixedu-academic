package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.UnitSiteLayoutType;

/**
 * Changes the layout of a unit site.
 * 
 * @author cfgi
 */
public class ChangeUnitSiteLayout extends FenixService {

    public void run(UnitSite site, UnitSiteLayoutType layout) {
        site.setLayout(layout);
    }

}
