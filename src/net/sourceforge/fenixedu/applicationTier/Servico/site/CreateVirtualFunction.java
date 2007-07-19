package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateVirtualFunction extends ManageVirtualFunction {

	public Function run(UnitSite site, Unit unit, String name) {
		checkUnit(site, unit);
		return Function.createVirtualFunction(unit, name);
	}
	
}
