package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.ManagementCouncilSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.ManagementCouncilUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;

public class ManagementCouncilSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        return getManagementCouncilUnit().getSite();
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return ManagementCouncilSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 0;
    }

    private ManagementCouncilUnit getManagementCouncilUnit() {
        return (ManagementCouncilUnit) PartyType.getPartiesSet(PartyTypeEnum.MANAGEMENT_COUNCIL).iterator().next();
    }

}
