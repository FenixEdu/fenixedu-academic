package net.sourceforge.fenixedu.applicationTier.Servico.site;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class RearrangeUnitSiteFunctions extends ManageVirtualFunction {

    public void run(UnitSite site, Unit unit, Collection<Function> functions) {
	checkUnit(site, unit);
	unit.setFunctionsOrder(functions);
    }

}
