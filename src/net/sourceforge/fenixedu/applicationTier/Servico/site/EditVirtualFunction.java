package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

public class EditVirtualFunction extends ManageVirtualFunction {

	public void run(UnitSite site, Function function, String name) {
		checkFunction(site, function);
		function.setName(name);
	}
	
}
