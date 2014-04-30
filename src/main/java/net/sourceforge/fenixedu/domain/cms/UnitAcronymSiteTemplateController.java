package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import com.google.common.collect.Iterables;

public abstract class UnitAcronymSiteTemplateController extends SiteTemplateController {

    private UnitSite unitSite;
    private final String unitAcronym;

    public UnitAcronymSiteTemplateController(String unitAcronym) {
        this.unitAcronym = unitAcronym;
    }

    @Override
    public Site selectSiteForPath(String[] parts) {
        return getAssemblySite();
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 0;
    }

    private UnitSite getAssemblySite() {
        if (unitSite == null) {
            unitSite = getAssemblyUnit().getSite();
        }
        return unitSite;
    }

    private Unit getAssemblyUnit() {
        return Iterables.getFirst(Unit.readUnitsByAcronym(unitAcronym, true), null);
    }
}
