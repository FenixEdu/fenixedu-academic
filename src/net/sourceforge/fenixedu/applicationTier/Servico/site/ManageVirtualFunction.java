package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class ManageVirtualFunction extends Service {

    protected void checkFunction(UnitSite site, Function function) {
	checkUnit(site, function.getUnit());

	if (!function.isVirtual()) {
	    throw new DomainException("site.functions.notVirtual");
	}
    }

    protected void checkUnit(UnitSite site, Unit unit) {
	if (unit == site.getUnit()) {
	    return;
	}

	if (site.getUnit().getAllSubUnits().contains(unit)) {
	    return;
	}

	throw new DomainException("site.functions.unrelatedUnit");
    }

}
