package net.sourceforge.fenixedu.domain.cms;

import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitAcronym;

import org.fenixedu.bennu.core.domain.Bennu;

public class DepartmentSiteController extends SiteTemplateController {

    @Override
    public Site selectSiteForPath(String[] parts) {
        Unit departmentUnit = null;
        for (UnitAcronym acronym : Bennu.getInstance().getUnitAcronymsSet()) {
            if (acronym.getAcronym().equalsIgnoreCase(parts[0])) {
                departmentUnit = getDepartmentFromAcronym(acronym);
                break;
            }
        }

        if (departmentUnit == null) {
            return null;
        }
        return departmentUnit.getSite();
    }

    private DepartmentUnit getDepartmentFromAcronym(UnitAcronym acronym) {
        for (Unit unit : acronym.getUnitsSet()) {
            if (unit instanceof DepartmentUnit) {
                return (DepartmentUnit) unit;
            }
        }
        return null;
    }

    @Override
    public Class<? extends Site> getControlledClass() {
        return DepartmentSite.class;
    }

    @Override
    public int getTrailingPath(Site site, String[] parts) {
        return 1;
    }

}
