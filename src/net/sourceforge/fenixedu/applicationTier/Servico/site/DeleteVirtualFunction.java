package net.sourceforge.fenixedu.applicationTier.Servico.site;

import java.util.ArrayList;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

public class DeleteVirtualFunction extends ManageVirtualFunction {

	public void run(UnitSite site, Function function) {
		checkFunction(site, function);
		
		ArrayList<PersonFunction> accountability = new ArrayList<PersonFunction>(function.getPersonFunctions());
		for (PersonFunction pf : accountability) {
			pf.delete();
		}
		
		function.delete();
	}
	
}
